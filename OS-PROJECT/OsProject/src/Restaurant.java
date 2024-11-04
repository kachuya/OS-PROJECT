
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Restaurant {

    static int NC, NW, NF, NT;
    static Map<String, Integer> mealTimes = new HashMap<>(); // hashmap to store meal times , key = meal name(string)
                                                             // and int = time to prepare
    static List<Customer> customers = new ArrayList<>(); // list to store customer objects
    static Semaphore table;     // available tables
    static Semaphore chef;     // available chefs
    static Semaphore waiter;  // available waiters
    static Queue<Order> orderQueue = new ConcurrentLinkedQueue<>(); // thread-safe queue to store orders
    static List<Meal> mealsReady = Collections.synchronizedList(new ArrayList<>()); // thread-safe list to store cooked
                                                                                    // meals

    public static void main(String[] args) throws IOException {

        String Rfile = "/Users/shahenazabushanab/Downloads/restaurant_simulation_input1.txt";
        inputConfig ( Rfile );


        table = new Semaphore(NT, true);
        chef = new Semaphore(NF, true);
        waiter = new Semaphore(NW, true);

        for (Customer customer : customers) {
            new Thread(customer).start();
        }
        
    }


        private static void inputConfig (String Rfile ) throws IOException {

        // reading the first and second line of input file1
        // printing the chefs , waiters and tables

       

        BufferedReader br = new BufferedReader(new FileReader(Rfile));

        String line1 = br.readLine();
        String[] fp = line1.split(" "); // Split by space
        NC = Integer.parseInt(fp[0].split("=")[1]);
        NW = Integer.parseInt(fp[1].split("=")[1]);
        NF = Integer.parseInt(fp[2].split("=")[1]);

        

        String line2 = br.readLine();
        String[] sp = line2.split(" "); // Split by space
        NT = Integer.parseInt(sp[0].split("=")[1]);

        System.out.print("Simulation Started with " + NF + " Chefs, " + NW + " Waiters, and " + NT + " Tables.");

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
