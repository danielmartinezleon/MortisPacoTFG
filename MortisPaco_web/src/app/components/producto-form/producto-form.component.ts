import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { ActivatedRoute, Router } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { Location } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-producto-form',
  templateUrl: './producto-form.component.html',
  styleUrls: ['./producto-form.component.css'],
})
export class ProductoFormComponent implements OnInit {
  productoForm: FormGroup;
  selectedFile: File | null = null;
  isEditMode = false;
  productoId: string | null = null;

  constructor(
    private fb: FormBuilder,
    private productoService: ProductoService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location
  ) {
    this.productoForm = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: ['', Validators.required],
      stock: [0, [Validators.required, Validators.min(0)]],
      precio: [0, [Validators.required, Validators.min(0)]],
      categoriaId: ['', Validators.required],
      descuento: [false],
    });
  }

  ngOnInit(): void {
    this.productoId = this.route.snapshot.paramMap.get('id');
    this.isEditMode = !!this.productoId;

    if (this.isEditMode) {
      this.productoService.getProductoById(this.productoId!).subscribe({
        next: (producto) => {
          this.productoForm.patchValue({
            nombre: producto.nombre,
            descripcion: producto.descripcion,
            stock: producto.stock,
            precio: producto.precio,
            categoriaId: producto.categoriaId,
            descuento: producto.descuento,
          });
        },
        error: (err) => {
          console.error('Error al cargar producto:', err);
          this.location.back();
        },
      });
    }
  }

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input?.files?.length) {
      this.selectedFile = input.files[0];
    }
  }

  onSubmit(): void {
    if (this.productoForm.invalid) return;

    const productoData = this.productoForm.value;

    if (this.isEditMode) {
      this.productoService
        .editarProducto(this.productoId!, productoData, this.selectedFile)
        .subscribe({
          next: () => {
            Swal.fire('Actualizado', 'Producto actualizado correctamente.', 'success');
            this.location.back();
          },
          error: (err) => {
            console.error(err);
            Swal.fire('Error', 'Error al actualizar el producto', 'error');
          },
        });
    } else {
      if (!this.selectedFile) return;

      this.productoService
        .crearProducto(productoData, this.selectedFile)
        .subscribe({
          next: () => {
            console.log(this.selectedFile);
            Swal.fire('Añadido', 'El producto ha sido añadido al carrito.', 'success');
            this.location.back();
          },
          error: (err) => {
            console.error(err);
            Swal.fire('Error', 'Error al crear el producto', 'error');
          },
        });
    }
  }
}
