package com.salesianostriana.dam.mortispaco_danielmartinez.files.service.local;

import com.salesianostriana.dam.mortispaco_danielmartinez.files.model.AbstractFileMetadata;
import com.salesianostriana.dam.mortispaco_danielmartinez.files.model.FileMetadata;
import lombok.experimental.SuperBuilder;


@SuperBuilder
public class LocalFileMetadataImpl extends AbstractFileMetadata {

    public static FileMetadata of(String filename) {
        return LocalFileMetadataImpl.builder()
                .id(filename)
                .filename(filename)
                .build();
    }

}