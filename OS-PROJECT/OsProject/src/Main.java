
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static int NC, NW, NF, NT, NOM, NCM; //customer line, waiters, chefs, tables, ordered meals, cooked meals
    static Map<String, Integer> mealTimes = new HashMap<>(); // hashmap to store meal times , key = meal name(string)
                                                             // and int = time to prepare
    static List<Customer> customers = new ArrayList<>(NC); // list to store customer objects
    //static Semaphore table;     // available tables
    //static Semaphore chef;     // available chefs
    //static Semaphore waiter;  // available waiters
    //static Queue<Order> orderQueue = new ConcurrentLinkedQueue<>(); // thread-safe queue to store orders
    //static List<Meal> mealsReady = Collections.synchronizedList(new ArrayList<>()); // thread-safe list to store cooked
                                                                                    // meals

    static BlockingQueue tables = new ArrayBlockingQueue<>(NT);
    static BlockingQueue orderedMeal = new ArrayBlockingQueue<>(NOM);
    static BlockingQueue cookedMeal = new ArrayBlockingQueue<>(NCM);

    static AtomicInteger custServed = new AtomicInteger(0); // to count number of customers served

    public static void main(String[] args) throws IOException {

        String Rfile = "C:\\Users\\lenah\\Desktop\\OS-PROJECT\\OS-PROJECT\\input files\\restaurant_simulation_input1.txt";
        inputConfig(Rfile);

       // for (int i = 0; i < NF; i++) {
       //    Chef chef = new Chef(i);
       //    Thread cthread = new Thread(chef);
       //   cthread.start();
       //}

        //for (int i = 0; i < NW; i++) {
        //    Waiter waiter = new Waiter(i);
        //    Thread wthread = new Thread(waiter);
        //   wthread.start();
        //}

        System.out.print("Simulation Started with " + NF + " Chefs, " + NW + " Waiters, and " + NT + " Tables.");

        //table = new Semaphore(NT, true);
        //chef = new Semaphore(NF, true);
        //waiter = new Semaphore(NW, true);

        for (Customer customer : customers) {
            new Thread(customer).start();
        }

    }

    private static void inputConfig(String Rfile) throws IOException {

        // reading the first and second line of input file1
        // printing the chefs , waiters and tables

        BufferedReader br = new BufferedReader(new FileReader(Rfile));

        String line1 = br.readLine();
        String[] fp = line1.split(" "); // Split by space
        NC = Integer.parseInt(fp[0].split("=")[1]); // customer line size
        NW = Integer.parseInt(fp[1].split("=")[1]);
        NF = Integer.parseInt(fp[2].split("=")[1]);

        String line2 = br.readLine();
        String[] sp = line2.split(" "); // Split by space
        NT = Integer.parseInt(sp[0].split("=")[1]); // table size
        NOM = Integer.parseInt(sp[1].split("=")[1]); // ordered meal size
        NCM = Integer.parseInt(sp[2].split("=")[1]); // cooked meal size

        //System.out.print("Simulation Started with " + NF + " Chefs, " + NW + " Waiters, and " + NT + " Tables.");

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
            customers.add(new Customer(remainingline));
        }
        br.close(); // Close the BufferedReader
    }

}

class Restaraunt {
    
}

class Customer implements Runnable {
    int custid;
    String arrivalTime;
    String order;                            
    private final int orderPrepTime;


    public Customer(int id, String meal) { 
        
        
        
   

        
    }

    @Override
    public void run() {

    }

    public void getSeated() { // produce -- customer is seated at a table
        
    }
}

class Chef implements Runnable {

    int chefid;

    public Chef(int i) {
        
    }

    @Override
    public void run() {

    }

    public void cookMeal() { // consume -- chef begins preparing a customer's ordered meal from orderedMeal

    }
    
    public void hands() { // produce -- produce a meal in cookedMeal

    }
}

class Waiter implements Runnable {

    int waiterid;

    public Waiter(int i) {
        
    }

    @Override
    public void run() {

    }

    public void removeSeat() { // consume -- waiter releases a table after customer has left

    }
    
    public void serveMeal() { // consume -- waiter serves a meal to customer from cookedMeal
        
    }
}

