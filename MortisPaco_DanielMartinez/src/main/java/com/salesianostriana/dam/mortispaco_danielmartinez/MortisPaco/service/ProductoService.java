package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.service;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.producto.CreateProductoCmd;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.producto.EditProductoCmd;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.ventas.GetVentaDto;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.*;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.query.ProductoSpecificationBuilder;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository.CategoriaRepository;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository.ProductoRepository;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository.UsuarioRepository;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository.VentaRepository;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.util.SearchCriteria;
import com.salesianostriana.dam.mortispaco_danielmartinez.files.model.FileMetadata;
import com.salesianostriana.dam.mortispaco_danielmartinez.files.service.StorageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final StorageService storageService;
    private final UsuarioRepository usuarioRepository;
    private final VentaRepository ventaRepository;

    public Producto findById(UUID id) {
        return productoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Producto createProducto(CreateProductoCmd cmd, MultipartFile file) {
        Categoria c = categoriaRepository.findById(cmd.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));
        System.out.println("producto creado con imagen: "+file);
        FileMetadata fileMetadata = storageService.store(file);

        String prefixedFilename = "/imports/" + fileMetadata.getFilename();

        Producto p = Producto.builder()
                .nombre(cmd.nombre())
                .descripcion(cmd.descripcion())
                .stock(cmd.stock())
                .precio(cmd.precio())
                .imagen(prefixedFilename)
                .categoria(c)
                .descuento(cmd.descuento())
                .build();

        return productoRepository.save(p);
    }

    public Page<Producto> findAll(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }



    @Transactional
    public Optional<Producto> editProducto(UUID id, EditProductoCmd cmd, MultipartFile file) {
        return productoRepository.findById(id).map(producto -> {
            Categoria categoria = categoriaRepository.findById(cmd.categoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));


            producto.setNombre(cmd.nombre());
            producto.setDescripcion(cmd.descripcion());
            producto.setStock(cmd.stock());
            producto.setPrecio(cmd.precio());
            producto.setDescuento(cmd.descuento());
            producto.setCategoria(categoria);

            // Si se envía una nueva imagen, la actualizamos
            if (file != null && !file.isEmpty()) {
                FileMetadata fileMetadata = storageService.store(file);
                String prefixedFilename = "/imports/" + fileMetadata.getFilename();
                producto.setImagen(prefixedFilename);
            }

            return productoRepository.save(producto);
        });
    }

    public void deleteProducto(UUID id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    public Page<Producto> search(Specification<Producto> spec, Pageable pageable) {
        return (spec != null) ? productoRepository.findAll(spec, pageable) : productoRepository.findAll(pageable);
    }

    @Transactional
    public GetVentaDto agregarProductoAlCarrito(Usuario usuarioAuth, UUID productoId, int cantidad) {
        System.out.println("usuarioAuth: "+usuarioAuth);
        Usuario usuario = usuarioRepository.findById(usuarioAuth.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Venta venta = usuario.getVentas().stream()
                .filter(Venta::isAbierta)
                .findFirst()
                .orElseGet(() -> {
                    Venta nuevaVenta = Venta.builder()
                            .cliente(usuario)
                            .fecha(LocalDate.now())
                            .abierta(true)
                            .importeTotal(0.0)
                            .gastosEnvio(0.0)
                            .lineas(new ArrayList<>())
                            .build();
                    usuario.getVentas().add(nuevaVenta);
                    return ventaRepository.save(nuevaVenta);
                });

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        Optional<LineaVenta> lineaExistente = venta.getLineas().stream()
                .filter(linea -> linea.getProducto().getId().equals(productoId))
                .findFirst();

        if (lineaExistente.isPresent()) {
            LineaVenta lineaVenta = lineaExistente.get();
            lineaVenta.setCantidad(lineaVenta.getCantidad() + cantidad);
        } else {
            LineaVenta nuevaLinea = new LineaVenta();
            nuevaLinea.setProducto(producto);
            nuevaLinea.setCantidad(cantidad);
            nuevaLinea.setVenta(venta);

            venta.getLineas().add(nuevaLinea);
        }

        double total = venta.getLineas().stream().mapToDouble(LineaVenta::getTotalLinea).sum();
        venta.setImporteTotal(total);

        ventaRepository.save(venta);

        return GetVentaDto.of(venta);
    }

    @Transactional
    public GetVentaDto obtenerCarrito(Usuario usuarioAuth) {
        Usuario usuario = usuarioRepository.findById(usuarioAuth.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Venta venta = ventaRepository.findByClienteAndAbiertaTrue(usuario);

        if (venta == null) {
            return GetVentaDto.of(new Venta());
        }

        return GetVentaDto.of(venta);
    }



    @Transactional
    public GetVentaDto eliminarProductoDelCarrito(Usuario usuarioAuth, UUID lineaVentaId) {
        Usuario usuario = usuarioRepository.findById(usuarioAuth.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Venta venta = usuario.getVentas().stream()
                .filter(Venta::isAbierta)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No hay venta abierta"));

        LineaVenta lineaVenta = venta.getLineas().stream()
                .filter(linea -> linea.getId().equals(lineaVentaId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("La línea de venta no está en el carrito"));

        venta.getLineas().remove(lineaVenta);

        venta.setImporteTotal(
                venta.getLineas().stream()
                        .mapToDouble(LineaVenta::getTotalLinea)
                        .sum()
        );

        ventaRepository.save(venta);

        return GetVentaDto.of(venta);
    }



    @Transactional
    public GetVentaDto cerrarVentaParaUsuario(Usuario usuarioAuth, UUID ventaId) {
        Usuario usuario = usuarioRepository.findById(usuarioAuth.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada"));

        if (!venta.getCliente().getId().equals(usuarioAuth.getId())) {
            throw new IllegalStateException("La venta no pertenece al usuario");
        }

        if (!venta.isAbierta()) {
            throw new IllegalStateException("La venta ya está cerrada");
        }

        venta.setAbierta(false);
        venta.setFecha(LocalDate.now());

        ventaRepository.save(venta);

        return GetVentaDto.of(venta);
    }






}
