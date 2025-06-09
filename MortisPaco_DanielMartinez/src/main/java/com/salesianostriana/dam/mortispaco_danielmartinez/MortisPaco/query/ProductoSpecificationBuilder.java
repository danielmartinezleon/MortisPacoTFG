package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.query;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Producto;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.util.SearchCriteria;

import java.util.List;

public class ProductoSpecificationBuilder
    extends GenericSpecificationBuilder<Producto> {
    public ProductoSpecificationBuilder(List<SearchCriteria> params) {
        super(params);
    }
}
