package cn.com.unary.initcopy.filecopy;

import cn.com.unary.initcopy.common.AbstractLoggable;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ServerFileCopyTest extends AbstractLoggable {
    @Test
    public void testCopyTask() throws InterruptedException {
        CopyTask task = new CopyTask();
        task.addPack(1);
        task.addPack(2);
        task.addPack(3);
        new Thread(task).start();
        Thread.sleep(1000);
        task.addPack(20);
        task.addPack(21);
        task.addPack(-1);
    }

    protected class CopyTask implements Runnable {
        private List<Integer> packs;
        private Integer index = 0;
        private Boolean pause = false;

        public CopyTask() {
            packs = new ArrayList<>();
        }

        /**
         * 根据解析器种类来选择不同的解析器。
         */
        @Override
        public void run() {
            Integer pack = null;
            while (true) {
                synchronized (index) {
                    if (index < packs.size()) {
                        pack = packs.get(index);
                        index++;
                    }
                }
                if (pack == null) {
                    try {
                        if (index >= packs.size()) {
                            logger.debug("Index: " + index + ", pack size: " + packs.size());
                        } else {
                            logger.error("Index: " + index + ", pack size: " + packs.size());
                            throw new IllegalStateException("Program error.");
                        }
                        synchronized (pause) {
                            pause.wait();
                        }
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                }
                if (resolve(pack)) {
                    if (index >= packs.size()) {
                        logger.debug("Index: " + index + ", pack size: " + packs.size());
                    } else {
                        logger.error("Index: " + index + ", pack size: " + packs.size());
                        throw new IllegalStateException("Program error.");
                    }
                    break;
                } else {
                    if (index >= packs.size()) {
                        try {
                            logger.debug("Index: " + index + ", pack size: " + packs.size());
                            synchronized (pause) {
                                pause.wait();
                            }
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }
            }
        }

        private boolean resolve(Integer i) {
            System.out.println(i);
            if (i.equals(-1))
                return true;
            return false;
        }

        public void addPack(Integer i) {
            synchronized (index) {
                this.packs.add(i);

                synchronized (pause) {
                    pause.notify();
                }
            }
        }
    }
}