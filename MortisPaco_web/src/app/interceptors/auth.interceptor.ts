import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, catchError, switchMap, throwError } from 'rxjs';
import { UsuarioService } from '../services/usuario.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private isRefreshing = false;

  constructor(
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessToken = localStorage.getItem('token');

    let authReq = req;
    if (accessToken) {
      authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${accessToken}`
        }
      });
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (
          error.status === 401 &&
          !authReq.url.includes('/usuario/auth/login') &&
          !authReq.url.includes('/usuario/auth/refresh/token')
        ) {
          return this.handle401(authReq, next);
        }
        return throwError(() => error);
      })
    );
  }

  private handle401(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.isRefreshing) {
      // Si ya está refrescando, no hagas múltiples llamadas
      return throwError(() => new Error('Token refresh already in progress'));
    }

    this.isRefreshing = true;

    return this.usuarioService.refreshToken().pipe(
      switchMap((res) => {
        this.isRefreshing = false;
        localStorage.setItem('token', res.token);
        localStorage.setItem('refreshToken', res.refreshToken);

        const cloned = request.clone({
          setHeaders: {
            Authorization: `Bearer ${res.token}`
          }
        });
        return next.handle(cloned);
      }),
      catchError(err => {
        this.isRefreshing = false;
        localStorage.clear();
        this.router.navigate(['/login']);
        return throwError(() => err);
      })
    );
  }
}
