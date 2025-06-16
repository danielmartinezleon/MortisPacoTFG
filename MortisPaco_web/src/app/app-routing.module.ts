import { NgModule } from '@angular/core';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { ProductoListComponent } from './components/producto-list/producto-list.component';
import { ProductoFormComponent } from './components/producto-form/producto-form.component';
import { CategoriaListComponent } from './components/categoria-list/categoria-list.component';
import { CarritoComponent } from './components/carrito/carrito.component';
import { RouterModule, Routes } from '@angular/router';
import { ProductoDetailComponent } from './components/producto-detail/producto-detail.component';
import { ActivateComponent } from './components/auth/activate/activate.component';
import { HistorialComponent } from './components/historial/historial.component';
import { DetallesPedidoComponent } from './components/detalles-pedido/detalles-pedido.component';
import { QuienesSomosComponent } from './components/quienes-somos/quienes-somos.component';
import { AvisosLegalesComponent } from './components/avisos-legales/avisos-legales.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegisterComponent },
  { path: 'activate', component: ActivateComponent },
  { path: 'productos', component: ProductoListComponent },
  { path: 'productos/categoria/:categoria', component: ProductoListComponent },
  { path: 'producto/:id', component: ProductoDetailComponent },
  { path: 'productos/nuevo', component: ProductoFormComponent },
  { path: 'categorias', component: CategoriaListComponent },
  { path: 'carrito', component: CarritoComponent },
  { path: 'productos/crear', component: ProductoFormComponent },
  { path: 'productos/editar/:id', component: ProductoFormComponent },
  { path: 'productos/eliminar/:id', component: ProductoDetailComponent },
  { path: 'historial', component: HistorialComponent },
  { path: 'detalle/:id', component: DetallesPedidoComponent },
  { path: 'quienessomos', component: QuienesSomosComponent },
  { path: 'avisoslegales', component: AvisosLegalesComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
