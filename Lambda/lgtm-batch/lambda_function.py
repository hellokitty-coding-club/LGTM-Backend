#
# 미션 정보를 통해 추천 미션 인덱싱을 수행하는 람다 함수
#

import pymysql
import os

connection = pymysql.connect(
    host = os.getenv('DB_HOST'),
    user = os.getenv('DB_USERNAME'),
    passwd = os.getenv('DB_PASSWORD'),
    db = os.getenv('DB_NAME'),
    port = int(os.getenv('DB_PORT'))
)  # db 접근 하기 위한 정보

global GET_RECOMMENDATION_TABLE_QUERY, UPDATE_QUERY, DELETE_FINISHED_MISSION_QUERY

GET_RECOMMENDATION_TABLE_QUERY = """WITH UserRelatedMissions AS (
    select member_id, mission_id, sum(weight) AS weight
     from (SELECT junior_id AS member_id, mission_id, 5 AS weight
           FROM mission_registration
           WHERE junior_id = {0}

           union all

           SELECT scrapper_id AS member_id, mission_id, 1 AS weight
           FROM mission_scrap
           WHERE scrapper_id = {0}

           union all

           SELECT viewer_id AS member_id, mission_id, 1 AS weight
           FROM mission_view
           WHERE viewer_id = {0}) MY_MISSIONS
     group by mission_id
)

, RelatedRates AS (
    -- 각 미션과 연관된 미션들의 rate 계산
    SELECT
        um.member_id,
        um.mission_id AS user_related_mission,
        CASE
            WHEN t1.mission_id = um.mission_id THEN t2.mission_id
            ELSE t1.mission_id
        END AS related_mission,
        COUNT(DISTINCT t1.tech_tag_id) * um.weight AS rate
    FROM
        UserRelatedMissions um
    JOIN
        tech_tag_per_mission AS t1 ON um.mission_id = t1.mission_id
    JOIN
        tech_tag_per_mission AS t2 ON t1.tech_tag_id = t2.tech_tag_id AND t1.mission_id != t2.mission_id
    WHERE
        t1.is_deleted = 0
        AND t2.is_deleted = 0
        AND t2.mission_id NOT IN (SELECT mission_id FROM UserRelatedMissions)
    GROUP BY
        um.mission_id,
        CASE
            WHEN t1.mission_id = um.mission_id THEN t2.mission_id
            ELSE t1.mission_id
        END
)

# select * from RelatedRates;

-- 연관된 미션들의 rate 합계
SELECT
    member_id,
    related_mission,
    SUM(rate) AS total_rate
FROM
    RelatedRates
JOIN mission m ON m.mission_id = RelatedRates.related_mission
WHERE m.mission_status = 'RECRUITING'
GROUP BY
    related_mission
ORDER BY
    total_rate DESC;
"""

UPDATE_QUERY = """
INSERT INTO mission_recommendation (member_id, mission_id, total_rate)
VALUES ({0}, {1}, {2})
ON DUPLICATE KEY UPDATE total_rate = {0};
"""

DELETE_FINISHED_MISSION_QUERY = """
DELETE FROM mission_recommendation
WHERE mission_id IN (SELECT mission_id FROM mission WHERE mission_status != 'RECRUITING');
"""


def lambda_handler(event, context):
    global GET_RECOMMENDATION_TABLE_QUERY
    global UPDATE_QUERY
    global DELETE_FINISHED_MISSION_QUERY

    cursor = connection.cursor()  # DB에 접속 및 DB 객체를 가져옴
    cursor.execute("select member_id from member;")  # SQL 문장을 DB 서버에 보냄

    memberIds = cursor.fetchall()  # 데이터를 DB로부터 가져온 후, Fetch 된 데이터를 사용

    totalResult = [];

    for _memberId in memberIds:
        memberId = _memberId[0]

        cursor.execute(GET_RECOMMENDATION_TABLE_QUERY.format(str(memberId)))
        result = cursor.fetchall()
        totalResult += result

    for r in totalResult:
        cursor.execute(UPDATE_QUERY.format(r[0], r[1], r[2]))

    cursor.execute(DELETE_FINISHED_MISSION_QUERY)
    connection.commit()
