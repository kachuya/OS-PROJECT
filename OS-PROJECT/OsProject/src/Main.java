/* ---CURRENT CONCERNS---:

chef run method unfilled. waiter run method unfilled. how to make use of both mealTimes and orderedMeal?
parsing arrival time in minutes and seconds or just minutes?
how to add the minutes passed inbetween threads sleeping to overall time?
how can all three classes -- chefs, waiters, customers --
have correctly assumed how much time has passed from the customer's arrival time
when it is an attribute unique to the customer class?

*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    static int NF, NW, NT; //customer line, waiters, chefs, tables, ordered meals, cooked meals
    static Map<String, Integer> mealTimes = new HashMap<>();

    static PriorityBlockingQueue<Order> orderedMeal = new PriorityBlockingQueue<>();
    static Semaphore tables;
    static Semaphore ordersReady;
    static Semaphore mealsReady;

    private static final ConcurrentHashMap<Integer, Semaphore> customerSemaphores = new ConcurrentHashMap<>();
    static Queue<Customer> customerKiosk = new LinkedList<Customer>();;

    static class Customer implements Runnable {
        int custid;
        int arrivalTime;
        String meal;
        int orderPrepTime;

        public Customer() {

        }

        public void setId(int i) {
            this.custid = i;
        }

        public void setTime(String t) {
            int t1 = Integer.parseInt(t.split(":")[1]);
            this.arrivalTime = t1;
        }

        public void setOrder(String o) {
            this.meal = o;
            this.orderPrepTime = mealTimes.get(meal);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("[" + arrivalTime + "]" + " Customer" + custid + "arrives at the restaraunt");
                    tables.acquire();
                    System.out.println("[" + arrivalTime + "]" + " Customer" + custid + "is seated at table "
                            + (NT - tables.availablePermits()));

                    Semaphore custSemaphore = new Semaphore(0);
                    customerSemaphores.put(custid, custSemaphore);
                    
                    System.out.println("Customer " + custid + "places an order: " + meal);
                    Order order = new Order(custid, meal);
                    orderedMeal.put(order);
                    ordersReady.release();

                    custSemaphore.acquire();
                    System.out.println("Customer " + custid + " finishes eating and leaves the restaraunt");

                    tables.release();    
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

            }
        }
    }

    static class Chef implements Runnable {
        @Override
        public void run() {
            // try {
            //     while (true) {

            //     }
            // } catch (InterruptedException ex) {
            //     Thread.currentThread().interrupt();
            // }
        }
    }

    static class Waiter implements Runnable {
        @Override
        public void run() {
            while (true) {

            }
        }
    }

    static class Order implements Comparable<Order> {
        int custid;
        String mealname;
        long timestamp;

        Order(int custid, String mealname) {
            this.custid = custid;
            this.mealname = mealname;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public int compareTo(Order o) {
            return Long.compare(this.timestamp, o.timestamp);
        }
    }

    public static void main(String[] args) throws IOException {     

        Random r = new Random(); // to simulate delays

        String Rfile = "C:\\Users\\lenah\\Desktop\\OS-PROJECT\\OS-PROJECT\\input files\\restaurant_simulation_input1.txt";
        inputConfig(Rfile);

        tables = new Semaphore(NT);
        ordersReady = new Semaphore(0);
        mealsReady = new Semaphore(0);

        long startTime = System.currentTimeMillis();
        System.out.print("Simulation Started with " + NF + " Chefs, " + NW + " Waiters, and " + NT + " Tables.");

        for (int i = 0; i < NF; i++) {
            Thread chef = new Thread(new Chef());
            chef.start();
        }

        for (int i = 0; i < NW; i++) {
            Thread waiter = new Thread(new Waiter());
            waiter.start();
        }

        for (Customer c : customerKiosk) {
            Thread customer = new Thread(c);
            customer.start();

            try {
                Thread.sleep(8000);
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("simulation ended at " + (endTime - startTime));

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
