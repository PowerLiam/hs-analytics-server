package com.liamnbtech.server.service.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;

/**
 * Implementation of LocalFileService that tries to change file/directory permissions in order to obtain the desired
 * input and/or output streams.
 */
@Service
public class LocalFileServiceImpl implements LocalFileService {

    private final Logger LOG = LoggerFactory.getLogger(LocalFileServiceImpl.class);

    @Override
    public File getFile(String localFilePath) throws IOException {
        File localFile = new File(localFilePath);

        Files.setPosixFilePermissions(localFile.getAbsoluteFile().getParentFile().toPath(),
                EnumSet.of(PosixFilePermission.OWNER_READ,
                        PosixFilePermission.OWNER_WRITE,
                        PosixFilePermission.OWNER_EXECUTE,
                        PosixFilePermission.GROUP_READ,
                        PosixFilePermission.GROUP_WRITE,
                        PosixFilePermission.GROUP_EXECUTE));

        if (localFile.exists()) {
            Files.setPosixFilePermissions(localFile.getAbsoluteFile().toPath(),
                    EnumSet.of(PosixFilePermission.OWNER_READ,
                            PosixFilePermission.OWNER_WRITE,
                            PosixFilePermission.OWNER_EXECUTE,
                            PosixFilePermission.GROUP_READ,
                            PosixFilePermission.GROUP_WRITE,
                            PosixFilePermission.GROUP_EXECUTE));
        }

        return localFile;
    }

    @Override
    public InputStream getFileInputStream(String localFilePath) throws IOException {
        File localFile = getFile(localFilePath);

        return new BufferedInputStream(new FileInputStream(localFile));
    }

    @Override
    public OutputStream getFileOutputStream(String localFilePath, boolean append) throws IOException {
        File localFile = getFile(localFilePath);

        if (!localFile.exists()) {
            boolean success = localFile.createNewFile();
        } else if (!append){
            boolean deletionSuccess = localFile.delete();
            boolean recreationSuccess = localFile.createNewFile();
        }

        return new BufferedOutputStream(new FileOutputStream(localFile));
    }

    @Override
    public boolean deleteFile(String localFilePath) throws IOException {
        File localFile = getFile(localFilePath);

        boolean result = localFile.delete();

        LOG.info(String.format("Attempting to delete file %s | success %b", localFile.getAbsoluteFile().getPath(), result));

        return result;
    }

    @Override
    public boolean fileExists(String localFilePath) throws IOException {
        return getFile(localFilePath).exists();
    }
}
