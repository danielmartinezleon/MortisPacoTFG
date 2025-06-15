import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { ActivatedRoute, Router } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { Location } from '@angular/common';

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
            alert('Producto actualizado correctamente');
            this.location.back();
          },
          error: (err) => {
            console.error(err);
            alert('Error al actualizar el producto');
          },
        });
    } else {
      if (!this.selectedFile) return;

      this.productoService
        .crearProducto(productoData, this.selectedFile)
        .subscribe({
          next: () => {
            console.log(this.selectedFile);
            alert('Producto creado correctamente');
            this.location.back();
          },
          error: (err) => {
            console.error(err);
            alert('Error al crear el producto');
          },
        });
    }
  }
}
