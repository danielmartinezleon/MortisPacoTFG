import { Component, Inject } from '@angular/core';
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
    this.usuarioService.login(this.username, this.password).subscribe(
      {
        next: (response) => {
          localStorage.setItem('token', response.token);
          localStorage.setItem('refreshToken', response.refreshToken);
          localStorage.setItem('userRole', response.userRole);
          localStorage.setItem('username', this.username);
          console.log('Login successful:', response.userRole);
          
          if (response.userRole) {
            this.router.navigate(['/']).then(() => {
            window.location.reload();
            });
            
        }},
        error: (error) => {
        console.error('Login failed:', error);
      }
      }
    );
  }
  
}
