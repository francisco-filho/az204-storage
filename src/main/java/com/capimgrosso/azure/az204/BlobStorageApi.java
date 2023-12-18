package com.capimgrosso.azure.az204;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Path("/storage")
public class BlobStorageApi {

    public class DownloadFormData {
        @RestForm
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        File file;
    }

    @GET
    public Response listBlobs(){
        return Response.accepted().build();
    }

    @POST
    @Path("/upload")
    public Response upload(@RestForm("f") FileUpload fileUpload){
        var name = fileUpload.fileName();
        return Response.ok(name).build();
    }

    @POST
    @Path("/upload2")
    public Response upload2(MultipartFormDataInput input){
        var map = input.getValues().get("f");
        for (FormValue f: map){
            try {
                int i = 0;
                var is = f.getFileItem().getInputStream();
                var bytes = new ByteArrayOutputStream();
                while ((i = is.read()) != -1){
                    bytes.write(i);
                }
                return Response.ok(bytes.toString(StandardCharsets.UTF_8)).build();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Response.accepted().build();
    }

    @POST
    @Path("/download/{name}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public DownloadFormData getFile(@PathParam("name") String filename){
        var data = new DownloadFormData();
        data.file =  new File("/home/f3445038/.bashrc");
        return data;
    }
}
