package cn.com.unary.initcopy;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
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
    public void test3() {
        Charset charset = InitCopyContext.CHARSET;
        String uuid = UUID.randomUUID().toString();
        int size = uuid.getBytes(charset).length;
        int tempSize;
        String tempUuid;
        System.out.println(String.format("%s len:%d", uuid, uuid.length()));
        System.out.println(String.format("%s size:%d", charset.toString(), size));
        charset = StandardCharsets.UTF_16;
        size = uuid.getBytes(charset).length;
        System.out.println(String.format("%s size:%d", charset.toString(), size));
        charset = StandardCharsets.ISO_8859_1;
        size = uuid.getBytes(charset).length;
        System.out.println(String.format("%s size:%d", charset.toString(), size));
        for (int i = 0; i < 1000; i++) {
            tempUuid = UUID.randomUUID().toString();
            tempSize = UUID.randomUUID().toString().getBytes(InitCopyContext.CHARSET).length;
            if (tempSize != size) {
                System.out.println(String.format("%s len:%d", uuid, size));
                System.out.println(String.format("%s len:%d", tempUuid, tempSize));
            }
        }
    }

    @Test
    public void test4() {
        BigDecimal bi = new BigDecimal("1000");
        BigDecimal bi1 = new BigDecimal("832");
        System.out.println(bi1.divide(bi, 2, RoundingMode.CEILING));
    }

    @Test
    public void test5() {
        B b = new B();
        b.test();
    }

    @Test
    public void test6() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        list.add(5);
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer i = iterator.next();
            if (i == 3) {
                iterator.remove();
            } else {
                System.out.println(i);
            }
        }
    }

    static class A {
        int a = 1;

        A() {
            print(this);
        }

        void print(A a) {
            System.out.println(a.a);
            this.overrideMtd();
        }

        void overrideMtd() {
            System.out.println("overrideMtd");
        }
    }

    static class B extends A {
        int a = 2;

        void test() {
            print(this);
            System.out.println(a);
        }

        @Override
        void overrideMtd() {
            System.out.println("overrideMtd in subclass");
        }
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
