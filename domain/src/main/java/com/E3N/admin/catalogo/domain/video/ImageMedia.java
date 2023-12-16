package com.E3N.admin.catalogo.domain.video;

import com.E3N.admin.catalogo.domain.ValueObject;
import com.E3N.admin.catalogo.domain.utils.IdUtils;

import java.util.Objects;

public class ImageMedia extends ValueObject {
    private final String id;
    private final String checksum;
    private final String name;
    private final  String location;

    private ImageMedia(String id, String checksum, String name, String location) {
        this.id = id;
        this.checksum = checksum;
        this.name = name;
        this.location = location;
    }

    public static ImageMedia with(String checksum, String name, String location){
        return new ImageMedia(IdUtils.uuid(), checksum, name, location);
    }

    public static ImageMedia with(String id, String checksum, String name, String location){
        return  new ImageMedia(id, checksum, name, location);
    }

    public String id(){
        return id;
    }

    public String checksum(){
        return checksum;
    }

    public String name(){
        return name;
    }

    public String location(){
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ImageMedia that = (ImageMedia) o;
        return Objects.equals(checksum, that.checksum) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, location);
    }
}
