package com.qiao.assetmanagement.common.exception;

public class ResourceNotFoundException extends RuntimeException{

public ResourceNotFoundException() {
    }
    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " " + id + " not found");
    }
}
