import { Component } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  login() {
    alert('Login button clicked');
    this.usuarioService.login(this.username, this.password).subscribe(
      {
        next: (response) => {
          localStorage.setItem('token', response.token);
          localStorage.setItem('refreshToken', response.refreshToken);
          if (response.userRole === 'ADMIN') {
            this.router.navigate(['/admin']);
          }else if (response.userRole === 'USER') {
            this.router.navigate(['/home']);
          }
        },
        error: (error) => {
        console.error('Login failed:', error);
      }
      }
    );
  }
}
