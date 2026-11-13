import java.util.List;

public class Parser {
    public static List<Paragraph> parse(String markdown) {
        List<Paragraph> result = new java.util.ArrayList<>();
        Paragraph currentParagraph = new Paragraph();
        String currentBlock = "";
        ParagraphStyle currentParagraphStyle = null;
        for (int i = 0; i < markdown.length(); i++){
            char c = markdown.charAt(i);
            if (c == '\\' && i < markdown.length()-1){
                currentBlock += markdown.charAt(i+1);
                i++;
            } else if (c == '[') {
                if (currentBlock != ""){
                    Block block = new Block(new TextBlock(currentBlock), null);
                    currentParagraph.addBlock(block);
                }
                String linkText = "", linkUrl = "";
                for (; i < markdown.length() && markdown.charAt(i) != ']'; i++){
                    linkText += markdown.charAt(i);
                }
                i += 2; // Skip the "](" characters
                for (; i < markdown.length() && markdown.charAt(i) != ')'; i++){
                    linkUrl += markdown.charAt(i);
                }
                Block block = new Block(new LinkBlock(linkText, linkUrl), BlockStyle.LINK);
                currentParagraph.addBlock(block);
            } else if (c == '#'){
                int sharpCnt = 1;
                i++;
                for (; i < markdown.length() && markdown.charAt(i) == '#'; i++){
                    sharpCnt++;
                }
                switch (sharpCnt) {
                    case 1:
                        currentParagraphStyle = ParagraphStyle.H1;
                        break;
                    case 2:
                        currentParagraphStyle = ParagraphStyle.H2;
                        break;
                    case 3:
                        currentParagraphStyle = ParagraphStyle.H3;
                        break;
                    case 4:
                        currentParagraphStyle = ParagraphStyle.H4;
                        break;
                    case 5:
                        currentParagraphStyle = ParagraphStyle.H5;
                        break;
                    case 6:
                        currentParagraphStyle = ParagraphStyle.H6;
                        break;
                    default:
                        currentParagraphStyle = null;
                        break;
                }
            } else if (c == '*' || c == '~' || c == '$'){
                if (currentBlock != ""){
                    Block block = new Block(new TextBlock(currentBlock), null);
                    currentParagraph.addBlock(block);
                }
                currentBlock = "";
                String style = "";
                style += c;
                i++;
                if (c == '*' || c == '~'){
                    if (i < markdown.length() && markdown.charAt(i) == c){
                        style += c;
                        i++;
                    }
                }
                for (; i < markdown.length() && !markdown.substring(i).startsWith(style); i++){
                    currentBlock += markdown.charAt(i);
                }
                i += style.length()-1;
                BlockStyle blockStyle = null;
                switch (style){
                    case "*":
                        blockStyle = BlockStyle.ITALIC;
                        break;
                    case "**":
                        blockStyle = BlockStyle.BOLD;
                        break;
                    case "~~":
                        blockStyle = BlockStyle.STRIKETHROUGH;
                        break;
                    case "$":
                        blockStyle = BlockStyle.MATH;
                        break;
                }
                Block block = new Block(new TextBlock(currentBlock), blockStyle);
                currentParagraph.addBlock(block);
                currentBlock = "";
            } else if (c == '\n'){ 
                if (i+1 < markdown.length() && (markdown.charAt(i+1) == '\n')){
                    if (currentBlock != ""){
                        Block block = new Block(new TextBlock(currentBlock), null);
                        currentParagraph.addBlock(block);
                    }
                    result.add(currentParagraph.changeStyle(currentParagraphStyle));
                    currentParagraph = new Paragraph();
                    currentBlock = "";
                    i++;
                }
                currentParagraphStyle = null;
             } else {
                currentBlock += c;
            }
        }
        if (currentBlock != ""){
            Block block = new Block(new TextBlock(currentBlock), null);
            currentParagraph.addBlock(block);
        }
        result.add(currentParagraph.changeStyle(currentParagraphStyle));
        return result;
    }
}
