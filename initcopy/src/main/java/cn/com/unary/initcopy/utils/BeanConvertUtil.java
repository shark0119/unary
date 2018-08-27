package cn.com.unary.initcopy.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.util.ArrayList;
import java.util.List;

import cn.com.unary.initcopy.entity.BaseFileInfo;
import cn.com.unary.initcopy.entity.Constants.FileType;
import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.grpc.entity.FileBaseInfo;

public class BeanConvertUtil {

	private BeanConvertUtil() {}
	
	/**
	 * 将初始化时发送的文件信息转化为文件信息实体。FileInfo 包含了文件除内容以外所有的信息。
	 * 但本函数不初始化以下属性：
	 * seqs
	 * @param bfi 初始化时需要的文件基础信息
	 * @return 文件的待同步信息
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

	public static List<BaseFileInfo> takeFromGrpc (List<FileBaseInfo> fbis) {
	    List<BaseFileInfo> bfis = new ArrayList<>();
	    for(FileBaseInfo fbi : fbis) {
	        bfis.add(takeFromGrpc(fbi));
	    }
		return bfis;
	}
	public static List<FileBaseInfo> takeToGrpc (List<BaseFileInfo> bfis) {
	    List<FileBaseInfo> fbis = new ArrayList<>();
	    for(BaseFileInfo bfi : bfis) {
	        fbis.add(takeToGrpc(bfi));
	    }
	    return fbis;
    }
    public static BaseFileInfo takeFromGrpc (FileBaseInfo fbi) {
        BaseFileInfo bfi = new BaseFileInfo();
        bfi.setFileSize(fbi.getFileSize());
        bfi.setFullName(fbi.getFullName());
        bfi.setModifyTime(fbi.getModifyTime());
        // TODO set file id and task id
	    return bfi;
    }

    public static FileBaseInfo takeToGrpc (BaseFileInfo bfi) {
        FileBaseInfo.Builder builder = FileBaseInfo.newBuilder();
        builder.setFileSize(bfi.getFileSize())
                .setFullName(bfi.getFullName())
                .setModifyTime(bfi.getModifyTime())
        // TODO set file id and task id
        ;
        return builder.build();
    }

}
