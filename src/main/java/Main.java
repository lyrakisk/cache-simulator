import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {

        AbstractParserClass parserClass = new ConcreteParser();

        try {
            parserClass.parse("C:\\Users\\nana1\\Documents\\opendc\\src\\main\\java\\parser\\resources\\request.txt");
        } catch (FileNotFoundException e) {
            System.out.println("file not found " );
            e.printStackTrace();
        }
    }

}
