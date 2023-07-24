package ru.netology;

public class Request {
private String methodReq;
private String pathReq;
private String typeHTTP;

    public Request(String methodReq, String pathReq, String typeHTTP){
        this.methodReq = methodReq;
        this.pathReq = pathReq;
        this.typeHTTP = typeHTTP;
    }

    public String getMethodReq() {
        return methodReq;
    }

    public String getPathReq() {
        return pathReq;
    }

    public String getTypeHTTP() {
        return typeHTTP;
    }
}
