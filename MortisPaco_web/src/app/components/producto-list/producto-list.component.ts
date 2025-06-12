import { Component, OnInit } from '@angular/core';
import { ProductoListResponse, Producto } from '../../interfaces/producto/producto-list.interface';
import { ProductoService } from '../../services/producto.service';
import { ActivatedRoute } from '@angular/router';

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


  constructor(private productoService: ProductoService,
              private route: ActivatedRoute
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


}
