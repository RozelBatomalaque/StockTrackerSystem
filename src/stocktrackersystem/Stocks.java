package stocktrackersystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Stocks {

    public void STOCKS() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("-------------------------------------------");
            System.out.println("\n== STOCKS MENU ==");
            System.out.println("1. Add Stock");
            System.out.println("2. View Stock Reports");
            System.out.println("3. Update Stock");
            System.out.println("4. Delete Stock");
            System.out.println("5. Exit Stock Menu");
            System.out.println("-------------------------------------------");

            int action = 0;
            boolean validAction = false;

            while (!validAction) {
                System.out.print("Enter Action (1-5): ");
                try {
                    action = sc.nextInt();
                    if (action >= 1 && action <= 5) {
                        validAction = true;
                    } else {
                        System.out.println("Invalid action. Please choose a number between 1 and 5.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
                    sc.next(); 
                }
            }

            switch (action) {
                case 1:                                 
                    addStock();
                    break;
                case 2:
                    viewStock();
                    break;
                case 3:
                    viewStock();
                    updateStock();
                    viewStock();
                    break;
                case 4:
                    viewStock();
                    deleteStock();
                    viewStock();
                    break;
                case 5:
                    System.out.println("-- Exiting Stock Menu --");
                    return; 
            }

            boolean validResponse = false;
            while (!validResponse) {
                System.out.print("Do you want to make another transaction? (yes/no): ");
                response = sc.next().trim();
                if (response.equalsIgnoreCase("yes")) {
                    validResponse = true;
                } else if (response.equalsIgnoreCase("no")) {
                    System.out.println("-- Returning to Main Menu --");
                    return; 
                } else {
                    System.out.println("Invalid response. Please type 'yes' or 'no'.");
                }
            }

        } while (true);
    }

    private void addStock() {
        Scanner scanner = new Scanner(System.in);
        config conf = new config();
        Items i = new Items();
        i.viewItem();
        System.out.println("Adding a stock...");
        System.out.println("---------------------------");

        System.out.print("Enter ID of Item: ");
        int i_id = scanner.nextInt(); 
        
        String qry = "SELECT i_id FROM tbl_Items WHERE i_id = ?";
        while(conf.getSingleValue(qry, i_id) == 0){
            System.out.print("Item ID does not EXIST!.. Please try Again: ");
            i_id = scanner.nextInt(); 
        }
        
        System.out.print("Enter Quantity: ");
        String quantity = scanner.next();
        
        while (!quantity.matches("\\d+")) {
            System.out.print("Invalid quantity. Please enter a positive integer: ");
            quantity = scanner.next();
        }
        
        LocalDate currDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy: ");
        String date = currDate.format(formatter);
        
        String status = "SOLD";
        
        String stockqry = "INSERT INTO tbl_Stocks (i_id, quantity, date, status) VALUES (?, ?, ?, ?)";
        conf.addRecord(stockqry, i_id, quantity, date, status);
    }

    public void viewStock(){      
        config conf = new config();
        String qry = "SELECT tbl_Stocks.s_id, tbl_Items.Name, tbl_Stocks.quantity, tbl_Stocks.date, tbl_Stocks.status "
                   + "FROM tbl_Stocks "
                   + "INNER JOIN tbl_Items ON tbl_Items.i_id = tbl_Stocks.i_id";
        String[] headers = {"Stock ID", "Item Name", "Quantity", "Date Received", "Status"};
        String[] columns = {"s_id", "Name", "quantity", "date", "status"};

        System.out.println("\n--- Stock Report ---");
        conf.viewRecords(qry, headers, columns);
        
    }

    public void updateStock() {
        Scanner scanner = new Scanner(System.in);
        config conf = new config();
        System.out.println("------------------------------------");
        System.out.print("Enter Stock ID to update: ");
        int s_id = scanner.nextInt();

        String checkQuery = "SELECT quantity FROM tbl_Stocks WHERE s_id = ?";
        double currentQuantity = conf.getSingleValue(checkQuery, s_id);
        if (currentQuantity == 0) {
            System.out.println("Stock ID does not exist. Please try again.");
            return;
        }

        System.out.print("Do you want to Restock or Item Sold? (restock/sold): ");
        String updateType = scanner.next().trim().toLowerCase();

        int adjustmentQuantity = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Enter quantity to " + updateType + ": ");
                adjustmentQuantity = scanner.nextInt();
                if (adjustmentQuantity > 0) {
                    validInput = true;
                } else {
                    System.out.println("Quantity must be a positive number. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); 
            }
        }

        double newQuantity = currentQuantity;
        if (updateType.equals("restock")) {
            newQuantity += adjustmentQuantity;
        } else if (updateType.equals("sold")) {
            if (adjustmentQuantity > currentQuantity) {
                System.out.println("Error: Insufficient stock quantity. Operation cannot proceed.");
                return;
            }
            newQuantity -= adjustmentQuantity;
        } else {
            System.out.println("Invalid action. Please choose either 'restock' or 'sold item'.");
            return;
        }

        System.out.print("Enter new Status (Restock/Sold): ");
        String newStatus = scanner.next().trim();

        String updateQuery = "UPDATE tbl_Stocks SET quantity = ?, status = ? WHERE s_id = ?";
        conf.updateRecord(updateQuery, newQuantity, newStatus, s_id);

        System.out.println("-- Stock updated successfully --");
    }

    public void deleteStock() {
        Scanner scanner = new Scanner(System.in);
        config conf = new config();
        System.out.println("---------------------------------");
        System.out.print("Enter Stock ID to delete: ");
        int s_id = scanner.nextInt();

        String checkQuery = "SELECT s_id FROM tbl_Stocks WHERE s_id = ?";
        if (conf.getSingleValue(checkQuery, s_id) == 0) {
            System.out.println("Stock ID does not exist. Please try again.");
            return;
        }

        System.out.print("Are you sure you want to delete this stock? (yes/no): ");
        String confirm = scanner.next().trim();
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("-- Deletion canceled --");
            return;
        }

        String deleteQuery = "DELETE FROM tbl_Stocks WHERE s_id = ?";
        conf.deleteRecord(deleteQuery, s_id);

        System.out.println("-- Stock deleted successfully --");
    }
}
