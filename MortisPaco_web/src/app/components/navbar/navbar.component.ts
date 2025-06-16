import { Component } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  get userRole(): string | null {
    return localStorage.getItem('userRole');
  }

  get username(): string | null {
    return localStorage.getItem('username');
  }

  constructor(private usuarioService: UsuarioService) {}

  public logout(): void {
    this.usuarioService.logout();
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('username');
    window.location.reload(); // Esto recarga y actualiza el navbar
  }

  reloadAfterNavigation(): void {
    setTimeout(() => location.reload(), 100);
  }
}

