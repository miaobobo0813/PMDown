public class LinkBlock implements ContentBlock {
    private String url;
    private String text;

    public LinkBlock(String url, String text) {
        this.url = url;
        this.text = text;
    }

    @Override public String[] getContent() {
        return new String[]{url, text};
    }
}