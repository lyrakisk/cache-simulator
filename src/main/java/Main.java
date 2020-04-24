import java.io.FileNotFoundException;

public class Main {

    /**
     * Run parser.
     * @param args args.
     */
    public static void main(String[] args) {

        AbstractParserClass parserClass = new ConcreteParser();

        parserClass.parse("src/main/resources/request.txt");
    }

}
