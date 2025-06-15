import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { CarritoComponent } from './components/carrito/carrito.component';
import { PerfilComponent } from './components/perfil/perfil.component';
import { ProductosComponent } from './components/admin/productos/productos.component';
import { CategoriasComponent } from './components/admin/categorias/categorias.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FooterComponent } from './components/footer/footer.component';
import { ProductoCardComponent } from './components/producto-card/producto-card.component';
import { ProductoListComponent } from './components/producto-list/producto-list.component';
import { ProductoFormComponent } from './components/producto-form/producto-form.component';
import { CategoriaListComponent } from './components/categoria-list/categoria-list.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ProductoDetailComponent } from './components/producto-detail/producto-detail.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ActivateComponent } from './components/auth/activate/activate.component';
import { HistorialComponent } from './components/historial/historial.component';
import { DetallesPedidoComponent } from './components/detalles-pedido/detalles-pedido.component';
import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';
import { LOCALE_ID } from '@angular/core';


registerLocaleData(localeEs, 'es-ES');



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    CarritoComponent,
    PerfilComponent,
    ProductosComponent,
    CategoriasComponent,
    NavbarComponent,
    FooterComponent,
    ProductoCardComponent,
    ProductoListComponent,
    ProductoFormComponent,
    CategoriaListComponent,
    ProductoDetailComponent,
    ActivateComponent,
    HistorialComponent,
    DetallesPedidoComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule
  ],
  providers: [
    { provide: LOCALE_ID, useValue: 'es-ES' }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
