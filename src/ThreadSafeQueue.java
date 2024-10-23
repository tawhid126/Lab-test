import java.util.LinkedList;
import java.util.Queue;

public class ThreadSafeQueue<T> {
    private final Queue<T> queue = new LinkedList<>();

    public synchronized void enqueue(T data) {
        queue.add(data);
        notifyAll();
    }

    public synchronized T dequeue() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
class Runnerr{
    public static void main(String[] args) {
        ThreadSafeQueue<Integer> queue = new ThreadSafeQueue<>();

        Runnable enqueueTask = () -> {
            for (int i = 0; i < 1000; i++) {
                queue.enqueue(i);
            }
        };

        Runnable dequeueTask = () -> {
            for (int i = 0; i < 1000; i++) {
                queue.dequeue();
            }
        };

        Thread thread1 = new Thread(enqueueTask);
        Thread thread2 = new Thread(dequeueTask);


        thread1.start();
        thread2.start();


        try {
            thread1.join();
            thread2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Queue is empty: " + queue.isEmpty());
    }
}