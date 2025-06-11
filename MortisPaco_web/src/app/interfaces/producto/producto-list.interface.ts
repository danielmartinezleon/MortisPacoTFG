export interface ProductoListResponse {
  content: Producto[]
  pageable: Pageable
  last: boolean
  totalElements: number
  totalPages: number
  first: boolean
  size: number
  number: number
  sort: Sort2
  numberOfElements: number
  empty: boolean
}

export interface Producto {
  id: string
  nombre: string
  descripcion: string
  precio: number
  imageUrl: string
}

export interface Pageable {
  pageNumber: number
  pageSize: number
  sort: Sort
  offset: number
  paged: boolean
  unpaged: boolean
}

export interface Sort {
  sorted: boolean
  empty: boolean
  unsorted: boolean
}

export interface Sort2 {
  sorted: boolean
  empty: boolean
  unsorted: boolean
}
