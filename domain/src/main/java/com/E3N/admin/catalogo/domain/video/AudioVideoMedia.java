package com.E3N.admin.catalogo.domain.video;

import com.E3N.admin.catalogo.domain.ValueObject;
import com.E3N.admin.catalogo.domain.utils.IdUtils;

import java.util.Objects;

public class AudioVideoMedia extends ValueObject {

    private final String id;
    private final String checksum;
    private final String name;
    private final String rawLocation;
    private final String encodedLocation;
    private final MediaStatus status;

    public AudioVideoMedia(String id, String checksum, String name, String rawLocation, String encodedLocation, //
                           MediaStatus status) {
        this.id = Objects.requireNonNull(id);
        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.rawLocation = Objects.requireNonNull(rawLocation);
        this.encodedLocation = Objects.requireNonNull(encodedLocation);
        this.status = Objects.requireNonNull(status);
    }

    public static AudioVideoMedia with(String checksum, String name, String rawLocation){
        return new AudioVideoMedia(IdUtils.uuid(), checksum, name, rawLocation, "", MediaStatus.PENDING);
    }

    public static AudioVideoMedia with(String id, String checksum, String name, String rawLocation, String encodedLocation, //
                                       MediaStatus status){
        return new AudioVideoMedia(id,checksum, name, rawLocation, encodedLocation, status);
    }

    public String id() {
        return id;
    }

    public String checksum() {
        return checksum;
    }

    public String name() {
        return name;
    }

    public String rawLocation() {
        return rawLocation;
    }

    public String encodedLocation() {
        return encodedLocation;
    }

    public MediaStatus status() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AudioVideoMedia that = (AudioVideoMedia) o;
        return Objects.equals(checksum, that.checksum) && Objects.equals(rawLocation, that.rawLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, rawLocation);
    }

    public AudioVideoMedia processing(){
        return AudioVideoMedia.with(this.id, this.checksum, this.name, this.rawLocation, this.encodedLocation, MediaStatus.PROCESSING);
    }

    public AudioVideoMedia completed(final String encodedPath){
        return AudioVideoMedia.with(this.id, this.checksum, this.name, this.rawLocation, encodedPath, MediaStatus.COMPLETED);
    }

    public AudioVideoMedia pendingEncode(){
        return AudioVideoMedia.with(this.id, this.checksum, this.name, this.rawLocation, this.encodedLocation, MediaStatus.PENDING);
    }

    public boolean isPendingEncode(){
        return MediaStatus.PENDING == this.status;
    }
}
