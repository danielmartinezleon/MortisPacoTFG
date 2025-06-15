import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UsuarioService } from '../../services/usuario.service';
import { VentaResponse } from '../../interfaces/venta/venta.interface';

@Component({
  selector: 'app-detalles-pedido',
  templateUrl: './detalles-pedido.component.html',
  styleUrl: './detalles-pedido.component.css'
})
export class DetallesPedidoComponent implements OnInit {

  pedido!: VentaResponse;
  loading = true;
  error = '';

   constructor(
    private route: ActivatedRoute,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.usuarioService.getHistorial().subscribe({
        next: (ventas) => {
          const lista = Array.isArray(ventas) ? ventas : [ventas];
          const ventaEncontrada = lista.find(v => v.id === id);

          if (ventaEncontrada) {
            this.pedido = ventaEncontrada;
          } else {
            this.error = 'Pedido no encontrado';
          }
          this.loading = false;
        },
        error: () => {
          this.error = 'Error al cargar el historial';
          this.loading = false;
        }
      });
    }
  }
}
