import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Location } from '@angular/common';
import { environment } from '../../../environments/environment';
import { UsuarioService } from '../../services/usuario.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-editar-perfil',
  templateUrl: './editar-perfil.component.html',
  styleUrls: ['./editar-perfil.component.css'],
})
export class EditarPerfilComponent implements OnInit {
  form!: FormGroup;
  userId!: string;
  loading = true;
  error = '';

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private http: HttpClient,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.usuarioService.getUsuarioDetail().subscribe({
      next: (usuario) => {
        this.userId = usuario.id;
        this.form = this.fb.group(
          {
            nombre: [usuario.nombre, [Validators.required]],
            apellidos: [usuario.apellidos, [Validators.required]],
            email: [usuario.email, [Validators.required, Validators.email]],
            direccion: [usuario.direccion, [Validators.required]],
            password: ['', [Validators.required, Validators.minLength(6)]],
            verifyPassword: ['', [Validators.required]],
          },
          { validators: this.passwordsMatchValidator }
        );

        this.loading = false;
      },
      error: () => {
        this.error = 'No se pudo cargar el perfil';
        this.loading = false;
      },
    });
  }

  passwordsMatchValidator(group: FormGroup) {
    return group.get('password')!.value === group.get('verifyPassword')!.value
      ? null
      : { mismatch: true };
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    const editUserCmd = this.form.value; // Incluye verifyPassword

    this.http
      .put(
        `${environment.apiBaseUrl}/usuario/editar/${this.userId}`,
        editUserCmd
      )
      .subscribe({
        next: () => {
          Swal.fire('Actualizado', 'Perfil actualizado correctamente.', 'success');
          this.location.back();
        },
        error: () => {
          this.error = 'Error al actualizar el perfil';
          Swal.fire('Error', 'Error al actualizar el perfil', 'error');
        },
      });
  }
}
