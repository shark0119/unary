package cn.com.unary.initcopy.filecopy.filepacker;

import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants.PackType;
import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.filecopy.io.AbstractFileInput;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采用 rsync 算法实现的文件差异复制打包与解析
 *
 * @author shark
 */
@Component("rsyncPacker")
public class RsyncPacker implements SyncDiffPacker {

    private final Map<String, FileInfo> fiMap = new HashMap<>();
    private final Map<String, DiffFileInfo> dfiMap = new HashMap<>();
    private final List<String> readFileIds = new ArrayList<>();
    @Autowired
    protected AbstractFileInput afi;
    @Autowired
    @Qualifier("sqliteFileManager")
    protected FileManager fm;
    private volatile boolean ready = false;

    @Override
    public void start(List<String> fileIds) {
        ValidateUtils.requireNotEmpty(fileIds);
        if (ready) {
            for (FileInfo fi : fm.query(fileIds.toArray(new String[fileIds.size()]))) {
                fiMap.put(fi.getId(), fi);
            }
            if (fiMap.keySet().size() != dfiMap.keySet().size()) {
                throw new IllegalStateException("Insufficient Info for doing Rsync with "
                        + fiMap.keySet().size()
                        + " files but " + dfiMap.keySet().size() + " diff file infos");
            }
            return;
        }
    }

    @Override
    public void setFileDiffInfos(List<DiffFileInfo> diffFileInfo) {
        ValidateUtils.requireNotEmpty(diffFileInfo);
        for (DiffFileInfo dfi : diffFileInfo) {
            dfiMap.put(dfi.getFileId(), dfi);
        }
        ready = Boolean.TRUE;
    }

    @Override
    public PackType getPackType() {
        return PackType.RSYNC_JAVA;
    }

    @Override
    public void close() throws IOException {
        if (afi != null) {
            afi.close();
        }
    }
}
