import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Proyecto } from '../../services/proyecto/proyecto';

@Component({
  selector: 'app-buscar-trabajos',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './buscar-trabajos.html',
  styleUrl: './buscar-trabajos.css',
})

export class BuscarTrabajos implements OnInit {

  proyectos: any[] = [];

  proyectoSeleccionado: any = null;
  nuevaPropuesta = {
    monto: null,
    dias: null,
    mensaje: '',
  };

  constructor(private proyectoService: Proyecto, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.cargarProyectos();
  }

  cargarProyectos(): void {
    this.proyectoService.obtenerProyectosDisponibles().subscribe({
      next: (data) => {
        if (typeof data === 'string') {
          this.proyectos = JSON.parse(data);
        } else {
          this.proyectos = data;
        }
        this.cdr.detectChanges(); 
      },
      error: (err) => console.error('Error al obtener proyectos:', err)
    });
  }

  abrirFormulario(proyecto: any): void {
    this.proyectoSeleccionado = proyecto;
    this.nuevaPropuesta = { monto: null, dias: null, mensaje: '' }; // Limpiamos si había algo antes
  }

  cancelarPostulacion(): void {
    this.proyectoSeleccionado = null;
  }

  enviarPropuesta(): void {
    const datosUsuario = localStorage.getItem('usuario');
    if (!datosUsuario) {
      alert("Error: No estás logueado.");
      return;
    }

    const user = JSON.parse(datosUsuario);

    const paquetePostulacion = {
      idProyecto: this.proyectoSeleccionado.idProyecto,
      idFreelancer: user.idUsuario,
      montoOfertado: this.nuevaPropuesta.monto,       
      plazoDias: this.nuevaPropuesta.dias,
      cartaPresentacion: this.nuevaPropuesta.mensaje
    };

    this.proyectoService.postularseAProyecto(paquetePostulacion).subscribe({
      next: (respuesta) => {
        if (respuesta.status === 'success') {
          alert("¡Propuesta enviada con éxito al cliente!");
          this.proyectoSeleccionado = null; // Cerramos el formulario
          this.cdr.detectChanges();
        } else {
          alert("Hubo un problema al enviar la propuesta.");
        }
      },
      error: (err) => {
        console.error("Error en red:", err);
        alert("Error de conexión al postularse.");
      }
    });
  }

}