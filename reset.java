import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class reset {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner pricesimport = new Scanner(new File("origprices.txt"));
        PrintWriter pricesexport = new PrintWriter("prices.txt");
        while (pricesimport.hasNextLine()) {
            pricesexport.println(pricesimport.nextLine());
        }
        pricesexport.close();
        PrintWriter days = new PrintWriter("days.txt");
        days.println(1);
        days.close();
        PrintWriter balance = new PrintWriter("balance.txt");
        balance.println(10000);
        balance.println("AMZN 3"); // Amazon leave plan

        // balance.println(1000000); // Small loan of 1 million dollars plan
        
        balance.close();
    }
}
