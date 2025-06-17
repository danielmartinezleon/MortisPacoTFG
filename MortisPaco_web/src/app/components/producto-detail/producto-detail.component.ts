import { Component, OnInit } from '@angular/core';
import { ProductoService } from '../../services/producto.service';
import { ActivatedRoute } from '@angular/router';
import { ProductoDetailResponse } from '../../interfaces/producto/producto-detail.interface';
import { CarritoService } from '../../services/carrito.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-producto-detail',
  templateUrl: './producto-detail.component.html',
  styleUrl: './producto-detail.component.css'
})
export class ProductoDetailComponent implements OnInit {
  producto!: ProductoDetailResponse;
  userRole = localStorage.getItem('userRole');
  cantidadInput: number = 1;

  constructor(
    private productoService: ProductoService,
    private carritoService: CarritoService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.productoService.getProductoById(id).subscribe({
        next: (data) => this.producto = data,
        error: (err) => console.error('Error al cargar producto:', err)
      });
    }
  }

  agregarAlCarrito(cantidadInput: number): void {

  if (this.producto && this.producto.id && cantidadInput > 0) {
    
    this.carritoService.agregarProducto(this.producto.id, cantidadInput).subscribe({
      next: (venta) => {
        console.log('Producto agregado al carrito:', venta);
        Swal.fire('Añadido', 'El producto ha sido añadido al carrito.', 'success');
      },
      error: (err) => {
        console.error('Error al agregar producto al carrito:', err);
        Swal.fire('Error', 'No se pudo añadir el producto al carrito.', 'error');
      }
    });
  }
}



}
