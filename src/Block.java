import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class Block {
    private ContentBlock content;
    private BlockStyle style;
    public Block(ContentBlock content, BlockStyle style) {
        this.content = content;
        this.style = style;
    }
    public String getHTMLContent() {
        if (content instanceof TableBlock) {
            return ((TableBlock) content).getHTMLContent();
        }
        String blockContent = content.getContent()[0];
        if (style == null) {
            return escapeHtml(blockContent);
        }
        switch (style){
            case BOLD:
                return "<strong>" + escapeHtml(blockContent) + "</strong>";
            case ITALIC:
                return "<em>" + escapeHtml(blockContent) + "</em>";
            case STRIKETHROUGH:
                return "<del>" + escapeHtml(blockContent) + "</del>";
            case INLINECODE:
                return "<code>" + escapeHtml(blockContent) + "</code>";
            case MATH:
                return "\\(" + escapeHtml(blockContent) + "\\)";
            case LINK:
                String[] linkContent = content.getContent();
                if (!isSafeLink(linkContent[0])) {
                    return escapeHtml(linkContent[1]);
                }
                return "<a href=\"" + escapeHtml(linkContent[0]) + "\">"
                        + escapeHtml(linkContent[1]) + "</a>";
            case NEWORDERITEM:
                return "</li><li>";
            default:
                return escapeHtml(blockContent);
        }
    }

    static String escapeHtml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    boolean isTableBlock() {
        return content instanceof TableBlock;
    }

    private static boolean isSafeLink(String url) {
        try {
            String scheme = new URI(url).getScheme();
            if (scheme == null) {
                return true;
            }
            scheme = scheme.toLowerCase(Locale.ROOT);
            return scheme.equals("http") || scheme.equals("https") || scheme.equals("mailto");
        } catch (URISyntaxException exception) {
            return false;
        }
    }
}