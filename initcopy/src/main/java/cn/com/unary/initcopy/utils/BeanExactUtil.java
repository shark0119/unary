package cn.com.unary.initcopy.utils;

import cn.com.unary.initcopy.entity.FileInfoDO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将实体和 SQL 互相转换
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class BeanExactUtil {
    public static List<FileInfoDO> deSerFromResult (ResultSet rset) throws SQLException {
        List<FileInfoDO> fis = new ArrayList<>();
        FileInfoDO fi ;
        while (rset.next()) {
            fi = new FileInfoDO();
            fi.setId(rset.getString("FILE_ID"));
            fi.setModifyTime(rset.getLong("MODIFY_TIME"));
            fi.setFileSize(rset.getLong("FILE_SIZE"));
            fi.setTaskId(rset.getInt("TASK_ID"));
            fis.add(fi);
        }
        return fis;
    }
    public static String serToSql (FileInfoDO fi) {
        StringBuilder sb = new StringBuilder ("");

        return sb.toString();
    }
}
