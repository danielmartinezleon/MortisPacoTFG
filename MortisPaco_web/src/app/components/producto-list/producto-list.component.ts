import { Component, OnInit } from '@angular/core';
import { ProductoListResponse, Producto } from '../../interfaces/producto/producto-list.interface';
import { ProductoService } from '../../services/producto.service';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { CarritoService } from '../../services/carrito.service';

@Component({
  selector: 'app-producto-list',
  templateUrl: './producto-list.component.html',
  styleUrl: './producto-list.component.css'
})
export class ProductoListComponent implements OnInit {

  productos!: ProductoListResponse;
  page: number = 0; // ¡Empieza en 0!
  loading: boolean = false;
  categoriaSeleccionada: string | null = null;
  userRole = localStorage.getItem('userRole');


  constructor(private productoService: ProductoService,
              private route: ActivatedRoute,
              private router: Router,
              private carritoService: CarritoService
  ) { }

  ngOnInit(): void {
  // Inicializamos con estructura vacía compatible con la interfaz
  this.productos = {
    content: [],
    pageable: {
      pageNumber: 0,
      pageSize: 0,
      sort: { sorted: false, empty: true, unsorted: true },
      offset: 0,
      paged: true,
      unpaged: false
    },
    last: false,
    totalElements: 0,
    totalPages: 0,
    first: true,
    size: 0,
    number: 0,
    sort: { sorted: false, empty: true, unsorted: true },
    numberOfElements: 0,
    empty: true
  };

  // Suscribirse a los cambios en los parámetros de ruta
  this.route.paramMap.subscribe(params => {
    const categoria = params.get('categoria');
    this.categoriaSeleccionada = categoria;
    this.page = 0;
    this.productos.content = [];
    this.cargarProductos();
  });
}


 cargarProductos(): void {
  if (this.loading || this.productos.last) return;
  this.loading = true;

  const observable = this.categoriaSeleccionada
    ? this.productoService.getProductosByCategoria(this.categoriaSeleccionada, this.page)
    : this.productoService.getProductos(this.page);

  observable.subscribe({
    next: (response) => {
      this.productos.content = [...this.productos.content, ...response.content];
      this.productos = { ...response, content: this.productos.content };
      this.page++;
      this.loading = false;
    },
    error: (error) => {
      console.error('Error al cargar productos:', error);
      this.loading = false;
    }
  });
}

filtrarPorCategoria(categoria: string): void {
  this.categoriaSeleccionada = categoria;
  this.page = 0;
  this.productos.content = []; // limpiamos los productos actuales
  this.cargarProductos();
}

irACrearProducto(): void {
  this.router.navigate(['/productos/crear']);
}

confirmarEliminacion(id: string): void {
  Swal.fire({
    title: '¿Estás seguro?',
    text: 'Esta acción eliminará el producto permanentemente.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Sí, eliminar',
    cancelButtonText: 'Cancelar'
  }).then((result) => {
    if (result.isConfirmed) {
      this.productoService.eliminarProducto(id).subscribe({
        next: () => {
          Swal.fire('Eliminado', 'El producto ha sido eliminado.', 'success');
          this.page = 0;
          this.productos.content = [];
          this.cargarProductos();
        },
        error: () => {
          Swal.fire('Error', 'No se pudo eliminar el producto.', 'error');
        }
      });
    }
  });
}

agregarAlCarrito(productoId: string): void {
  this.carritoService.agregarProducto(productoId, 1).subscribe({
    next: () => {
      Swal.fire('Añadido', 'El producto ha sido añadido al carrito.', 'success');
    },
    error: () => {
      Swal.fire('Error', 'No se pudo añadir el producto al carrito.', 'error');
    }
  });
}
}
