package cn.com.unary.initcopy.filecopy.filepacker;

import api.UnaryTClient;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants.PackerType;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.filecopy.io.AbstractFileInput;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采用 rsync 算法实现的文件差异复制打包与解析
 *
 * @author shark
 */
@Component("RsyncPacker")
@Scope("prototype")
public class RsyncPacker implements SyncDiffPacker {

    private final Map<String, FileInfoDO> fiMap = new HashMap<>();
    private final Map<String, DiffFileInfo> dfiMap = new HashMap<>();
    private final List<String> readFileIds = new ArrayList<>();
    @Autowired
    @Qualifier("JavaNioFileInput")
    private AbstractFileInput afi;
    @Autowired
    private FileManager fm;
    private UnaryTClient unaryTClient;

    private boolean ready = false;

    @Override
    public void start(List<String> fileIds) throws InfoPersistenceException {
        ValidateUtils.requireNotEmpty(fileIds);
        if (ready) {
            for (FileInfoDO fi : fm.query(fileIds.toArray(new String[fileIds.size()]))) {
                fiMap.put(fi.getId(), fi);
            }
            if (fiMap.keySet().size() != dfiMap.keySet().size()) {
                throw new IllegalStateException("Insufficient Info for doing Rsync with "
                        + fiMap.keySet().size()
                        + " files but " + dfiMap.keySet().size() + " diff file infos");
            }
        }
    }

    @Override
    public void restore(int taskId) throws Exception {

    }

    @Override
    public SyncDiffPacker setFileDiffInfos(List<DiffFileInfo> diffFileInfo) {
        ValidateUtils.requireNotEmpty(diffFileInfo);
        for (DiffFileInfo dfi : diffFileInfo) {
            dfiMap.put(dfi.getFileId(), dfi);
        }
        ready = Boolean.TRUE;
        return this;
    }

    @Override
    public SyncDiffPacker setTaskId(int taskId) {
        return null;
    }

    @Override
    public PackerType getPackType() {
        return PackerType.RSYNC_JAVA;
    }

    @Override
    public void pause() {

    }

    @Override
    public SyncDiffPacker setTransfer(UnaryTClient unaryTClient) {
        this.unaryTClient = unaryTClient;
        return this;
    }

    @Override
    public void close() throws Exception {
        if (afi != null) {
            afi.close();
        }
    }
}
