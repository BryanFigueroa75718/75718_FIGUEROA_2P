import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static final String RED = "\033[0;31m";
    public static final String WHITE = "\033[0;37m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String RESET = "\033[0m";  // Text Reset

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r);
        ){
            String input;

            do {
                System.out.println(CYAN_BACKGROUND  + "@" + System.getProperty("user.user") + " 📁/~" + RESET);
                System.out.print("--> ");
                input = br.readLine();

                String res = fnInput(input);

                if (input != null && !input.equalsIgnoreCase("exit()")){
                    System.out.println("comando " + input.length());
                }
            } while (input != null && !input.equalsIgnoreCase("exit()"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fnInput(String input){
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0];



        return "";
    }
}