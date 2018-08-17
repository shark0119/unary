package cn.com.unary.initcopy.filecopy.filepacker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants.PackType;
import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.filecopy.io.AbstractFileInput;
import cn.com.unary.initcopy.filecopy.io.AbstractFileOutput;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;

/**
 * 采用 rsync 算法实现的文件差异复制打包与解析
 * @author shark
 *
 */
@Component("rsyncFilePackAndResolve")
public class RsyncPacker implements SyncDiffPacker{

	@Autowired
	protected AbstractFileInput afi;
	@Autowired
	protected AbstractFileOutput afo;
	@Autowired
	@Qualifier("sqliteFileManager")
	protected FileManager fm;
	
	protected List<FileInfo> fis;
	
	@Override
	public void start(List<String> fileIds) {
		fis = fm.query(fileIds.toArray(new String[fileIds.size()]));
	}

	@Override
	public void setFileDiffInfos(List<DiffFileInfo> diffFileInfo) {
		
	}

	@Override
	public PackType getPackType() {
		return PackType.RSYNC_JAVA;
	}

}
