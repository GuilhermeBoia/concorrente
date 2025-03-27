import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ContadorPalavras3 {

    static int sum = 0;
    static int wait = 0;
   
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Lab8");
        
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (String arg : args) {
            executorService.execute(new RunnableTask(arg));
        };

        while(wait != args.length) {}

        executorService.shutdown();

        System.out.println(sum);
    }

    static class RunnableTask implements Runnable {

        private String nomeArquivo;

        public RunnableTask (String nomeArquivo) {
            this.nomeArquivo = nomeArquivo;
        }

        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new FileReader(this.nomeArquivo))) {
                int count = 0;
                String linha;
                while ((linha = br.readLine()) != null) {
                    count += linha.split("\\s+").length;
                }
                br.close();
                sum += count;
                wait += 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    


}

