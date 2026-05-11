import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Proyecto {
  private apiUrl = 'http://localhost:8080/api/proyectos/publicar';
  private apiListaUrl = 'http://localhost:8080/api/proyectos/disponibles';

  public saldoActualizado$ = new Subject<void>();

  constructor(private http: HttpClient) {}

  publicarProyecto(datosProyecto: any): Observable<any> {
    return this.http.post(this.apiUrl, datosProyecto);
  }

  obtenerProyectosDisponibles(): Observable<any> {
    return this.http.get(this.apiListaUrl);
  }

  obtenerMisPublicaciones(idUsuario: number): Observable<any> {
    // Le agregamos la hora exacta al final para destruir el caché de Firefox
    return this.http.get(`http://localhost:8080/api/proyectos/mis-publicaciones?idUsuario=${idUsuario}&t=${new Date().getTime()}`);
  }

  postularseAProyecto(datosPropuesta: any): Observable<any> {
    return this.http.post(`http://localhost:8080/api/proyectos/postularse`, datosPropuesta);
  }

  obtenerPropuestas(idProyecto: number): Observable<any> { 
    return this.http.get(`http://localhost:8080/api/proyectos/propuestas?idProyecto=${idProyecto}`);
  }

  aceptarPropuesta(datosContrato: any): Observable<any> {
    return this.http.post(`http://localhost:8080/api/contratos/aceptar`, datosContrato);
  }

  obtenerContratosFreelancer(idUsuario: number): Observable<any> {
    return this.http.get(`http://localhost:8080/api/contratos/freelancer?idUsuario=${idUsuario}`);
  }

  subirEntrega(datosEntrega: any): Observable<any> {
    return this.http.post(`http://localhost:8080/api/entregas/subir`, datosEntrega);
  }

  obtenerEntrega(idProyecto: number): Observable<any> {
    return this.http.get(`http://localhost:8080/api/entregas/obtener?idProyecto=${idProyecto}`);
  }

  evaluarEntrega(datosEvaluacion: any): Observable<any> {
    return this.http.post(`http://localhost:8080/api/entregas/evaluar`, datosEvaluacion);
  }

  enviarCalificacion(datosCalificacion: any): Observable<any> {
    return this.http.post(`http://localhost:8080/api/calificaciones/crear`, datosCalificacion);
  }

  getUltimaEntrega(idContrato: number): Observable<any> {
    return this.http.get(`http://localhost:8080/api/entregas/ultima?idContrato=${idContrato}`);
  }

  obtenerSaldo(idUsuario: number, rol: string): Observable<any> {
    return this.http.get(`http://localhost:8080/api/billetera?idUsuario=${idUsuario}&rol=${rol}`);
  }

  recargarSaldo(paquete: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/billetera', paquete);
  }

  obtenerHistorialRecargas(idUsuario: number): Observable<any> {
    return this.http.get(`http://localhost:8080/api/reportes/cliente/recargas?idUsuario=${idUsuario}`);
  }

  obtenerReporteCliente(tipo: string, idUsuario: number, fechaInicio: string = '', fechaFin: string = ''): Observable<any> {
    let url = `http://localhost:8080/api/reportes/cliente?tipo=${tipo}&idUsuario=${idUsuario}`;
    if (fechaInicio && fechaFin) {
      url += `&fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
    }
    return this.http.get(url);
  }

  obtenerUsuariosAdmin(): Observable<any> {
    return this.http.get('http://localhost:8080/api/admin/usuarios');
  }

  cambiarEstadoUsuario(datos: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/admin/usuarios', datos);
  }

  obtenerCategoriasAdmin(): Observable<any> {
    return this.http.get('http://localhost:8080/api/admin/categorias');
  }

  gestionarCategoriaAdmin(datos: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/admin/categorias', datos);
  }

  obtenerHabilidadesAdmin(): Observable<any> {
    return this.http.get('http://localhost:8080/api/admin/habilidades');
  }

  gestionarHabilidadAdmin(datos: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/admin/habilidades', datos);
  }

  obtenerComisionesAdmin(): Observable<any> {
    return this.http.get('http://localhost:8080/api/admin/comisiones');
  }

  cambiarComisionAdmin(datos: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/admin/comisiones', datos);
  }

  obtenerReportesAdmin(tipo: string, fechaInicio: string, fechaFin: string): Observable<any> {
    let url = `http://localhost:8080/api/admin/reportes?tipo=${tipo}`;
    
    if (fechaInicio && fechaFin) {
      url += `&fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
    }
    
    return this.http.get(url);
  }

  retirarPropuesta(idPropuesta: number): Observable<any> {
    return this.http.delete(`http://localhost:8080/api/propuestas?idPropuesta=${idPropuesta}`);
  }

  guardarPerfil(datos: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/perfil/completar', datos);
  }

  obtenerSaldoFreelancer(idFreelancer: number): Observable<any> {
    return this.http.get(`http://localhost:8080/api/reportes-freelancer?tipo=saldo&id=${idFreelancer}`);
  }

  obtenerReporteFreelancer(tipo: string, idFreelancer: number, fechaInicio: string, fechaFin: string): Observable<any> {
    let url = `http://localhost:8080/api/reportes-freelancer?tipo=${tipo}&id=${idFreelancer}`;
    
    if (fechaInicio && fechaFin) {
      url += `&fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
    }
    
    return this.http.get(url);
  }
}
