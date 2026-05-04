import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Proyecto {
  private apiUrl = 'http://localhost:8080/api/proyectos/publicar';
  private apiListaUrl = 'http://localhost:8080/api/proyectos/disponibles';

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
}
