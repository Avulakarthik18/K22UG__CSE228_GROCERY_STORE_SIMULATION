import java.util.*;

class Product {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity(int amount) {
        quantity -= amount;
    }
}

class Customer {
    private String name;
    private double budget;
    private List<String> shoppingList;

    public Customer(String name, double budget) {
        this.name = name;
        this.budget = budget;
        this.shoppingList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public double getBudget() {
        return budget;
    }

    public List<String> getShoppingList() {
        return shoppingList;
    }

    public void addToShoppingList(String productName) {
        shoppingList.add(productName);
    }

    public void deductBudget(double amount) {
        budget -= amount;
    }
}

public class GroceryStoreSimulation {
    private Map<String, Product> inventory;
    private Queue<Customer> customerQueue;

    public GroceryStoreSimulation() {
        inventory = new HashMap<>();
        customerQueue = new LinkedList<>();
    }

    public void addProductToInventory(String name, double price, int quantity) {
        Product product = new Product(name, price, quantity);
        inventory.put(name, product);
    }

    public void enqueueCustomer(Customer customer) {
        customerQueue.add(customer);
    }

    public void simulateShopping() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of customers: ");
        int numberOfCustomers = scanner.nextInt();

        for (int i = 1; i <= numberOfCustomers; i++) {
            System.out.println("Enter budget for Customer " + i + ": ");
            double budget = scanner.nextDouble();
            Customer customer = new Customer("Customer" + i, budget);
            System.out.println("Enter shopping list for Customer " + i + " (comma-separated): ");
            String shoppingListInput = scanner.next();
            String[] shoppingItems = shoppingListInput.split(",");

            for (String productName : shoppingItems) {
                System.out.println("Enter quantity of " + productName.trim() + " for Customer " + i + ": ");
                int quantity = scanner.nextInt();

                Product product = inventory.get(productName.trim());
                if (product != null && product.getQuantity() >= quantity && product.getPrice() * quantity <= customer.getBudget()) {
                    // Product is available in the required quantity and within budget
                    customer.deductBudget(product.getPrice() * quantity);
                    product.reduceQuantity(quantity);
                    System.out.println(customer.getName() + " added " + quantity + " " + productName.trim() + " to the cart.");
                } else {
                    System.out.println(customer.getName() + " couldn't buy " + productName.trim() + " in the required quantity or within budget.");
                }
            }

            enqueueCustomer(customer);
        }

        System.out.println("Simulation started...");

        while (!customerQueue.isEmpty()) {
            Customer customer = customerQueue.poll();
            System.out.println(customer.getName() + " is shopping.");

            for (String productName : customer.getShoppingList()) {
                Product product = inventory.get(productName);
                if (product != null && product.getQuantity() > 0 && product.getPrice() <= customer.getBudget()) {
                    // Product is available and within budget
                    customer.deductBudget(product.getPrice());
                    product.reduceQuantity(1);
                    System.out.println(customer.getName() + " added " + productName + " to the cart.");
                } else {
                    System.out.println(customer.getName() + " couldn't buy " + productName + ".");
                }
            }

            System.out.println(customer.getName() + " finished shopping.");
            System.out.println("Remaining budget for " + customer.getName() + ": " + customer.getBudget());
        }

        System.out.println("Simulation completed.");

        // Display remaining inventory
        System.out.println("\nRemaining Inventory:");
        for (String productName : inventory.keySet()) {
            Product product = inventory.get(productName);
            System.out.println(product.getName() + " - Price: $" + product.getPrice() + " | Quantity: " + product.getQuantity());
        }

        // Ask if the user wants to simulate again
        System.out.println("\nDo you want to simulate shopping again? (yes/no): ");
        String simulateAgain = scanner.next().toLowerCase();

        if (simulateAgain.equals("yes")) {
            simulateShopping();
        }

        scanner.close();
    }

    public static void main(String[] args) {
        GroceryStoreSimulation store = new GroceryStoreSimulation();
        Scanner scanner = new Scanner(System.in);

        // Interactive addition of products to the inventory
        System.out.println("Welcome to the Grocery Store Inventory Management System!");
        System.out.println("Please enter the initial inventory:");

        System.out.println("Enter the number of products in inventory: ");
        int numberOfProducts = scanner.nextInt();

        for (int i = 0; i < numberOfProducts; i++) {
            System.out.println("Enter product name: ");
            String productName = scanner.next();
            System.out.println("Enter price: ");
            double price = scanner.nextDouble();
            System.out.println("Enter quantity: ");
            int quantity = scanner.nextInt();

            store.addProductToInventory(productName, price, quantity);
        }

        // Display inventory menu
        System.out.println("\nInventory Menu:");
        for (String productName : store.inventory.keySet()) {
            Product product = store.inventory.get(productName);
            System.out.println(product.getName() + " - Price: Rs." + product.getPrice() + " | Quantity: " + product.getQuantity());
        }

        // Customer simulation
        System.out.println("\nWelcome customers!");
        System.out.println("Enter the number of customers: ");
        int numberOfCustomers = scanner.nextInt();
        // Start the simulation
        store.simulateShopping();
        scanner.close();
    }
}
