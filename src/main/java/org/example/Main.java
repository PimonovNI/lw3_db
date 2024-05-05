package org.example;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        var url = "jdbc:mysql://localhost:3306/mydb";
        var username = "root";
        var password = "Password123";

        try (var connection =
                     DriverManager.getConnection(url, username, password);
             var statement =
                     connection.createStatement();
             var scanner = new Scanner(System.in)) {

            processing:
            while (true) {

                System.out.println("""
                        1. Create
                        2. Retrieve
                        3. Update
                        4. Delete
                        
                        5. Exit
                        """);

                var cmd = scanner.nextInt();

                switch (cmd) {

                    case 1 -> create(statement, scanner);
                    case 2 -> retrieve(statement);
                    case 3 -> update(statement, scanner);
                    case 4 -> delete(statement, scanner);

                    case 5 -> {
                        break processing;
                    }

                    default -> {
                        continue processing;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void create(Statement statement, Scanner scanner)
            throws SQLException {

        System.out.println("Enter name of customer:");
        var name = scanner.nextLine();

        System.out.println("Enter address of customer:");
        var address = scanner.nextLine();

        System.out.println("Enter phone of customer:");
        var phone = scanner.nextLine();

        System.out.println("Enter checking account of customer:");
        var checkingAccount = scanner.nextLine();

        statement.executeUpdate(String.format("""
                INSERT INTO customer(name_customer, address, phone, checking_account)
                VALUES (%s, %s, %s, %s);
                """, name, address, phone, checkingAccount));
    }

    public static void retrieve(Statement statement) throws SQLException {

        var result = statement.executeQuery(
                """
                        SELECT
                            id_customer,
                            name_customer,
                            address,
                            phone,
                            checking_account
                        FROM
                            customer;
                        """);

        while (result.next()) {

            var id = result.getLong("id_customer");
            var name = result.getString("name_customer");
            var address = result.getString("address");
            var phone = result.getString("phone");
            var checkingAccount = result.getString("checking_account");

            System.out.printf("%d. %s | %s | %s [%s]\n",
                    id, name, address, phone, checkingAccount);
        }
    }

    public static void update(Statement statement, Scanner scanner) throws SQLException {

        System.out.println("Enter id of customer:");
        var id = scanner.nextInt();

        System.out.println("Enter name of customer:");
        var name = scanner.nextLine();

        statement.executeUpdate(String.format("""
                UPDATE customer
                SET name_customer = %s
                WHERE id_customer = %d;
                """, name, id));
    }

    public static void delete(Statement statement, Scanner scanner) throws SQLException {

        System.out.println("Enter id of customer:");
        var id = scanner.nextInt();

        statement.executeUpdate(String.format("""
                DELETE FROM customer
                WHERE id_customer = %d;
                """, id));
    }
}
