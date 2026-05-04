import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Proyecto } from '../../services/proyecto/proyecto'; 
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-mis-publicaciones',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mis-publicaciones.html',
  styleUrl: './mis-publicaciones.css',
})
export class MisPublicaciones implements OnInit {
  misProyectos: any[] = [];

  propuestas: any[] = [];
  proyectoViendoPropuestas: any = null;

  proyectoEnRevision: any = null;
  entregaActual: any = null;
  motivoRechazo: string = '';

  proyectoParaCalificar: number | null = null;
  contratoParaCalificar: number | null = null;
  nuevaCalificacion = { estrellas: 5, comentario: '' };

  constructor(private proyectoService: Proyecto, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.cargarMisProyectos();
  }

  cargarMisProyectos(): void {
    this.misProyectos = [];
    const datos = localStorage.getItem('usuario');
    
    if (datos) {
      const user = JSON.parse(datos);
      
      this.proyectoService.obtenerMisPublicaciones(user.idUsuario).subscribe({
        next: (data) => {
          if (typeof data === 'string') {
            this.misProyectos = JSON.parse(data);
          } else {
            this.misProyectos = data;
          }
          // Le ordenamos a Angular que actualice el HTML con los nuevos datos
          this.cdr.detectChanges(); 
        },
        error: (err) => {
          console.error("ERROR EN LA RED: ", err);
        }
      });
    }
  }

  verPropuestas(proyecto: any): void {
    this.proyectoViendoPropuestas = proyecto;
    
    this.proyectoService.obtenerPropuestas(proyecto.idProyecto).subscribe({
      next: (data) => {
        if (typeof data === 'string') {
          this.propuestas = JSON.parse(data);
        } else {
          this.propuestas = data;
        }
        // Le ordenamos a Angular que actualice el HTML con los nuevos datos
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        console.error("ERROR EN LA RED: ", err);
      }
    });
  }

  cerrarPropuestas(): void {
    this.proyectoViendoPropuestas = null;
    this.propuestas = [];
  }

  aceptarOferta(propuesta: any): void {
    const confirmacion = confirm(`¿Estás seguro de aceptar la oferta de ${propuesta.nombreFreelancer}?`);
    if (confirmacion) {
      // Armamos la caja con los datos exactos que espera leer el Servlet
      const paqueteContrato = {
        idPropuesta: propuesta.idPropuesta,
        idProyecto: propuesta.idProyecto,
        montoOfertado: propuesta.montoOfertado
      };

      this.proyectoService.aceptarPropuesta(paqueteContrato).subscribe({
        next: (respuesta) => {
          if (respuesta.status === 'success') {
            alert('¡Contrato generado con éxito! El proyecto ya está En Progreso.');
            this.cerrarPropuestas();     // Escondemos el panel
            this.cargarMisProyectos();   // Recargamos la lista para ver el cambio de estado
          } else {
            alert('Hubo un problema al generar el contrato interno.');
          }
        },
        error: (err) => {
          console.error("Error en red al aceptar propuesta:", err);
          alert('Error de conexión con el servidor.');
        }
      });
    }
  }

  abrirRevision(proyecto: any): void {
    this.proyectoEnRevision = proyecto;
    this.entregaActual = null;
    this.motivoRechazo = '';

    const idRealProyecto = proyecto.idProyecto || proyecto.id_proyecto;

    this.proyectoService.obtenerEntrega(idRealProyecto).subscribe({
      next: (data) => {
        this.entregaActual = typeof data === 'string' ? JSON.parse(data) : data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Error en la petición de entrega:", err);
        alert("Error al cargar la entrega.");
      }
    });
  }

  cerrarRevision(): void {
    this.proyectoEnRevision = null;
    this.entregaActual = null;
  }

  aceptarTrabajo(): void {
    if (!confirm("¿Estás seguro de ACEPTAR este trabajo? El proyecto se finalizará y se le pagará al freelancer.")) return;

    const idRealProyecto = this.proyectoEnRevision.idProyecto || this.proyectoEnRevision.id_proyecto;
    const idRealEntrega = this.entregaActual.idEntrega || this.entregaActual.id_entrega;
    const idRealContrato = this.entregaActual.idContrato || this.entregaActual.id_contrato;

    const paquete = {
      idEntrega: idRealEntrega,
      idProyecto: idRealProyecto,
      idContrato: idRealContrato,
      esAceptada: true,
      motivoRechazo: ""
    };

    this.enviarEvaluacion(paquete, "¡Trabajo aceptado! El proyecto ha finalizado.");
  }

  rechazarTrabajo(): void {
    if (!this.motivoRechazo.trim()) {
      alert("Debes escribir un motivo para rechazar el trabajo (ej. 'Falta el color azul').");
      return;
    }

    const idRealProyecto = this.proyectoEnRevision.idProyecto || this.proyectoEnRevision.id_proyecto;
    const idRealEntrega = this.entregaActual.idEntrega || this.entregaActual.id_entrega;
    const idRealContrato = this.entregaActual.idContrato || this.entregaActual.id_contrato;

    const paquete = {
      idEntrega: idRealEntrega,
      idProyecto: idRealProyecto,
      idContrato: idRealContrato,
      esAceptada: false,
      motivoRechazo: this.motivoRechazo
    };

    this.enviarEvaluacion(paquete, "Trabajo rechazado. El freelancer tendrá que corregirlo.");
  }

  private enviarEvaluacion(paquete: any, mensajeExito: string): void {
    this.proyectoService.evaluarEntrega(paquete).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          alert(mensajeExito);

          if (paquete.esAceptada) {
            this.proyectoParaCalificar = paquete.idProyecto;
            this.contratoParaCalificar = paquete.idContrato;
          }

          this.cerrarRevision();
          this.cargarMisProyectos();
        } else {
          alert("Error al procesar la evaluación.");
        }
      },
      error: (err) => alert("Error de conexión con el servidor.")
    });
  }

  guardarCalificacion(): void {
    if (!this.nuevaCalificacion.comentario.trim()) {
      alert("Por favor, deja un comentario para el freelancer.");
      return;
    }

    const datos = {
      idContrato: this.contratoParaCalificar,
      estrellas: this.nuevaCalificacion.estrellas,
      comentario: this.nuevaCalificacion.comentario
    };

    this.proyectoService.enviarCalificacion(datos).subscribe({
      next: (res) => {
        if(res.status === 'success') {
          alert("Tu calificación ha sido guardada.");
          this.proyectoParaCalificar = null; 
          this.contratoParaCalificar = null; 
          this.nuevaCalificacion = { estrellas: 5, comentario: '' }; 
        }
      },
      error: (err) => alert("Error al enviar calificación.")
    });
  }
}
