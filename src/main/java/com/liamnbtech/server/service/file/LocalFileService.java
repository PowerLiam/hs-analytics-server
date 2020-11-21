package com.liamnbtech.server.service.file;

import java.io.*;

/**
 * Service for easily obtaining stream handles to read from and write to local files.
 */
public interface LocalFileService {

    File getFile(String localFilePath) throws IOException;

    /**
     * Gets an input stream that can be used to read from the file specified by the provided local file path.  The file
     * must exist
     *
     * @param localFilePath Path specifying a local file (on-box)

     *
     * @return  an input stream that can be used to read from the file specified by localFilePath
     *
     * @throws FileNotFoundException If the file specified by localFilePath does not exist, or is not readable
     */
    InputStream getFileInputStream(String localFilePath) throws IOException;

    /**
     * Gets an output stream that can be used to write to the file specified by the provided local file path.  If the
     * file specified by localFilePath does not exist, an empty file will be created at localFilePath.
     *
     * @param localFilePath Path specifying a local file (on-box)
     * @param append True if the input stream should append to the file (provided it exists), false if the existing file
     *        content (if any) should be discarded.
     *
     * @return  an output stream that can be used to write to the file specified by localFilePath
     *
     * @throws IOException If the file specified by localFilePath does not exist and could not be created, or is not
     * writeable
     */
    OutputStream getFileOutputStream(String localFilePath, boolean append) throws IOException;

    /**
     * Deletes the file specified by the local file path.  If the file is a directory than it must be empty.
     *
     * @param localFilePath The path to the file to be deleted.
     */
    boolean deleteFile(String localFilePath) throws IOException;

    boolean fileExists(String localFilePath) throws IOException;
}
