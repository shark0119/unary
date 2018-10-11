package cn.com.unary.initcopy.filecopy.filepacker;

import api.UnaryTransferClient;
import cn.com.unary.initcopy.common.utils.ValidateUtils;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants.PackerType;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.filecopy.io.AbstractFileInput;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private UnaryTransferClient unaryTransferClient;

    private boolean ready = false;

    @Override
    public void start(String taskId, UnaryTransferClient transfer) throws InfoPersistenceException {
        Objects.requireNonNull(transfer);
        if (ready) {
            this.unaryTransferClient = transfer;
            for (FileInfoDO fi : fm.queryByTaskId(taskId)) {
                fiMap.put(fi.getFileId(), fi);
            }
            if (fiMap.keySet().size() != dfiMap.keySet().size()) {
                throw new IllegalStateException("Insufficient Info for doing Rsync with "
                        + fiMap.keySet().size()
                        + " files but " + dfiMap.keySet().size() + " diff file infos");
            }
        }
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
    public PackerType getPackType() {
        return PackerType.RSYNC_JAVA;
    }

    @Override
    public void close() throws IOException {
        if (afi != null) {
            afi.close();
        }
    }
}
