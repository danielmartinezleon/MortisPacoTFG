import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Producto } from '../../interfaces/producto/producto-list.interface';

@Component({
  selector: 'app-producto-card',
  templateUrl: './producto-card.component.html',
  styleUrl: './producto-card.component.css'
})
export class ProductoCardComponent {
  @Input() producto!: Producto;

}