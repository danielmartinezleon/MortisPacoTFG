import { Component } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-activate',
  templateUrl: './activate.component.html',
  styleUrls: ['./activate.component.css']
})
export class ActivateComponent {
  token: string = '';
  mensajeExito: string = '';
  mensajeError: string = '';

  constructor(
    private usuarioService: UsuarioService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    // Leer token desde la URL si existe
    this.route.queryParams.subscribe(params => {
      const tokenParam = params['token'];
      if (tokenParam) {
        this.token = tokenParam;
        this.activateAccount();
      }
    });
  }

  onSubmit() {
    this.activateAccount();
  }

  activateAccount(): void {
    this.usuarioService.activate(this.token).subscribe({
      next: (response) => {
        this.mensajeExito = 'Cuenta activada con éxito. Redirigiendo al login...';
        this.mensajeError = '';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (error) => {
        this.mensajeError = 'Error al activar la cuenta. Verifique el token.';
        this.mensajeExito = '';
        console.error('Error al activar la cuenta:', error);
      }
    });
  }
}
