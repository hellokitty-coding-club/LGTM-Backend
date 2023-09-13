package swm.hkcc.LGTM.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataRedisTest
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("test")
class RedisConfigTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testStrings() {
        final String key = "stringKey";
        final ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();

        valueOperations.set(key, "value1");
        final String result1 = valueOperations.get(key);

        assertEquals("value1", result1);
    }

    @Test
    public void testList() {
        final String key = "listKey";
        final ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();

        listOperations.rightPush(key, "value1");
        listOperations.rightPush(key, "value2");
        listOperations.rightPush(key, "value3");

        final String char1 = listOperations.index(key, 0);
        final String char2 = listOperations.index(key, 1);
        final String char3 = listOperations.index(key, 2);

        assertEquals("value1", char1);
        assertEquals("value2", char2);
        assertEquals("value3", char3);
    }

    @Test
    public void testHash() {
        final String key = "chanho";
        final HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();

        hashOperations.put(key, "name", "chanho");
        hashOperations.put(key, "age", "23");
        hashOperations.put(key, "job", "developer");

        assertThat(hashOperations.get(key, "name")).isEqualTo("chanho");
        assertThat(hashOperations.get(key, "age")).isEqualTo("23");
        assertThat(hashOperations.get(key, "job")).isEqualTo("developer");
    }

}