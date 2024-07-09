import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 1000;
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = countR(route);
                updateSizeToFreq(countR);
                latch.countDown();
            }).start();
        }

        latch.await();

        printSizeToFreq();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countR(String route) {
        int count = 0;
        for (char c : route.toCharArray()) {
            if (c == 'R') {
                count++;
            }
        }
        return count;
    }

    public static void updateSizeToFreq(int count) {
        synchronized (sizeToFreq) {
            sizeToFreq.put(count, sizeToFreq.getOrDefault(count, 0) + 1);
        }
    }

    public static void printSizeToFreq() {
        Map<Integer, Integer> freqMap = new HashMap<>(sizeToFreq);
        freqMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach((entry) -> {
                    if (entry.getValue() > 1) {
                        System.out.println("Частота " + entry.getKey() + " (встретилось " + entry.getValue() + " раз)");
                    }
                });
    }
}