package com.capimgrosso.azure.az204;

public class MyBlob {
    private String name;
    private String url;

    public MyBlob(){}

    public MyBlob(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MyBlob{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
