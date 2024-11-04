import java.util.concurrent.*;

// Restaurant class to manage the restaurant logic
class Restaurant {
    private final Semaphore tables; // Manage table availability
    private final BlockingQueue<int[]> orderQueue; // Queue for orders (customerId, mealPreparationTime)
    private final BlockingQueue<String> readyMeals; // Queue for ready meals
    private final int N1;
    private final int N2;
    private final int N3;

    public Restaurant(int numTables) {
        this.tables = new Semaphore(numTables);
        this.orderQueue = new LinkedBlockingQueue<>();
        this.readyMeals = new LinkedBlockingQueue<>();
    }

    public void placeOrder(int customerId, String meal, int preparationTime) throws InterruptedException {
        orderQueue.put(new int[]{customerId, preparationTime}); // Place order with customerId and preparationTime
        System.out.println("[Order] Customer " + customerId + " placed an order: " + meal);
    }

    public int[] retrieveOrder() throws InterruptedException {
        return orderQueue.take(); // Wait for and retrieve the next order
    }

    public void mealReady(String meal) throws InterruptedException {
        readyMeals.put(meal); // Put the ready meal in the queue
        System.out.println("[Meal Ready] " + meal + " is ready.");
    }

    public String serveMeal() throws InterruptedException {
        return readyMeals.take(); // Wait until a meal is ready and serve it
    }

    public Semaphore getTables() {
        return tables;
    }
}

// Customer class to represent each customer
class Customer implements Runnable {
    private final int id;
    private final Restaurant restaurant;
    private final String meal;
    private final int preparationTime; // Time to prepare the meal

    public Customer(int id, Restaurant restaurant, String meal, int preparationTime) {
        this.id = id;
        this.restaurant = restaurant;
        this.meal = meal;
        this.preparationTime = preparationTime;
    }

    @Override
    public void run() {
        try {
            System.out.println("[Arrival] Customer " + id + " arrives.");
            restaurant.getTables().acquire(); // Wait for a table
            System.out.println("[Seating] Customer " + id + " is seated.");
            restaurant.placeOrder(id, meal, preparationTime); // Place order
            Thread.sleep(2000); // Simulate waiting for meal
            System.out.println("[Departure] Customer " + id + " finishes eating and leaves.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            restaurant.getTables().release(); // Release the table
            System.out.println("[Table Availability] Table is now available for other customers.");
        }
    }
}

// Chef class to represent each chef
class Chef implements Runnable {
    private final Restaurant restaurant;

    public Chef(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int[] order = restaurant.retrieveOrder(); // Retrieve an order
                int customerId = order[0];
                int preparationTime = order[1];
                System.out.println("[Preparation] Chef starts preparing meal for Customer " + customerId);
                Thread.sleep(preparationTime * 1000); // Simulate meal preparation time
                String meal = "Meal for Customer " + customerId; // Create a simple representation of the meal
                restaurant.mealReady(meal); // Notify that the meal is ready
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Waiter class to represent each waiter
class Waiter implements Runnable {
    private final Restaurant restaurant;

    public Waiter(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String meal = restaurant.serveMeal(); // Consume the next available meal
                System.out.println("[Serving] Waiter serves " + meal + ".");
                Thread.sleep(1000); // Simulate serving time
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Main class to run the restaurant simulation
class RestaurantSimulation {
    public static void main(String[] args) {
        // Configuration example
        int numTables = 4;
        Restaurant restaurant = new Restaurant(numTables);

        // Start Chefs
        int numChefs = 2;
        for (int i = 0; i < numChefs; i++) {
            new Thread(new Chef(restaurant)).start();
        }

        // Start Waiters
        int numWaiters = 3;
        for (int i = 0; i < numWaiters; i++) {
            new Thread(new Waiter(restaurant)).start();
        }

        // Sample customers with their orders (customerId, meal name, preparation time in seconds)
        new Thread(new Customer(1, restaurant, "Burger", 8)).start();
        new Thread(new Customer(2, restaurant, "Pizza", 10)).start();
        new Thread(new Customer(3, restaurant, "Pasta", 10)).start();
        new Thread(new Customer(4, restaurant, "Salad", 5)).start();
    }
}