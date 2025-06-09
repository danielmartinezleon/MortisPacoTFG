package com.salesianostriana.dam.mortispaco_danielmartinez.files.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
public record FileResponse(
        String id,
        String name,
        String uri,
        String type,
        long size
)
{}