package com.E3N.admin.catalogo.domain.video;

import com.E3N.admin.catalogo.domain.ValueObject;

public class Resource extends ValueObject {

    private final byte[] content;
    private final String checksum;
    private final String contentType;
    private final String name;

    private Resource(byte[] content, String checksum, String contentType, String name) {
        this.content = content;
        this.checksum = checksum;
        this.contentType = contentType;
        this.name = name;
    }

    public static Resource with(final byte[] content, final String checksum, final String contentType, final String name){
        return new Resource(content, checksum, contentType, name);
    }

    public byte[] content() {
        return content;
    }

    public String checksum() {
        return checksum;
    }

    public String contentType() {
        return contentType;
    }

    public String name() {
        return name;
    }
}
