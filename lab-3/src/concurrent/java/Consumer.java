import java.util.concurrent.Semaphore;

class Consumer {
    private final Buffer buffer;
    private final int sleepTime;
    private final int id;
    private Semaphore sConsumer;
    private Semaphore sProducer;
    
    public Consumer(int id, Buffer buffer, int sleepTime, Semaphore sConsumer,  Semaphore sProducer) {
        this.id = id;
        this.buffer = buffer;
        this.sleepTime = sleepTime;
        this.sProducer = sProducer;
        this.sConsumer = sConsumer;
    }
    
    public void process() throws InterruptedException {
        while (true) {

            try {
                sConsumer.acquire();
            
                int item = buffer.remove();
                if (item == -1) break;
                System.out.println("Consumer " + id + " consumed item " + item);
                Thread.sleep(sleepTime);

                sProducer.release();
               
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

           
        }
    }
}