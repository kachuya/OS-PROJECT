import java.util.concurrent.TimeUnit;

public class Customer extends Restaurant implements Runnable {

    int id;
    String arrivalTime;
    String order;                            
    private final int orderPrepTime;

    public Customer(String customerInfo) {

        String[] parts = customerInfo.split(" ");
        this.id = Integer.parseInt(parts[0].split("=")[1]);
        this.arrivalTime = parts[1].split("=")[1];
        this.order = parts[2].split("=")[1];
        this.orderPrepTime = mealTimes.get(order);

    }

    public void run() {
        try {

            TimeUnit.MINUTES.sleep(Integer.parseInt(arrivalTime.split(":")[1]) - 8);

            table.acquire();

            System.out.printf("%n[%s] Customer %d arrives.%n", arrivalTime, id);
            System.out.printf("%n[%s] Customer %d is seated at Table %d %n ", arrivalTime, id, id);

            // Place the order
            synchronized (orderQueue) {
                orderQueue.add(new Order(id, order));
                System.out.printf("%n[%s] Customer %d places an order: %s.%n", arrivalTime, id, order);

            }
            
        } catch (NumberFormatException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

    }
}
