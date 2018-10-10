package cn.com.unary.initcopy;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestTest {

    @Setter
    @Getter
    List<String> list = new ArrayList<>();

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

    @Test
    public void test1() {
        String pattern = "^(is|get)\\w+$";
        String getMethod = "getTrue";
        System.out.println(getMethod.matches(pattern));
    }

    @Test
    public void test2() throws NoSuchMethodException {
        Method method = TestTest.class.getMethod("getList");
        ParameterizedType type = (ParameterizedTypeImpl) method.getGenericReturnType();
        System.out.println(type);
        Type type1 = type.getActualTypeArguments()[0];
        System.out.println(type1);
        System.out.println(type1.getClass());
    }

    @Test
    public void test3() throws IOException {

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
