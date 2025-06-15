import { Component } from '@angular/core';
import { CarritoService } from '../../services/carrito.service';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-carrito',
  templateUrl: './carrito.component.html',
  styleUrl: './carrito.component.css'
})
export class CarritoComponent {
  carritoItems: any[] = [];
  ventaId: string = '';
  constructor(private carritoService: CarritoService) {}

  ngOnInit(): void {
    this.cargarCarrito();
  }

  cargarCarrito(): void {
    this.carritoService.getCarrito().subscribe({
      next: (data) => {
        this.carritoItems = data.lineas;
        this.ventaId = data.id;
        console.log('Carrito cargado:', data);
      },
      error: (err) => {
        console.error('Error al cargar el carrito:', err);
      }
    });
  }
  eliminarDelCarrito(lineaVentaId: string): void {
    this.carritoService.eliminarProducto(lineaVentaId).subscribe({
      next: () => {
        console.log('Producto eliminado del carrito');
        this.cargarCarrito(); // Recargar el carrito después de eliminar un producto
      },
      error: (err) => {
        console.error('Error al eliminar el producto del carrito:', err);
      }
    });
  }

  calcularTotal(): number {
    return this.carritoItems.reduce((total, item) => {
      return total + (item.totalLinea * item.cantidad);
    }, 0);
  }

  cerrarCarrito(): void {
  this.carritoService.cerrarCarrito(this.ventaId).subscribe({
    next: (response) => {
      console.log('Carrito cerrado:', response);
      Swal.fire('Listo', 'Su compra ha sido realizada con éxito.', 'success');
      this.cargarCarrito();

      setTimeout(() => {
        location.reload();
      }, 2000);
    },
    error: (err) => {
      console.error('Error al cerrar el carrito:', err);
      Swal.fire('Error', 'No se pudo realizar la compra.', 'error');
    }
  });
}

}
