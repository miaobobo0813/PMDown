import java.io.BufferedReader;
import java.util.List;
import java.io.File;

public class App {
    public static String version = "1.0.0";
    public static void main(String[] args) throws Exception {
        if ((args.length > 0 && (args[0].equals("-h") || args[0].equals("--help"))) || args.length == 0) {
            showHelp();
            return;
        } else {
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
            for (Paragraph paragraph: result){
                System.out.println(paragraph.getHTMLContent());
                System.out.println("<br>");
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
