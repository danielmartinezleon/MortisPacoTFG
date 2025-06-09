package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.validation;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Producto;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository.ProductoRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.UUID;

public class UniqueNombreValidator implements ConstraintValidator<UniqueNombre, String> {

    @Autowired
    private ProductoRepository repo;

    private String idField;

    @Override
    public void initialize(UniqueNombre constraintAnnotation) {
        this.idField = constraintAnnotation.idField();
    }

    @Override
    public boolean isValid(String nombre, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(nombre)) {
            return false;
        }

        // Obtengo el ID del producto desde la solicitud HTTP si estamos en edición
        UUID id = getProductIdFromRequest();

        Optional<Producto> existingProduct = repo.findByNombre(nombre);

        // Si no existe un producto con ese nombre, es válido
        if (existingProduct.isEmpty()) {
            return true;
        }

        // Si el producto encontrado es el mismo que se está editando, es válido
        return existingProduct.get().getId().equals(id);
    }

    private UUID getProductIdFromRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String pathVariable = request.getRequestURI().replaceAll(".*/editar/", "");
            try {
                return UUID.fromString(pathVariable);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }
}