import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {

  constructor() { }

  agregarProducto(productId: number, cantidad: number): void {
    // Implement logic to add the product to the cart
    // Example:
    // this.cart.push({ productId, cantidad });
  }
}
