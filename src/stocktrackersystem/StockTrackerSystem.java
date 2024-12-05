    package stocktrackersystem;

import java.util.InputMismatchException;
import java.util.Scanner;

public class StockTrackerSystem {

    public static void main(String[] args) {
         Scanner sc = new Scanner(System.in);
        boolean exit = true;

        do { 
            System.out.println("\n---------------------------------");
            System.out.println("=== STOCK TRACKER SYSTEM ===");
            System.out.println("\tfor clothing");
            System.out.println("---------------------------------");
            
            System.out.println("\n== MAIN MENU ==");
            System.out.println("1. Items");
            System.out.println("2. Stocks Report");
            System.out.println("3. Exit");
            
            int action = 0;
            boolean validAction = false;

           
            while (!validAction) {
                System.out.print("Enter Action (1-3): ");
                try {
                    action = sc.nextInt();
                    if (action >= 1 && action <= 3) {
                        validAction = true;
                    } else {
                        System.out.println("Invalid action. Please choose between 1 and 3.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 3.");
                    sc.next();
                }
            }
            
            switch (action) {
                case 1:
                    Items i = new Items();
                    i.ITEMS();
                  
                    break;
                    
                case 2:
                     Stocks s = new Stocks();
                     s.STOCKS();
                    break;
                    
                case 3:
                    boolean validResponse = false;
                    while (!validResponse) {
                        System.out.print("Exit Selected... Type 'yes' to confirm: ");
                        String res = sc.next().trim();
                        if (res.equalsIgnoreCase("yes")) {
                            exit = false;
                            validResponse = true;
                            System.out.println("-- THANK YOU FOR USING THE PROGRAM --");
                        } else {
                            System.out.println("Exit canceled. Returning to main menu.");
                            validResponse = true; 
                        }
                    }
                    break;
            }
            
        } while (exit);

        sc.close(); 
    }
}
        
        
        
 
