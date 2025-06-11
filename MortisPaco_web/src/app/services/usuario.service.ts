import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UsuarioResponse } from '../interfaces/usuario/usuario.interfaces';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(`${environment.apiBaseUrl}/auth/login`, { username, password });
  }

}
