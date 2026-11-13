public class Block {
    private ContentBlock content;
    private BlockStyle style;
    public Block(ContentBlock content, BlockStyle style) {
        this.content = content;
        this.style = style;
    }
    public String getHTMLContent() {
        if (style == null) {
            return content.getContent()[0];
        }
        switch (style){
            case BOLD:
                return "<strong>" + content.getContent()[0] + "</strong>";
            case ITALIC:
                return "<em>" + content.getContent()[0] + "</em>";
            case STRIKETHROUGH:
                return "<del>" + content.getContent()[0] + "</del>";
            case INLINECODE:
                return "<code>" + content.getContent()[0] + "</code>";
            case MATH:
                return "<p>\\(" + content.getContent()[0] + "\\)</p>";
            case LINK:
                String[] linkContent = content.getContent();
                return "<a href=\"" + linkContent[0] + "\">" + linkContent[1] + "</a>";
            default:
                return content.getContent()[0];
        }
    }
}