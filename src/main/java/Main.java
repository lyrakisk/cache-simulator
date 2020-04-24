import java.io.FileNotFoundException;

public class Main {

    /**
     * Run parser.
     * @param args args.
     */
    public static void main(String[] args) {

        AbstractParserClass parserClass = new ConcreteParser();

        try {
            parserClass.parse("/parser/resources/request.txt");
        } catch (FileNotFoundException e) {
            System.out.println("file not found ");
            e.printStackTrace();
        }
    }

}
