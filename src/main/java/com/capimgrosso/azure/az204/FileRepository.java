package com.capimgrosso.azure.az204;

import java.nio.file.Path;
import java.util.List;

public interface FileRepository {
    List<MyFile> listFiles();

    void uploadFile(Path file);

    byte[] downloadFile(String filename);
}
