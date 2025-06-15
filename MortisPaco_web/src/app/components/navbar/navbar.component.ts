import { Component } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  constructor(private usuarioService: UsuarioService) {}
    
  userRole = localStorage.getItem('userRole');
  username = localStorage.getItem('username');
  

  public logout(): void {
    this.usuarioService.logout();
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userRole');
    window.location.reload();
  }

  reloadAfterNavigation(): void {
  setTimeout(() => location.reload(), 100);
}

}
