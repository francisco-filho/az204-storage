package com.capimgrosso.azure.az204;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

public class FileBlobStorageRepository implements FileRepository {
    private String endpoint;
    private String sas;
    private String container;
    private BlobContainerClient containerClient;

    public FileBlobStorageRepository(String endpoint, String container, String sas) {
        this.endpoint = endpoint;
        this.sas = sas;
        this.container = container;
        this.containerClient = new BlobContainerClientBuilder()
                .endpoint(endpoint)
                .containerName(container)
                .sasToken(sas)
                .buildClient();
    }

    public BlobContainerClient getContainer(){
        return containerClient;
    }

    @Override
    public List<MyFile> listFiles(){
        System.out.println("--- Listing my blobs ---");
        var container = getContainer();
        if (!container.exists()){
            container.create();
        }
        return container.listBlobs()
                .stream()
                .map(b -> new MyFile(b.getName(), getBlobFullURL(b.getName())))
                .toList();
    }

    @Override
    public void uploadFile(Path file){
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

    @Override
    public byte[] downloadFile(String filename){
        System.out.println("--- Download the blob [" + filename + "] ---");
        var bytes = new ByteArrayOutputStream();

        getContainer().getBlobClient(filename)
                .getBlockBlobClient()
                .downloadStream(bytes);

        return bytes.toByteArray();
    }
    private String getBlobFullURL(String file){
        return "%s/%s/%s".formatted(endpoint, container, file);
    }

}
