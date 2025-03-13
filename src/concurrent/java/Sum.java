import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Sum {

        private static long total = 0;
        private static Semaphore mutex = new Semaphore(1);
        private static Semaphore multiplex;
        private static Map<Long,List<String>> sumMap = new HashMap<>();
    
        public static void incrementTotal (int value) throws InterruptedException {
            mutex.acquire();
            total += value;
            mutex.release();
        }
    
        public static int sum(FileInputStream fis) throws IOException, InterruptedException {

            multiplex.acquire();
                
            int byteRead;
            int sum = 0;
                
            while ((byteRead = fis.read()) != -1) {
                sum += byteRead;
            }
            
            try {
                incrementTotal(sum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            multiplex.release();

            return sum;
        }
        
        public static long sum(String path) throws IOException, InterruptedException {
        
            Path filePath = Paths.get(path);
            if (Files.isRegularFile(filePath)) {
                FileInputStream fis = new FileInputStream(filePath.toString());
                return sum(fis);
            } else {
                throw new RuntimeException("Non-regular file: " + path);
            }
        }
        
        public static void main(String[] args) throws Exception {
    
            if (args.length < 1) {
                System.err.println("Usage: java Sum filepath1 filepath2 filepathN");
                System.exit(1);
            }
            
            List<Thread> threads = new ArrayList<Thread>();
    
            int control = args.length / 2;
            if (control == 0) control = 1;
            multiplex = new Semaphore(control);

            System.out.println("--------- PRIMEIRA ETAPA ---------");

            //many exceptions could be thrown here. we don't care
            for (String path : args) {
                Thread thread = new Thread( () -> {
                    long sum = 0;
                    try {
                        sum = sum(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(path + " : " + sum);

                    List<String> lista;
                    if (sumMap.containsKey(sum)) {
                        lista = sumMap.get(sum);
                    } else {
                        lista = new ArrayList<>();
                    }

                    lista.add(path);
                    sumMap.put(sum, lista);
                });
            
                threads.add(thread);
                thread.start();
            }   

            for (Thread thread : threads) {
                thread.join();
            }
        
            System.out.println("Soma total: " + total);

            System.out.println("\n--------- TERCEIRA ETAPA ---------\n");

            for (Long soma : sumMap.keySet()) {
                
                if(sumMap.get(soma).size() > 1) {
                    String output = "";
                    output += soma + " ";

                    for (String file : sumMap.get(soma)) {
                        output += file + " ";
                    }

                    System.out.println(output);
                }
            }
    }
}
