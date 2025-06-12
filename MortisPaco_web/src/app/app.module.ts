import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
<<<<<<< Updated upstream

@NgModule({
  declarations: [
    AppComponent
=======
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
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ProductoDetailsComponent } from './components/producto-details/producto-details.component';

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
    ProductoDetailsComponent
>>>>>>> Stashed changes
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
