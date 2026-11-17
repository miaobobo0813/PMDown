public class TableBlock implements ContentBlock {
    private final String[] content;
    private final int tableWidth;
    private final int tableTitleHeight;

    public TableBlock(String[] content, int tableWidth, int tableTitleHeight) {
        this.content = content;
        this.tableWidth = tableWidth;
        this.tableTitleHeight = tableTitleHeight;
    }

    @Override
    public String[] getContent() {
        return content;
    }

    public int getTableWidth() {
        return tableWidth;
    }

    public int getTableTitleHeight() {
        return tableTitleHeight;
    }

    public String getHTMLContent() {
        StringBuilder html = new StringBuilder("<table>");
        int rowCount = tableWidth == 0 ? 0 : content.length / tableWidth;
        int headerRows = Math.min(tableTitleHeight, rowCount);

        if (headerRows > 0) {
            html.append("<thead>");
            appendRows(html, 0, headerRows, "th");
            html.append("</thead>");
        }

        html.append("<tbody>");
        appendRows(html, headerRows, rowCount, "td");
        html.append("</tbody></table>");
        return html.toString();
    }

    private void appendRows(StringBuilder html, int startRow, int endRow, String cellTag) {
        for (int row = startRow; row < endRow; row++) {
            html.append("<tr>");
            for (int column = 0; column < tableWidth; column++) {
                html.append('<').append(cellTag).append('>')
                        .append(content[row * tableWidth + column])
                        .append("</").append(cellTag).append('>');
            }
            html.append("</tr>");
        }
    }
}
