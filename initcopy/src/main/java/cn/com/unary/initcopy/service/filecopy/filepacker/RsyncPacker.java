package cn.com.unary.initcopy.service.filecopy.filepacker;

import cn.com.unary.initcopy.common.utils.ValidateUtils;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants.PackerType;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.grpc.entity.SyncProcess;
import cn.com.unary.initcopy.service.filecopy.io.NioFileInput;
import cn.com.unary.initcopy.service.transmit.TransmitClientAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
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
@Component("RsyncPacker")
@Scope("prototype")
public class RsyncPacker implements SyncDiffPacker {

    private final Map<String, FileInfoDO> fiMap = new HashMap<>();

    @Autowired
    @Qualifier("clientFM")
    private FileManager fm;

    @Override
    public void init(String taskId, SyncProcess process) {

    }

    private final Map<String, DiffFileInfo> dfiMap = new HashMap<>();
    private final List<String> readFileIds = new ArrayList<>();
    @Autowired
    @Qualifier("NioFileInput")
    private NioFileInput afi;

    @Override
    public byte[] pack() {
        return new byte[0];
    }
    private TransmitClientAdapter transmitClient;

    private boolean ready = false;

    @Override
    public void init(String taskId) {
        if (ready) {
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
