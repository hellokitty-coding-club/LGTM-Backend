package swm.hkcc.LGTM.utils;

public class CustomMDGenerator {

    private StringBuilder sb;

    private CustomMDGenerator() {
        sb = new StringBuilder();
    }

    public static CustomMDGenerator builder() {
        return new CustomMDGenerator();
    }

    public static String tableHead(String... columns) {
        StringBuilder tempSb = new StringBuilder();
        tempSb.append("<tr>").append("\n");
        for (String column : columns) {
            tempSb.append("<th>").append(column).append("</th>").append("\n");
        }
        tempSb.append("</tr>").append("\n");
        return tempSb.toString();
    }

    public static String tableRow(String... columns) {
        StringBuilder tempSb = new StringBuilder();
        tempSb.append("<tr>").append("\n");
        for (String column : columns) {
            tempSb.append("<td>").append(column).append("</td>").append("\n");
        }
        tempSb.append("</tr>").append("\n");
        return tempSb.toString();
    }

    public String build() {
        return sb.toString();
    }

    public CustomMDGenerator h1(String title) {
        sb.append("# ").append(title).append("\n\n");
        return this;
    }

    public CustomMDGenerator h2(String title) {
        sb.append("## ").append(title).append("\n\n");
        return this;
    }

    public CustomMDGenerator h3(String title) {
        sb.append("### ").append(title).append("\n\n");
        return this;
    }

    public CustomMDGenerator h4(String title) {
        sb.append("#### ").append(title).append("\n\n");
        return this;
    }

    public CustomMDGenerator h5(String title) {
        sb.append("##### ").append(title).append("\n\n");
        return this;
    }

    public CustomMDGenerator h6(String title) {
        sb.append("###### ").append(title).append("\n\n");
        return this;
    }

    public CustomMDGenerator p(String content) {
        sb.append(content).append("\n\n");
        return this;
    }

    public CustomMDGenerator code(String content) {
        sb.append("```").append("\n");
        sb.append(content).append("\n");
        sb.append("```").append("\n\n");
        return this;
    }

    public CustomMDGenerator code(String content, String language) {
        sb.append("```").append(language).append("\n");
        sb.append(content).append("\n");
        sb.append("```").append("\n\n");
        return this;
    }

    public CustomMDGenerator quote(String content) {
        sb.append("> ").append(content).append("\n\n");
        return this;
    }

    public CustomMDGenerator ul(String content) {
        sb.append("- ").append(content).append("\n");
        return this;
    }

    public CustomMDGenerator ol(String content) {
        sb.append("1. ").append(content).append("\n");
        return this;
    }

    /**
     * 테이블을 생성합니다.
     * 예제)
     * .table(
     * CustomMDGenerator.tableHead("column1", "column2"),
     * CustomMDGenerator.tableRow("row1", "row2"),
     * CustomMDGenerator.tableRow("row3", "row4")
     * ...
     * )
     *
     * @param head
     * @param rows
     * @return CustomMDGenerator
     */
    public CustomMDGenerator table(String head, String... rows) {
        sb.append("<table>").append("\n");

        sb.append("<thead>").append("\n");
        sb.append(head).append("\n");
        sb.append("</thead>").append("\n");

        sb.append("<tbody>").append("\n");
        for (String row : rows) {
            sb.append(row).append("\n");
        }
        sb.append("</tbody>").append("\n");

        sb.append("</table>").append("\n\n");
        return this;
    }

    public CustomMDGenerator br() {
        sb.append("\n");
        return this;
    }

    public CustomMDGenerator line() {
        sb.append("---").append("\n\n");
        return this;
    }

}
