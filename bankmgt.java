import java.sql.*;
import java.util.Scanner;
import java.sql.*;
public class BankMgt {
 
 public static void main(String[] args) {
 Connection conn = null;
 Scanner scanner = new Scanner(System.in); // Create a single Scanner object
 try {
 Class.forName("org.apache.derby.jdbc.ClientDriver");
 conn = DriverManager.getConnection("jdbc:derby://localhost:1527/BankDB", "vcp", "vcp");
 int choice;
 do {
 System.out.println("MENU");
 System.out.println("1. Add new Account Holder information");
 System.out.println("2. Amount Deposit");
 System.out.println("3. Amount Withdrawal (Maintain minimum balance 500 Rs)");
 System.out.println("4. Display all information");
 System.out.println("5. Exit");
 System.out.print("Enter your choice: ");
 choice = scanner.nextInt();
 switch (choice) {
 case 1:
 addNewAccountHolder(conn, scanner);
 break;
 case 2:
 depositAmount(conn, scanner);
 break;
 case 3:
 withdrawAmount(conn, scanner);
 break;
 case 4:
 displayAllInformation(conn);
 break;
 case 5:
 System.out.println("Exiting...");
 break;
 default:
 System.out.println("Invalid choice. Please try again.");
 }
 } while (choice != 5);
 conn.close();
 scanner.close();
 } catch (SQLException se) {
 se.printStackTrace();
 } catch (Exception e) {
 e.printStackTrace();
 }
 }
 static void addNewAccountHolder(Connection conn, Scanner scanner) {
 try {
 System.out.print("Enter Account Number: ");
 int accNo = scanner.nextInt();
 scanner.nextLine(); // Consume newline character
 System.out.print("Enter Account Holder Name: ");
 String accHolderName = scanner.nextLine();
 System.out.print("Enter Address: ");
 String address = scanner.nextLine();
 System.out.print("Enter Initial Balance: ");
 double balance = scanner.nextDouble();
 String sql = "INSERT INTO bank (accno, acname, address, balance) VALUES (?, ?, ?, ?)";
 PreparedStatement preparedStatement = conn.prepareStatement(sql);
 preparedStatement.setInt(1, accNo);
 preparedStatement.setString(2, accHolderName);
 preparedStatement.setString(3, address);
 preparedStatement.setDouble(4, balance);
 int rowsInserted = preparedStatement.executeUpdate();
 if (rowsInserted > 0) {
 System.out.println("New account holder information added successfully!");
 } else {
 System.out.println("Failed to add new account holder information.");
 }
 } catch (SQLException se) {
 se.printStackTrace();
 }
 }
 static void depositAmount(Connection conn, Scanner scanner) {
 try {
 System.out.print("Enter Account Number: ");
 int accNo = scanner.nextInt();
 System.out.print("Enter Deposit Amount: ");
 double amount = scanner.nextDouble();
 String sql = "UPDATE bank SET balance = balance + ? WHERE accno = ?";
 PreparedStatement preparedStatement = conn.prepareStatement(sql);
 preparedStatement.setDouble(1, amount);
 preparedStatement.setInt(2, accNo);
 int rowsUpdated = preparedStatement.executeUpdate();
 if (rowsUpdated > 0) {
 System.out.println("Amount deposited successfully!");
 } else {
 System.out.println("No account found with the given Account Number.");
 }
 } catch (SQLException se) {
 se.printStackTrace();
 }
 }
 static void withdrawAmount(Connection conn, Scanner scanner) {
 try {
 System.out.print("Enter Account Number: ");
 int accNo = scanner.nextInt();
 System.out.print("Enter Withdrawal Amount: ");
 double amount = scanner.nextDouble();
 String checkBalanceSql = "SELECT balance FROM bank WHERE accno = ?";
 PreparedStatement checkBalanceStmt = conn.prepareStatement(checkBalanceSql);
 checkBalanceStmt.setInt(1, accNo);
 ResultSet resultSet = checkBalanceStmt.executeQuery();
 if (resultSet.next()) {
 double balance = resultSet.getDouble("balance");
 if (balance - amount >= 500) {
 String updateSql = "UPDATE bank SET balance = balance - ? WHERE accno = ?";
 PreparedStatement updateStmt = conn.prepareStatement(updateSql);
 updateStmt.setDouble(1, amount);
 updateStmt.setInt(2, accNo);
 int rowsUpdated = updateStmt.executeUpdate();
 if (rowsUpdated > 0) {
 System.out.println("Amount withdrawn successfully!");
 } else {
 System.out.println("Failed to withdraw amount.");
 }
 } else {
 System.out.println("Insufficient balance! Minimum balance of 500 Rs should be 
maintained.");
 }
 } else {
 System.out.println("No account found with the given Account Number.");
 }
 } catch (SQLException se) {
 se.printStackTrace();
 }
 }
 static void displayAllInformation(Connection conn) {
 try {
 Statement stmt = conn.createStatement();
 String sql = "SELECT * FROM bank";
 ResultSet resultSet = stmt.executeQuery(sql);
 while (resultSet.next()) {
 System.out.println("Account Number: " + resultSet.getInt("accno"));
 System.out.println("Account Name: " + resultSet.getString("acname"));
 System.out.println("Address: " + resultSet.getString("address"));
 System.out.println("Balance: " + resultSet.getDouble("balance"));
 System.out.println("-----------------------------");
 }
 } catch (SQLException se) {
 se.printStackTrace();
 }
 }
}
