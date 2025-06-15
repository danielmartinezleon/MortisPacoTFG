import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UsuarioResponse } from '../interfaces/usuario/usuario.interfaces';
import { VentaResponse } from '../interfaces/venta/venta.interface';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(`${environment.apiBaseUrl}/usuario/auth/login`, { username, password });
  }

  register(username: string, email: string,nombre: string,apellidos: string, direccion: string, password: string, verifyPassword: string): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(`${environment.apiBaseUrl}/usuario/auth/registerUser`, { username, email, nombre, apellidos, direccion, password, verifyPassword });
  }

  activate(token: string): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(`${environment.apiBaseUrl}/usuario/activate/account/`, { token });
  }

  getHistorial(): Observable<VentaResponse> {
    const headers = { Authorization: `Bearer ${localStorage.getItem('token')}` };
    return this.http.get<VentaResponse>(`${environment.apiBaseUrl}/usuario/historial`, { headers });
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userRole');
    this.http.post(`${environment.apiBaseUrl}/usuario/auth/logout`, {}).subscribe({
      next: () => {
        console.log('Logout successful');
      },
      error: (error) => {
        console.error('Logout failed:', error);
      }
    });
  }

}
