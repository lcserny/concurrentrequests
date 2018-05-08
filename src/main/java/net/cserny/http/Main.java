package net.cserny.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        if (args.length != 2) {
            System.out.println("ERROR: provide thread count as arg1 and url as arg2");
            return;
        }

        int retries = Integer.valueOf(args[0]);
        String url = args[1];

        ExecutorService executorService = Executors.newFixedThreadPool(retries);

        for (int i = 0; i < retries; i++) {
            int index = i;
            executorService.submit(() -> {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");
                    System.out.println(String.format("Request #%d: %d", index, conn.getResponseCode()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Done!");

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
