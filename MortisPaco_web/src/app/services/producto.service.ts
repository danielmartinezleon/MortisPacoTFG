import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ProductoListResponse } from '../interfaces/producto/producto-list.interface';
import { ProductoDetailResponse } from '../interfaces/producto/producto-detail.interface';

import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProductoService {
  constructor(private http: HttpClient) {}

  getProductos(page: number): Observable<ProductoListResponse> {
    return this.http.get<ProductoListResponse>(
      `${environment.apiBaseUrl}/producto/lista?page=${page}`
    );
  }

  getProductoById(id: string) {
    return this.http.get<ProductoDetailResponse>(
      `${environment.apiBaseUrl}/producto/${id}`
    );
  }

  getProductosByCategoria(
    categoria: string,
    page: number
  ): Observable<ProductoListResponse> {
    return this.http.get<ProductoListResponse>(
      `${environment.apiBaseUrl}/producto/buscar/?categoria=${categoria}&page=${page}`
    );
  }

  crearProducto(producto: any, file: File) {
    const formData = new FormData();
    formData.append(
      'producto',
      new Blob([JSON.stringify(producto)], { type: 'application/json' })
    );
    formData.append('file', file);

    return this.http.post(`${environment.apiBaseUrl}/producto/crear`, formData);
  }

  editarProducto(id: string, producto: any, file: File | null) {
    const formData = new FormData();
    formData.append(
      'productoEditable',
      new Blob([JSON.stringify(producto)], { type: 'application/json' })
    );
    if (file) formData.append('file', file);

    return this.http.put(
      `${environment.apiBaseUrl}/producto/editar/${id}`,
      formData
    );
  }

  eliminarProducto(id: string) {

    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    return this.http.delete(
      `${environment.apiBaseUrl}/producto/eliminar/${id}`,
      { headers }
    );
  }
}
