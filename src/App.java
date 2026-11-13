import java.io.BufferedReader;
import java.util.List;
import java.io.File;
import java.nio.file.Path;

public class App {
    public static String version = "1.0.0";
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
                writer.write("<!DOCTYPE html>\n<head>\n<script src=\"https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js\"></script>\n</head>\n<html>\n<body>\n");
                for (Paragraph paragraph: result){
                    writer.write(paragraph.getHTMLContent());
                    writer.newLine();
                }
                writer.write("</body>\n</html>");
            }
        }
    }
    public static void showHelp() {
        System.out.println("PMDown Version " + version);
        System.out.println("Usage: java PMDown (file) (output) [options...]");
        System.out.println("file: The path to the Markdown file to process");
        System.out.println("output: The path to the output file");
        System.out.println("Options:");
        System.out.println("  -h, --help    Show this help message");
        System.out.println("Exit codes:");
        System.out.println("  0: Program exit normaly");
        System.out.println("  1: Program cannot find specified file");
    }
}
