import { Component } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  searchTerm: string = '';


  get userRole(): string | null {
    return localStorage.getItem('userRole');
  }

  get username(): string | null {
    return localStorage.getItem('username');
  }

  constructor(private usuarioService: UsuarioService,
              private router: Router
  ) {}

  onSearch(): void {
  if (this.searchTerm.trim()) {
    this.router.navigate(['/productos'], {
      queryParams: { nombre: this.searchTerm.trim() }
    });
  }
}


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

