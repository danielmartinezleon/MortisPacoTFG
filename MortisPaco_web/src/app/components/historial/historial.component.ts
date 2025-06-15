import { Component, OnInit } from '@angular/core';

import { VentaResponse } from '../../interfaces/venta/venta.interface';
import { UsuarioService } from '../../services/usuario.service';


@Component({
  selector: 'app-historial',
  templateUrl: './historial.component.html',
  styleUrls: ['./historial.component.css']
})
export class HistorialComponent implements OnInit {
  historial: VentaResponse[] = [];
  loading = true;
  error = '';

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.usuarioService.getHistorial().subscribe({
      next: (data) => {
        this.historial = Array.isArray(data) ? data : [data];
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al obtener el historial';
        this.loading = false;
      }
    });
  }
}
