import java.util.List;
import java.util.ArrayList;

public class Parser {
    public static List<Paragraph> parse(String markdown) {
        List<Paragraph> result = new java.util.ArrayList<>();
        Paragraph currentParagraph = new Paragraph();
        String currentBlock = "";
        ParagraphStyle currentParagraphStyle = null;
        boolean tableAdded = false;
        for (int i = 0; i < markdown.length(); i++){
            char c = markdown.charAt(i);
            if (c == '\\' && i < markdown.length()-1){
                currentBlock += markdown.charAt(i+1);
                i++;
            } else if (c == '|') {
                if (!currentBlock.trim().isEmpty()) {
                    currentParagraph.addBlock(new Block(new TextBlock(currentBlock), null));
                }
                currentBlock = "";
                if (currentParagraph.hasContent()) {
                    result.add(currentParagraph.changeStyle(currentParagraphStyle));
                }

                TableParseResult table = parseTable(markdown, i);
                Paragraph tableParagraph = new Paragraph();
                tableParagraph.addBlock(new Block(table.tableBlock, null));
                result.add(tableParagraph);
                currentParagraph = new Paragraph();
                currentParagraphStyle = null;
                tableAdded = true;
                i = table.endIndex;
            } else if (c == '[') {
                if (currentBlock != ""){
                    Block block = new Block(new TextBlock(currentBlock), null);
                    currentParagraph.addBlock(block);
                }
                String linkText = "", linkUrl = "";
                i++; // Skip the '[' character
                for (; i < markdown.length() && markdown.charAt(i) != ']'; i++){
                    linkText += markdown.charAt(i);
                }
                i += 2; // Skip the "](" characters
                for (; i < markdown.length() && markdown.charAt(i) != ')'; i++){
                    linkUrl += markdown.charAt(i);
                }
                Block block = new Block(new LinkBlock(linkUrl, linkText), BlockStyle.LINK);
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
            } else if (c == '-' || c == '1'){
                if (i+1 < markdown.length() && markdown.charAt(i+1) == '.'){
                    if (currentBlock != ""){
                        Block block = new Block(new TextBlock(currentBlock), null);
                        currentParagraph.addBlock(block);
                    }
                    result.add(currentParagraph.changeStyle(currentParagraphStyle));
                    currentParagraph = new Paragraph();
                    currentParagraphStyle = null;
                    currentBlock = "";
                    if (c == '-'){
                        currentParagraphStyle = ParagraphStyle.UNORDERED_LIST;
                    } else if (c == '1'){
                        currentParagraphStyle = ParagraphStyle.ORDERED_LIST;
                    }
                    i += 2;
                    do {
                        int blockStart = i;
                        while (i < markdown.length() && markdown.charAt(i) != '\n'){
                            i++;
                        }
                        int blockEnd = i;
                        String line = markdown.substring(blockStart, blockEnd);
                        List<Paragraph> lineParagraphs = parse(line);
                        if (lineParagraphs.size() >= 1){
                            Paragraph lineBlocks = lineParagraphs.get(0);
                            for (Block block : lineBlocks.getContent()) {
                                currentParagraph.addBlock(block);
                            }
                            currentParagraph.addBlock(new Block(new TextBlock(""), BlockStyle.NEWORDERITEM));
                        }
                        i++;
                    } while (i < markdown.length() && markdown.charAt(i) != '\n');
                    currentParagraph.removeEndOfContent();
                    result.add(currentParagraph.changeStyle(currentParagraphStyle));
                    currentParagraph = new Paragraph();
                    currentParagraphStyle = null;
                    currentBlock = "";
                } else {
                    currentBlock += c;
                }
            } else if (c == '\n'){ 
                if (i+1 < markdown.length() && (markdown.charAt(i+1) == '\n')){
                    if (currentBlock != ""){
                        Block block = new Block(new TextBlock(currentBlock), null);
                        currentParagraph.addBlock(block);
                    }
                    result.add(currentParagraph.changeStyle(currentParagraphStyle));
                    currentParagraph = new Paragraph();
                    currentParagraphStyle = null;
                    currentBlock = "";
                    i++;
                }
             } else {
                currentBlock += c;
            }
        }
        if (currentBlock != ""){
            Block block = new Block(new TextBlock(currentBlock), null);
            currentParagraph.addBlock(block);
        }
        if (currentParagraph.hasContent() || currentParagraphStyle != null || !tableAdded) {
            result.add(currentParagraph.changeStyle(currentParagraphStyle));
        }
        return result;
    }

    private static TableParseResult parseTable(String markdown, int startIndex) {
        List<String[]> rows = new ArrayList<>();
        int tableWidth = 0;
        int tableTitleHeight = 0;
        int separatorCount = 0;
        boolean ignoreRows = false;
        int lineStart = startIndex;
        int endIndex = startIndex;

        while (lineStart < markdown.length()) {
            int lineEnd = markdown.indexOf('\n', lineStart);
            if (lineEnd == -1) {
                lineEnd = markdown.length();
            }

            String[] cells = parseTableRow(markdown.substring(lineStart, lineEnd).trim());
            if (tableWidth == 0) {
                tableWidth = cells.length;
            }

            if (isSeparatorRow(cells)) {
                separatorCount++;
                if (separatorCount == 1) {
                    tableTitleHeight = rows.size();
                } else {
                    ignoreRows = true;
                }
            } else if (!ignoreRows) {
                rows.add(normalizeRow(cells, tableWidth));
            }

            endIndex = lineEnd;
            if (lineEnd == markdown.length()) {
                break;
            }

            lineStart = lineEnd + 1;
            while (lineStart < markdown.length()
                    && (markdown.charAt(lineStart) == ' ' || markdown.charAt(lineStart) == '\t')) {
                lineStart++;
            }
            if (lineStart >= markdown.length() || markdown.charAt(lineStart) != '|') {
                break;
            }
        }

        String[] content = new String[rows.size() * tableWidth];
        for (int row = 0; row < rows.size(); row++) {
            System.arraycopy(rows.get(row), 0, content, row * tableWidth, tableWidth);
        }
        return new TableParseResult(
                new TableBlock(content, tableWidth, tableTitleHeight),
                endIndex
        );
    }

    private static String[] parseTableRow(String row) {
        String cellsText = row.substring(1);
        if (cellsText.endsWith("|")) {
            cellsText = cellsText.substring(0, cellsText.length() - 1);
        }
        String[] cells = cellsText.split("\\|", -1);
        for (int i = 0; i < cells.length; i++) {
            cells[i] = cells[i].trim();
        }
        return cells;
    }

    private static boolean isSeparatorRow(String[] cells) {
        for (String cell : cells) {
            if (!cell.equals("---")) {
                return false;
            }
        }
        return true;
    }

    private static String[] normalizeRow(String[] cells, int tableWidth) {
        String[] normalized = new String[tableWidth];
        for (int i = 0; i < tableWidth; i++) {
            normalized[i] = i < cells.length ? cells[i] : "";
        }
        return normalized;
    }

    private static class TableParseResult {
        private final TableBlock tableBlock;
        private final int endIndex;

        private TableParseResult(TableBlock tableBlock, int endIndex) {
            this.tableBlock = tableBlock;
            this.endIndex = endIndex;
        }
    }
}
