package cn.com.unary.initcopy.common.utils;

/**
 * 差异复制时，源端和目标端路径转换
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class PathMapperUtil {

    private static final String SUFFIX = "/";
    private static final String UNARY_WIN_PREFIX = "win_driver_unary_";

    public enum OS {
        /**
         * Linux
         */
        LINUX,
        /**
         * Windows
         */
        WINDOWS,
    }

    /**
     * 将源端的文件路径映射为目标端实际备份的文件路径
     * 源端操作系统：
     * 以 filePath = /opt/dir/data.txt 为例
     * Linux: $destPath/opt/dir/data.txt
     * 以 filePath = "C:/dir/data.txt" 为例
     * Windows: $destPath/win_driver_unary_c/dir/data.txt
     *
     * @param destPath 目标端备份的文件目录
     * @param filePath 目标端备份的文件路径名
     * @return filePath 相对于 destPath 的源端文件路径名
     */
    public static String sourcePathMapper (String destPath, String filePath) {
        filePath = filePath.trim().replaceAll("\\\\", "/");
        destPath = destPath.trim().replaceAll("\\\\", "/");
        filePath = UNARY_WIN_PREFIX + filePath.toLowerCase().charAt(0) + filePath.substring(2);
        if (!destPath.endsWith(SUFFIX)) {
            destPath += SUFFIX;
        }
        return destPath + filePath;
    }

    /**
     * 将目标端备份的文件路径映射为源端的文件路径
     * 源端操作系统：
     * 以 filePath = $destPath/opt/dir/data.txt 为例
     * Linux: /opt/dir/data.txt
     * Windows: C:/opt/dir/data.txt
     *
     * 以 filePath = $destPath/win_driver_unary_d/dir/data.txt 为例
     * Linux: /win_driver_unary_d/dir/data.txt
     * Windows: D:/dir/data.txt
     *
     * @param destPath 目标端备份路径
     * @param filePath 目标端备份文件的全路径
     * @return 源端的文件路径
     */
    public static String destPathMapper (String destPath, String filePath, OS sourceOS) {
        // TODO use regex to achieve
        if (filePath.startsWith(destPath)) {
            filePath = filePath.substring(destPath.length());
            switch (sourceOS) {
                case LINUX:
                    break;
                case WINDOWS:
                    String driveLetter = "C:";
                    if (filePath.startsWith(UNARY_WIN_PREFIX)) {
                        driveLetter = filePath.charAt(19) + ":";
                    }
                    filePath = driveLetter + filePath;
                    break;
                default: break;
            }
            return filePath;
        } else {
            throw new IllegalStateException("Wrong path format with destPath :" + destPath + " and filePath: " + filePath);
        }
    }
}
