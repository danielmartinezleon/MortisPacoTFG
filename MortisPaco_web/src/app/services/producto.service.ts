import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ProductoListResponse } from '../interfaces/producto/producto-list.interface';
import { ProductoDetailResponse } from '../interfaces/producto/producto-detail.interface';

import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class ProductoService {

  constructor(private http: HttpClient) { }

  getProductos(page: number): Observable<ProductoListResponse> {
    return this.http.get<ProductoListResponse>(`${environment.apiBaseUrl}/producto/lista?page=${page}`);
  }

  getProductoById(id: string) {
    return this.http.get<ProductoDetailResponse>(`${environment.apiBaseUrl}/producto/${id}`);
  }

  getProductosByCategoria(categoria: string, page: number): Observable<ProductoListResponse> {
    return this.http.get<ProductoListResponse>(`${environment.apiBaseUrl}/producto/buscar/?categoria=${categoria}&page=${page}`);
  }
}
