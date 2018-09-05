package cn.com.unary.initcopy;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestTest {

    public static void main(String[] args) throws InterruptedException {
        Task task = new Task();
        new Thread(task).start();
        Thread.sleep(1000);
        task.lock.lock();
        System.out.println("main thread got lock, try notify task");
        task.ready.signal();
        task.lock.unlock();
        System.out.println("main thread done");
    }

    private static class Task implements Runnable {
        private ReentrantLock lock = new ReentrantLock();
        private Condition ready = lock.newCondition();

        @Override
        public void run() {
            lock.lock();
            try {
                System.out.println("im wait for signal and not unlock manually");
                ready.await();
                System.out.println("first statement after await");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println(Thread.currentThread().getName() + " wake from wait.");
        }
    }
}
