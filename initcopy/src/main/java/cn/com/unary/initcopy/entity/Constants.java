package cn.com.unary.initcopy.entity;

/**
 * 枚举集合地
 *
 * @author Shark.Yin
 * @since 1.0
 */
public final class Constants {

    private Constants() {
    }

    public enum FileType {
        /**
         * 普通文件
         */
        REGULAR_FILE,
        /**
         * 目录
         */
        DIR,
        /**
         * 链接
         */
        SYMBOLIC_LINK,
        /**
         * 其他未知类型
         */
        OTHER,
    }

    public enum PackerType {
        /**
         * 全复制- Java
         */
        SYNC_ALL_JAVA(0x01),
        /**
         * 差异复制- rsync
         */
        RSYNC_JAVA(0x02),
        ;
        private final byte value;

        PackerType(int b) {
            value = (byte) b;
        }

        public static PackerType valueOf(byte b) {
            for (PackerType type : PackerType.values()) {
                if (type.value == b) {
                    return type;
                }
            }
            return null;
        }

        public byte getValue() {
            return value;
        }
    }

    public enum THREAD_STATE {
        /**
         * 准备好了，可以接受数据
         */
        READY,
        /**
         * 正在运行中
         */
        RUNNING,
        /**
         * 挂起状态，等待数据
         */
        WAITING,
        /**
         * 被关闭但还在运行，不会继续接受数据
         */
        SHUTDOWN,
        /**
         * 线程结束
         */
        DEAD,
    }

}
