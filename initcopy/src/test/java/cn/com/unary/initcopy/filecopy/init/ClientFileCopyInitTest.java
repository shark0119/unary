package cn.com.unary.initcopy.filecopy.init;

import cn.com.unary.initcopy.entity.BaseFileInfoDO;
import cn.com.unary.initcopy.entity.FileInfoDO;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientFileCopyInitTest {

    @Test
    public void test () throws IOException {
        List<String> files = new ArrayList<>();
        files.add("G:\\test\\vir");
        for (FileInfoDO infoDO : traversingFiles(files)) {
            System.out.println(infoDO);
        }
    }

    private List<FileInfoDO> traversingFiles(List<String> files) throws IOException {
        Path path;
        final List<FileInfoDO> fileInfos = new ArrayList<>();
        for (String fileName : files) {
            path = Paths.get(fileName);
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (dir.toFile().list().length == 0) {
                        FileInfoDO fileInfo = new FileInfoDO(takeFromFile(dir.toFile()));
                        fileInfos.add(fileInfo);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    FileInfoDO fileInfo = new FileInfoDO(takeFromFile(file.toFile()));
                    fileInfos.add(fileInfo);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        return fileInfos;
    }

    private BaseFileInfoDO takeFromFile(File file) {
        BaseFileInfoDO bfi = new BaseFileInfoDO();
        bfi.setFileSize(file.length());
        bfi.setFileId(UUID.randomUUID().toString());
        bfi.setModifyTime(file.lastModified());
        bfi.setFullName(file.getPath());
        return bfi;
    }

}