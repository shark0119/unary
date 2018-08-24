package cn.com.unary.initcopy.utils;

import cn.com.unary.initcopy.entity.FileInfo;

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
    public static List<FileInfo> deSerFromResult (ResultSet rset) throws SQLException {
        List<FileInfo> fis = new ArrayList<>();
        FileInfo fi ;
        while (rset.next()) {
            fi = new FileInfo ();
            fi.setId(rset.getString("FILE_ID"));
            fi.setModifyTime(rset.getLong("MODIFY_TIME"));
            fi.setFileSize(rset.getLong("FILE_SIZE"));
            fi.setTaskId(rset.getInt("TASK_ID"));
            fis.add(fi);
        }
        return null;
    }
    public static String serToSql (FileInfo fi) {
        StringBuilder sb = new StringBuilder ("");

        return sb.toString();
    }
}
