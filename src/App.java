public class App {
    public static String version = "1.0.0";
    public static void main(String[] args) throws Exception {
        if ((args.length > 0 && (args[0].equals("-h") || args[0].equals("--help"))) || args.length == 0) {
            showHelp();
            return;
        } else {
            System.out.println("Hello, World!");
        }
    }
    public static void showHelp() {
        System.out.println("PMDown Version " + version);
        System.out.println("Usage: java PMDown (file) (output) [options...]");
        System.out.println("file: The path to the Markdown file to process");
        System.out.println("output: The path to the output file");
        System.out.println("Options:");
        System.out.println("  -h, --help    Show this help message");
    }
}
