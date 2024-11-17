package stocktrackersystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Items {

    public void ITEMS() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("-------------------------------------------");
            System.out.println("\n== ITEMS MENU ==");
            System.out.println("1. Add Item");
            System.out.println("2. View Item Reports");
            System.out.println("3. Update Item");
            System.out.println("4. Delete Item");
            System.out.println("5. Exit Item Menu");
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
                    addItem();
                    break;
                case 2:
                    viewItem();
                    break;
                case 3:
                    viewItem();
                    updateItem();
                    viewItem();
                    break;
                case 4:
                    viewItem();
                    deleteItem();
                    viewItem();
                    break;
                case 5:
                    System.out.println("-- Exiting Item Menu --");
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

    public void addItem() {
        Scanner scanner = new Scanner(System.in);
        config conf = new config();
        System.out.println("Adding an item...");
        System.out.println("---------------------------");

        System.out.print("Item Name: ");
        String i_name = scanner.next().trim();
        while (i_name.isEmpty()) {
            System.out.print("Item Name cannot be empty. Please enter a valid name: ");
            i_name = scanner.next().trim();
        }

        System.out.print("Size: ");
        String i_size = scanner.next().trim();
        while (i_size.isEmpty()) {
            System.out.print("Size cannot be empty. Please enter a valid size: ");
            i_size = scanner.next().trim();
        }

        System.out.print("Color: ");
        String i_color = scanner.next().trim();
        while (i_color.isEmpty()) {
            System.out.print("Color cannot be empty. Please enter a valid color: ");
            i_color = scanner.next().trim();
        }

        System.out.print("Category: ");
        String i_category = scanner.next().trim();
        while (i_category.isEmpty()) {
            System.out.print("Category cannot be empty. Please enter a valid category: ");
            i_category = scanner.next().trim();
        }

        double i_price = -1;
        while (i_price < 0) {
            System.out.print("Price: ");
            try {
                i_price = scanner.nextDouble();
                if (i_price < 0) {
                    System.out.println("Price cannot be negative. Please enter a valid price.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.next();
            }
        }

        String qry = "INSERT INTO tbl_Items (Name, Size, Color, Category, Price) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(qry, i_name, i_size, i_color, i_category, i_price);

        System.out.println("-- Item added successfully --");
    }

    public void viewItem() {
        config conf = new config();
        String qry = "SELECT * FROM tbl_Items";
        String[] headers = {"i_id", "Name", "Size", "Color", "Category", "Price"};
        String[] columns = {"i_id", "Name", "Size", "Color", "Category", "Price"};

        System.out.println("\n--- Item Report ---");
        conf.viewRecords(qry, headers, columns);
    }

    public void updateItem() {
        Scanner scanner = new Scanner(System.in);
        config conf = new config();

        int i_id = -1;
        boolean validId = false;
        while (!validId) {
            System.out.print("Enter Item ID to Update: ");
            if (scanner.hasNextInt()) {
                i_id = scanner.nextInt();
                if (conf.getSingleValue("SELECT i_id FROM tbl_Items WHERE i_id = ?", i_id) > 0) {
                    validId = true;
                } else {
                    System.out.println("Selected ID doesn't exist. Please enter a valid ID.");
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric ID.");
                scanner.next();
            }
        }

        System.out.print("New Item Name: ");
        String i_name = scanner.next().trim();
        while (i_name.isEmpty()) {
            System.out.print("Item Name cannot be empty. Please enter a valid name: ");
            i_name = scanner.next().trim();
        }

        System.out.print("New Size: ");
        String i_size = scanner.next().trim();
        while (i_size.isEmpty()) {
            System.out.print("Size cannot be empty. Please enter a valid size: ");
            i_size = scanner.next().trim();
        }

        System.out.print("New Color: ");
        String i_color = scanner.next().trim();
        while (i_color.isEmpty()) {
            System.out.print("Color cannot be empty. Please enter a valid color: ");
            i_color = scanner.next().trim();
        }

        System.out.print("New Category: ");
        String i_category = scanner.next().trim();
        while (i_category.isEmpty()) {
            System.out.print("Category cannot be empty. Please enter a valid category: ");
            i_category = scanner.next().trim();
        }

        double i_price = -1;
        while (i_price < 0) {
            System.out.print("New Price: ");
            try {
                i_price = scanner.nextDouble();
                if (i_price < 0) {
                    System.out.println("Price cannot be negative. Please enter a valid price.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.next();
            }
        }

        String qry = "UPDATE tbl_Items SET Name = ?, Size = ?, Color = ?, Category = ?, Price = ? WHERE i_id = ?";
        conf.updateRecord(qry, i_name, i_size, i_color, i_category, i_price, i_id);

        System.out.println("-- Item updated successfully --");
    }

    public void deleteItem() {
        Scanner scanner = new Scanner(System.in);
        config conf = new config();

        int i_id = -1;
        boolean validId = false;
        while (!validId) {
            System.out.print("Enter the Item ID to delete: ");
            if (scanner.hasNextInt()) {
                i_id = scanner.nextInt();
                if (checkItemExists(String.valueOf(i_id), conf)) {
                    validId = true;
                } else {
                    System.out.println("Item ID not found in the database. Please enter a valid ID.");
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric ID.");
                scanner.next();
            }
        }

        System.out.print("Are you sure you want to delete this item? (yes/no): ");
        String confirm = scanner.next().trim();
        if (confirm.equalsIgnoreCase("yes")) {
            String qry = "DELETE FROM tbl_Items WHERE i_id = ?";
            conf.deleteRecord(qry, i_id);
            System.out.println("-- Item deleted successfully --");
        } else {
            System.out.println("-- Deletion canceled --");
        }
    }

    private boolean checkItemExists(String itemId, config conf) {
        String query = "SELECT 1 FROM tbl_Items WHERE i_id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, itemId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking item existence: " + e.getMessage());
        }
        return false;
    }
}
