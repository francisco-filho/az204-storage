package com.capimgrosso.azure.az204;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class AppCmd {
    private static final String ENDPOINT = "https://az204cg001.blob.core.windows.net";
    private static final String CONTAINER = "az204";
    private static final String SAS = "sv=2022-11-02&ss=b&srt=sco&sp=rwdlaciytfx&se=2024-05-11T02:44:26Z&st=2023-12-23T18:44:26Z&spr=https&sig=eX4NMrZqwW%2Fu3sNFkFJKX%2F6flgCSf4zfN2dcNeNUgvc%3D";
    public static void main(String[] args) throws IOException {
        var repo = new FileBlobStorageRepository(ENDPOINT, CONTAINER, SAS);

        repo.listFiles().forEach(System.out::println);
        repo.uploadFile(Path.of("/home/francisco/.zshrc"));
        byte[] b = repo.downloadFile(".zshrc");
        Files.write(Path.of("/tmp/zshrc.sh"),
                b,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE);
    }
}
