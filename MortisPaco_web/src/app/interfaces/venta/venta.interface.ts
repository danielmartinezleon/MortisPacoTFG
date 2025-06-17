export interface VentaResponse {
  id: string
  fecha: string
  importeTotal: number
  gastosEnvio: number
  abierta: boolean
  lineas: Linea[]
}

export interface Linea {
  productoId: string
  nombreProducto: string
  cantidad: number
  totalLinea: number
  imagen: string
}
