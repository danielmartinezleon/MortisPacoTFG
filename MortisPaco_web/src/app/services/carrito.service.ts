import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { VentaResponse } from '../interfaces/venta/venta.interface';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {

  constructor(private http: HttpClient) { }

  agregarProducto(productoId: string, cantidad: number): Observable<VentaResponse> {
    const headers = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`,
    }
    const params = new HttpParams().set('cantidad', cantidad.toString());
    return this.http.post<VentaResponse>(`${environment.apiBaseUrl}/producto/agregar/${productoId}`, null, { params, headers });
  }

  getCarrito(): Observable<VentaResponse> {
    const headers = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`,
    }
    return this.http.get<VentaResponse>(`${environment.apiBaseUrl}/carrito/`, { headers });
  }
  eliminarProducto(lineaVentaId: string): Observable<VentaResponse> {
    const headers = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`,
    }
    return this.http.delete<VentaResponse>(`${environment.apiBaseUrl}/carrito/eliminar/${lineaVentaId}`, { headers });
  }

  cerrarCarrito(idVenta: string): Observable<VentaResponse> {
    const headers = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`,
    }
    return this.http.put<VentaResponse>(`${environment.apiBaseUrl}/carrito/cerrarventa/${idVenta}`, null, { headers });
  }
  }

  