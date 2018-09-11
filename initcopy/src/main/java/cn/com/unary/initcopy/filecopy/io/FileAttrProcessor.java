package cn.com.unary.initcopy.filecopy.io;

import cn.com.unary.initcopy.common.utils.CommonUtils;
import cn.com.unary.initcopy.common.utils.PathMapperUtil;
import cn.com.unary.initcopy.entity.FileInfoDO;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 将文件相关属性的元数据存储至目标端中
 * 将目标端文件相关属性的元数据读取至 {@link FileInfoDO}中
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("FileAttrProcessor")
public class FileAttrProcessor {

    /**
     * 将文件信息等元数据属性持久化至文件。
     * 属性文件放置在数据文件同级目录下。
     * 名称为 [源文件名].unary.fileAttr
     *
     * @param backupPath 在目标端的备份路径
     * @param fileInfo   文件相关的元数据信息
     * @throws IOException IO 异常
     */
    public void storeFileAttr(String backupPath, FileInfoDO fileInfo) throws IOException {
        String fileName = PathMapperUtil.sourcePathMapper(backupPath, fileInfo.getFullName());
        fileName += ".unary.fileAttr";
        File file = new File(fileName.substring(0, fileName.lastIndexOf("/")));
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("create dir fail");
            }
        }
        file = new File(fileName);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("create file fail");
            }
        }
        try (
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))
        ) {
            bos.write(CommonUtils.serToJson(fileInfo));
            bos.flush();
        }
    }

    public void restoreFileAttr(String attrFileName, FileInfoDO fileInfo) throws IOException {
        // TODO 将属性相关信息从文件中读取至指定数据结构中。
    }

}
