import { Component } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  username = '';
  email = '';
  nombre = '';
  apellidos = '';
  direccion = '';
  password = '';
  verifyPassword = '';

  emailValido = true;
  emailTocado = false;

  constructor(private usuarioService: UsuarioService, private router: Router) {}

  verificarEmail() {
    this.emailTocado = true;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    this.emailValido = emailRegex.test(this.email);
  }

  register() {
    this.verificarEmail(); // Validar email antes de enviar

    if (!this.emailValido || this.password !== this.verifyPassword) return;

    this.usuarioService.register(
      this.username,
      this.email,
      this.nombre,
      this.apellidos,
      this.direccion,
      this.password,
      this.verifyPassword
    ).subscribe({
      next: (response) => {
        this.router.navigate(['/activate'], {
          queryParams: { token: response.token }
        }).then(() => window.location.reload());
      },
      error: (error) => {
        console.error('Error en el registro:', error);
      }
    });
  }
}
