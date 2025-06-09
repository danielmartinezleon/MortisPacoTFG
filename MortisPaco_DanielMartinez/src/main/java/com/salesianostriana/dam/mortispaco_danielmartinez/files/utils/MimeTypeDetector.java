package com.salesianostriana.dam.mortispaco_danielmartinez.files.utils;

import org.springframework.core.io.Resource;


public interface MimeTypeDetector {

    String getMimeType(Resource resource);

}