import { Component, Inject } from '@angular/core';
import { CarritoService } from '../../services/carrito.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})


export class HomeComponent {
  productos: any[] = [];
  constructor(private carritoService: CarritoService) {}

  handleAddToCart(productId: number) {
    this.carritoService.agregarProducto(productId, 1);
  }
}
