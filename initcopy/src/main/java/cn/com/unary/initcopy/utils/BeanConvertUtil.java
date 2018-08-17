package cn.com.unary.initcopy.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.com.unary.initcopy.entity.BaseFileInfo;
import cn.com.unary.initcopy.entity.Constants.FileType;
import cn.com.unary.initcopy.entity.FileInfo;

public class BeanConvertUtil {

	private BeanConvertUtil() {}
	
	/**
	 * 将初始化时发送的文件信息转化为文件信息实体。FileInfo 包含了文件除内容以外所有的信息。
	 * 但本函数不初始化以下属性：
	 * seqs
	 * @param bfi
	 * @return
	 * @throws IOException 由于读文件导致的 IO 异常
	 */
	public static FileInfo readFromBaseFileInfo (BaseFileInfo bfi) throws IOException {
		FileInfo fi = new FileInfo(bfi);
		Path path = Paths.get(bfi.getFullName());
		BasicFileAttributes bfa = 
				Files
				.getFileAttributeView(path, BasicFileAttributeView.class)
				.readAttributes();
		if (bfa.isSymbolicLink())
			fi.setFileType(FileType.SYMBOLIC_LINK);
		else if (bfa.isDirectory())
			fi.setFileType(FileType.DIR);
		else if (bfa.isRegularFile())
			fi.setFileType(FileType.REGULAR_FILE);
		else if (bfa.isOther())
			fi.setFileType(FileType.OTHER);
		
		FileOwnerAttributeView ownerView = Files.getFileAttributeView(
				path, FileOwnerAttributeView.class);
		fi.setAttr(fi.newFileAttr());
		fi.getAttr().setOwner(ownerView.getOwner().getName());
		fi.getAttr().setHidden(path.toFile().isHidden());
		return fi;
	}
	
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
