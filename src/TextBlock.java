public class TextBlock implements ContentBlock {
    private String text;
    public TextBlock(String text) {
        this.text = text;
    }
    @Override public String[] getContent() {
        return new String[]{text};
    }
}
