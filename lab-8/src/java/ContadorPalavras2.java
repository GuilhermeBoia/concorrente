import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ContadorPalavras2 {

    static int sum = 0;
    
        public static void main(String[] args) throws IOException, InterruptedException {
            System.out.println("Lab8");
            
            List<Thread> threads = new ArrayList<Thread>();
    
            for (String arg : args) {
                Thread thread = new Thread( () -> {
                    try {
                        int fileSum = contarPalavras(arg);
                        sum += fileSum;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println(sum);
    }
    
    static int contarPalavras(String nomeArquivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
        int count = 0;
        String linha;
        while ((linha = br.readLine()) != null) {
            count += linha.split("\\s+").length;
        }
        br.close();
        return count;
    }


}

