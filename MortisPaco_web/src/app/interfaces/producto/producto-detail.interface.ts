export interface ProductoDetailResponse {
  id: string;
  nombre: string;
  descripcion: string;
  stock: number;
  precio: number;
  descuento: boolean;
  categoriaId: string;
  imageUrl: string;
}
