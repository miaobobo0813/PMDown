import java.io.BufferedReader;
import java.util.List;
import java.io.File;
import java.nio.file.Path;

public class App {
    public static String version = "1.3.0";
    public static void main(String[] args) throws Exception {
        if ((args.length > 0 && (args[0].equals("-h") || args[0].equals("--help"))) || args.length == 0) {
            showHelp();
            return;
        } else {
            File outputFile = null;
            if (args.length <= 1){
                Path markdownPath = Path.of(args[0]).toAbsolutePath();
                String markdownFilename = markdownPath.getFileName().toString();
                String outputFileName = markdownFilename.substring(0, (markdownFilename.lastIndexOf('.') == -1 ? markdownFilename.length() : markdownFilename.lastIndexOf('.')))+".html";
                outputFile = new File(markdownPath.getParent().toString(), outputFileName);
            } else {
                String outputFilePath = args[1];
                if (!outputFilePath.toLowerCase().endsWith(".html")){
                    outputFilePath += ".html";
                }
                outputFile = new File(outputFilePath);
            }
            File markdownFile = new File(args[0]);
            if (!markdownFile.exists()){
                System.err.println(String.format("\033[31mError\033[0m: File %s do not exists.", args[0]));
                System.exit(1);
            }
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(args[0]), "UTF-8"));
            StringBuilder markdownContent = new StringBuilder();
            String currentLine;
            while ((currentLine = reader.readLine()) != null){
                markdownContent.append(currentLine).append("\n");
            }
            reader.close();
            List<Paragraph> result = Parser.parse(markdownContent.toString());
            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(outputFile), "UTF-8"))){
                writer.write("<!DOCTYPE html>\n");
                writer.write("<html>\n");
                writer.write("<head>\n");
                writer.write("<meta charset=\"UTF-8\">\n");
                writer.write("<script src=\"https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js\"></script>\n");
                writer.write("<style>\n");
                writer.write(cssTemplate);
                writer.write("</style>\n");
                writer.write("</head>\n");
                writer.write("<body>\n");
                for (Paragraph paragraph: result){
                    writer.write(paragraph.getHTMLContent());
                    writer.newLine();
                }
                writer.write("</body>\n");
                writer.write("</html>");
            }
        }
    }
    public static void showHelp() {
        System.out.println("PMDown Version " + version);
        System.out.println();
        System.out.println("Usage: java -jar PMDown.jar <file> [output] [options...]");
        System.out.println();
        System.out.println("<file>");
        System.out.println("  Description");
        System.out.println("    The path to the Markdown file to process");
        System.out.println("  Details");
        System.out.println("    Required");
        System.out.println("    Position: 1st");
        System.out.println("    Default: /");
        System.out.println();
        System.out.println("[output]");
        System.out.println("  Description");
        System.out.println("    The path to the output file");
        System.out.println("  Details");
        System.out.println("    Optional");
        System.out.println("    Position: 2nd");
        System.out.println("    Default: <file>.md -> <file>.html");
        System.out.println();
        System.out.println("[options...]");
        System.out.println("  Description");
        System.out.println("    Extra options to customize generation");
        System.out.println("  Avaliable options");
        System.out.println("    -h, --help");
        System.out.println("      Description");
        System.out.println("        Show this help message");
        System.out.println("      Details");
        System.out.println("        Optional");
        System.out.println("        Position: [options...]->?");
        System.out.println("        No value required");
        System.out.println();
        System.out.println("Exit codes:");
        System.out.println("  0: Program exit normaly");
        System.out.println("  1: Program cannot find specified file");
    }
    private static String cssTemplate = """
    table { border-collapse: collapse; }
    td, th { border: 1px solid #ddd; padding: 8px; }
    code { background-color: #f2f2f2; }
    pre code { background: none; }
    pre { background-color: #f2f2f2; overflow-x: auto; }
    strong:has(+ pre) { 
        display: block; 
        background: #e8e8e8; 
        padding: .4em 1em; 
        border: 1px solid #ddd; 
        border-bottom: none; 
        margin-bottom: 0; 
    }
    strong:has(+ pre) + pre {
        margin-top: 0; 
        background: #f2f2f2; 
        border: 1px solid #ddd; 
        padding: 1em; 
        overflow-x: auto; 
        margin-bottom: 1em; 
    }
    strong:has(+ pre) + pre code { background: none; padding: 0; }
    """;
}
