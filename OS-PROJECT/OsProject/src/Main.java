
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static int NF, NW, NT; //customer line, waiters, chefs, tables, ordered meals, cooked meals
    static Map<String, Integer> mealTimes = new HashMap<>(); // hashmap to store meal times , key = meal name(string)
                                                             // and int = time to prepare
    static Queue<Customer> customerKiosk = new ConcurrentLinkedQueue<>(); // list to store customer objects

    static Semaphore customer; 
    static Semaphore table;    
    static Semaphore chef;     
    static Semaphore waiter;
    static int mutex;
    
    static Queue<String> orderQueue = new ConcurrentLinkedQueue<>(); // thread-safe queue to store orders
    static List<String> cookedMeal = Collections.synchronizedList(new ArrayList<>()); // thread-safe list to store cooked
    // meals

    static AtomicInteger custServed = new AtomicInteger(0); // to count number of customers served

    public static void main(String[] args) throws IOException {

        Random r = new Random(); // to simulate delays

        String Rfile = "C:\\Users\\lenah\\Desktop\\OS-PROJECT\\OS-PROJECT\\input files\\restaurant_simulation_input1.txt";
        inputConfig(Rfile);

        long startTime = System.currentTimeMillis();

        customer = new Semaphore(customerKiosk.size());
        table = new Semaphore(NT);
        chef = new Semaphore(NF);
        waiter = new Semaphore(NW);

        System.out.print("Simulation Started with " + NF + " Chefs, " + NW + " Waiters, and " + NT + " Tables.");        

    }

    private static void inputConfig(String Rfile) throws IOException {

        // reading the first and second line of input file1
        // printing the chefs , waiters and tables

        BufferedReader br = new BufferedReader(new FileReader(Rfile));

        String line1 = br.readLine();
        String[] fp = line1.split(" "); // Split by space
        NF = Integer.parseInt(fp[0].split("=")[1]); // customer line size
        NW = Integer.parseInt(fp[1].split("=")[1]);
        NT = Integer.parseInt(fp[2].split("=")[1]);

        String mealLine = br.readLine();
        String[] meals = mealLine.split(" ");
        for (String meal : meals) {
            String[] parts = meal.split("=");
            String mealName = parts[0];
            int prepTime = Integer.parseInt(parts[1].split(":")[1]);
            mealTimes.put(mealName, prepTime);
        }

        // reading the remaining lines to store customer info and adding it to customer
        // list

        String remainingline;

        while ((remainingline = br.readLine()) != null) {
            Customer c = new Customer();
            String[] parts = remainingline.split(" ");

            c.setId(Integer.parseInt(parts[0].split("=")[1]));
            c.setTime(parts[1].split("=")[1]);
            c.setOrder(parts[2].split("=")[1]);
            customerKiosk.add(c);
        }

        br.close(); // Close the BufferedReader
    }

}

class Restaraunt {
    
}

class Customer extends Main implements Runnable { 
    int custid;
    int arrivalTime;
    String order;                            
    int orderPrepTime;


    // public Customer() {

    // }
    
    public void setId(int i) {
        this.custid = i;
    }
    
    public void setTime(String t) {
        int t1 = Integer.parseInt(t.split(":")[1]) - 8;
        this.arrivalTime = t1;
    }
    
    public void setOrder(String o) {
        this.order = o;
        this.orderPrepTime = mealTimes.get(order);
    }

    @Override
    public void run() {
        try {
            
        }
        catch (InterruptedException ex) {

        }
        
        // int m = Integer.parseInt(arrivalTime.split(":")[1]) - 8;
        // TimeUnit.MINUTES.sleep(m);

        //     System.out.printf("%n[%s] Customer %d arrives.%n", arrivalTime, custid);
            

        //     // Place the order
        //     synchronized (orderQueue) {
        //         orderQueue.add(new Order(custid, order));
        //         System.out.printf("%n[%s] Customer %d places an order: %s.%n", arrivalTime, id, order);

        //         //wait for  order
        //         TimeUnit.MINUTES.sleep(orderPrepTime);
        //         System.out.printf("[%s] Customer %d finishes eating and leaves the restaurant.%n", arrivalTime, id);

                // Release the table
                // tables.remove();
    }
}

    // public void getSeated() { // produce -- customer is seated at a table
               
    // }

class Chef implements Runnable {

    int chefid;

    public Chef(int i) {
        this.chefid = i;
    }

    @Override
    public void run() {
        try {
            
        }
        catch (InterruptedException ex) {
            
        }
    }

    // public void cookMeal() { // consume -- chef begins preparing a customer's ordered meal from orderedMeal

    // }
    
    // public void hands() { // produce -- produce a meal in cookedMeal

    // }
}

class Waiter implements Runnable {

    int waiterid;

    public Waiter(int i) {

    }

    @Override
    public void run() {
        try {

        } catch (InterruptedException ex) {

        }
    }

    // public void removeSeat() { // consume -- waiter releases a table after customer has left

    // }

    // public void serveMeal() { // consume -- waiter serves a meal to customer from cookedMeal

    // }
}
