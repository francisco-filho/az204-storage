package com.capimgrosso.azure.az204;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class AppCmd {
    private static final String ENDPOINT = "https://az204cg001.blob.core.windows.net";
    private static final String CONTAINER = "az204";
    private static final String SAS = "sv=2022-11-02&ss=b&srt=sco&sp=rwdlaciytfx&se=2024-05-11T02:44:26Z&st=2023-12-23T18:44:26Z&spr=https&sig=eX4NMrZqwW%2Fu3sNFkFJKX%2F6flgCSf4zfN2dcNeNUgvc%3D";
    public static void main(String[] args) throws IOException {
        var app = new AppCmd();
        app.listBlobs().forEach(System.out::println);
        app.uploadBlob(Path.of("/home/francisco/.zshrc"));
        byte[] b = app.downloadBlob(".zshrc");
        Files.write(Path.of("/tmp/zshrc.sh"), b, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE_NEW);
    }

    public BlobContainerClient getContainer(){
        return new BlobContainerClientBuilder()
                .endpoint(ENDPOINT)
                .containerName(CONTAINER)
                .sasToken(SAS)
                .buildClient();
    }

    public List<MyBlob> listBlobs(){
        System.out.println("--- Listing my blobs ---");
        var container = getContainer();
        if (!container.exists()){
            container.create();
        }
        return container.listBlobs()
                .stream()
                .map(b -> new MyBlob(b.getName(), getBlobFullURL(b.getName())))
                .toList();
    }

    public void uploadBlob(Path file){
        System.out.println("--- Uploading the blob ---");
        BlobClient blobClient = getContainer()
                        .getBlobClient(file.getFileName().toString());

        if (blobClient.exists()){
            return;
        }
        try {
            blobClient.upload(new FileInputStream(file.toFile()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] downloadBlob(String name){
        System.out.println("--- Download the blob [" + name + "] ---");
        var bytes = new ByteArrayOutputStream();

        getContainer().getBlobClient(name)
                .getBlockBlobClient()
                .downloadStream(bytes);

        return bytes.toByteArray();
    }
    private String getBlobFullURL(String file){
        return "%s/%s/%s".formatted(ENDPOINT, CONTAINER, file);
    }

}
