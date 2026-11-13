import java.util.List;

public class Paragraph {
    private List<Block> content;
    private ParagraphStyle style;
    public Paragraph(List<Block> content, ParagraphStyle style) {
        this.content = content;
        this.style = style;
    }
    public Paragraph() {
        this.content = new java.util.ArrayList<Block>();
        this.style = null;
    }
    public String getHTMLContent() {
        StringBuilder html = new StringBuilder();
        for (Block block : content) {
            html.append(block.getHTMLContent());
        }
        if (style == null){
            return "<p>" + html.toString() + "</p>";
        }
        switch (style) {
            case H1:
                return "<h1>" + html.toString() + "</h1>";
            case H2:
                return "<h2>" + html.toString() + "</h2>";
            case H3:
                return "<h3>" + html.toString() + "</h3>";
            case H4:
                return "<h4>" + html.toString() + "</h4>";
            case H5:
                return "<h5>" + html.toString() + "</h5>";
            case H6:
                return "<h6>" + html.toString() + "</h6>";
            case BLOCKQUOTE:
                return "<blockquote>" + html.toString() + "</blockquote>";
            case CODEBLOCK:
                return "<pre><code>" + html.toString() + "</code></pre>";
            case UNORDERED_LIST:
                return "<ul><li>" + html.toString() + "</li></ul>";
            case ORDERED_LIST:
                return "<ol><li>" + html.toString() + "</li></ol>";
            default:
                return "<p>" + html.toString() + "</p>";
        }
    }
    public void addBlock(Block add){
        content.add(add);
    }
}
