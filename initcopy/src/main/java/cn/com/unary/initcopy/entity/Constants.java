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

    public enum PackType {
        /**
         * 全复制- Java
         */
        SYNC_ALL_JAVA(0x01),
        /**
         * 差异复制- rsync
         */
        RSYNC_JAVA(0x02),
        ;
        private byte value;

        PackType(int b) {
            value = (byte) b;
        }

        public static PackType valueOf(byte b) {
            for (PackType type : PackType.values()) {
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

    public enum UpdateType {
        DELETE,
        PAUSE,
        RESUME,
    }
}
