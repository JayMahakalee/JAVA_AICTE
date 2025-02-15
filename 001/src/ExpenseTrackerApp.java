import java.io.*;
import java.time.LocalDate;
import java.util.*;

class Expense {
    private double amount;
    private String category;
    private String description;
    private LocalDate date;

    public Expense(double amount, String category, String description, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return date + "," + amount + "," + category + "," + description;
    }
}

class ExpenseManager {
    private List<Expense> expenses;
    private static final String FILE_NAME = "expenses.txt";

    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadExpenses();
    }

    public void addExpense(double amount, String category, String description) {
        Expense expense = new Expense(amount, category, description, LocalDate.now());
        expenses.add(expense);
        saveExpense(expense);
    }

    private void saveExpense(Expense expense) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(expense.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving expense: " + e.getMessage());
        }
    }

    private void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                expenses.add(new Expense(
                        Double.parseDouble(data[1]),
                        data[2],
                        data[3],
                        LocalDate.parse(data[0])
                ));
            }
        } catch (IOException e) {
            System.out.println("No previous expenses found. Starting fresh.");
        }
    }

    public double getTotalExpenses(LocalDate startDate, LocalDate endDate) {
        return expenses.stream()
                .filter(exp -> !exp.getDate().isBefore(startDate) && !exp.getDate().isAfter(endDate))
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}

public class ExpenseTrackerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();

        while (true) {
            System.out.println("1. Add Expense\n2. View Total Expenses\n3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();

                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();

                    manager.addExpense(amount, category, description);
                    System.out.println("Expense added successfully.");
                    break;
                case 2:
                    LocalDate today = LocalDate.now();
                    System.out.println("Total expenses for today: " + manager.getTotalExpenses(today, today));
                    System.out.println("Total expenses for this week: " + manager.getTotalExpenses(today.minusDays(7), today));
                    System.out.println("Total expenses for this month: " + manager.getTotalExpenses(today.withDayOfMonth(1), today));
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

