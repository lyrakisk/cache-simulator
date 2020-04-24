import java.io.FileNotFoundException;

public class Main {

    /**
     * Run parser.
     * @param args args.
     */
    public static void main(String[] args) {

        AbstractParserClass parserClass = new ConcreteParser();

        try {
            parserClass.parse("src/main/resources/request.txt");
        } catch (FileNotFoundException e) {
            System.out.println("file not found ");
            e.printStackTrace();
        }
    }

}
