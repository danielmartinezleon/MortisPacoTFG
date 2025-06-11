import { Component, OnInit } from '@angular/core';
import { ProductoListResponse } from '../../interfaces/producto/producto-list.interface';
import { ProductoService } from '../../services/producto.service';

@Component({
  selector: 'app-producto-list',
  templateUrl: './producto-list.component.html',
  styleUrl: './producto-list.component.css'
})
export class ProductoListComponent implements OnInit {
  productos!: ProductoListResponse;
  page: number = 1;

  constructor(private productoService: ProductoService) {
    
  }

  ngOnInit(): void {
    this.productoService.getProductos(this.page-1).subscribe({
      next: (response) => {
        this.productos = response;
      },
      error: (error) => {
        console.error('Error fetching products:', error);
      }
    });
  }
}
