export interface UsuarioResponse {
  id: string
  username: string
  token: string
  refreshToken: string
  userRole: string
}

export interface UsuarioDetailResponse {
  id: string
  username: string
  nombre: string
  apellidos: string
  email: string
  direccion: string
}
