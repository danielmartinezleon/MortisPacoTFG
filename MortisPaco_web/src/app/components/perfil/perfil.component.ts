import { Component, OnInit } from '@angular/core';
import { UsuarioDetailResponse } from '../../interfaces/usuario/usuario.interfaces';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit {
  
  usuario: UsuarioDetailResponse | null = null;
  loading = true;

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.usuarioService.getUsuarioDetail().subscribe({
      next: (res) => {
        this.usuario = res;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar perfil:', err);
        this.loading = false;
      }
    });
  }


}