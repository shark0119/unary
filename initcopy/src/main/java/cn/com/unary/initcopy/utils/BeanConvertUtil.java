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

import cn.com.unary.initcopy.entity.BaseFileInfoDO;
import cn.com.unary.initcopy.entity.Constants.FileType;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.grpc.entity.FileBaseInfo;

/**
 * 实体转换类，支持 POJO 与 GRPC 实体
 * @author Shark.Yin
 * @since 1.0
 */
public class BeanConvertUtil {

	private BeanConvertUtil() {}
	
	/**
	 * 将初始化时发送的文件信息转化为文件信息实体。FileInfo 包含了文件除内容以外所有的信息。
	 * 但本函数不初始化以下属性：
     *
	 * {@link FileInfoDO#beginPackIndex}
     * {@link FileInfoDO#finishPackIndex}
     *
	 * @param bfi 初始化时需要的文件基础信息
	 * @return 文件的待同步信息
	 * @throws IOException 由于读文件导致的 IO 异常
	 */
	public static FileInfoDO readFromBaseFileInfo (BaseFileInfoDO bfi) throws IOException {
        FileInfoDO fi = new FileInfoDO(bfi);
        Path path = Paths.get(bfi.getFullName());
        BasicFileAttributes bfa =
                Files
                        .getFileAttributeView(path, BasicFileAttributeView.class)
                        .readAttributes();
        if (bfa.isSymbolicLink()) {
            fi.setFileType(FileType.SYMBOLIC_LINK);
        } else if (bfa.isDirectory()) {
            fi.setFileType(FileType.DIR);
        }else if (bfa.isRegularFile()) {
            fi.setFileType(FileType.REGULAR_FILE);
        } else if (bfa.isOther()) {
            fi.setFileType(FileType.OTHER);
        }
		FileOwnerAttributeView ownerView = Files.getFileAttributeView(
				path, FileOwnerAttributeView.class);
		fi.setAttr(FileInfoDO.newFileAttr());
		fi.getAttr().setOwner(ownerView.getOwner().getName());
		fi.getAttr().setHidden(path.toFile().isHidden());
		return fi;
	}

	public static List<BaseFileInfoDO> takeFromGrpc (List<FileBaseInfo> fbis) {
	    List<BaseFileInfoDO> bfis = new ArrayList<>();
	    for(FileBaseInfo fbi : fbis) {
	        bfis.add(takeFromGrpc(fbi));
	    }
		return bfis;
	}
	public static List<FileBaseInfo> takeToGrpc (List<? extends BaseFileInfoDO> bfis) {
	    List<FileBaseInfo> fbis = new ArrayList<>();
	    for(BaseFileInfoDO bfi : bfis) {
	        fbis.add(takeToGrpc(bfi));
	    }
	    return fbis;
    }
    public static BaseFileInfoDO takeFromGrpc (FileBaseInfo fbi) {
        BaseFileInfoDO bfi = new BaseFileInfoDO();
        bfi.setFileSize(fbi.getFileSize());
        bfi.setFullName(fbi.getFullName());
        bfi.setModifyTime(fbi.getModifyTime());
		bfi.setTaskId(fbi.getTaskId());
		bfi.setId(fbi.getFileId());
	    return bfi;
    }

    public static FileBaseInfo takeToGrpc (BaseFileInfoDO bfi) {
        FileBaseInfo.Builder builder = FileBaseInfo.newBuilder();
        builder.setFileSize(bfi.getFileSize())
                .setFullName(bfi.getFullName())
                .setModifyTime(bfi.getModifyTime())
				.setTaskId(bfi.getTaskId())
				.setFileId(bfi.getId())
        ;
        return builder.build();
    }

}
