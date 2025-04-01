import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {

    private static int maxItens = 0;

    public static void main(String[] args) throws InterruptedException {
	    System.out.println("Lab9 ...");    

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10000);
    
        Thread producer = new Thread(() -> {
            try {
                produce(700, queue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        Thread consumer = new Thread(() -> {
            try {
                consume(100, queue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static void produce(int producingTime, BlockingQueue<Integer> queue) throws InterruptedException {
        while (maxItens < 10000) {
            int number = new Random().nextInt(10) + 1;
            Thread.sleep(producingTime);
            queue.put(number);
            maxItens++;
        }
    }

    public static void consume(int consumingTime, BlockingQueue<Integer> queue) throws InterruptedException {
        while (true) {
            Thread.sleep(consumingTime);
            Integer item = queue.poll(600, TimeUnit.MILLISECONDS);
            if (item == null) {
                break;
            }
            System.out.println(item);
        }
    }

}

