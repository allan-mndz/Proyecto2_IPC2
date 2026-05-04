import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Proyecto } from '../../services/proyecto/proyecto';   

@Component({
  selector: 'app-publicar-proyecto',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './publicar-proyecto.html',
  styleUrl: './publicar-proyecto.css',
})
export class PublicarProyecto implements OnInit{
  nuevoProyecto = {
    idCliente: 0,
    idCategoria: 1,
    titulo: '',
    descripcion: '',
    presupuestoMaximo: null,
    fechaLimite: ''
  }

  mensajeExito: string = '';
  mensajeError: string = '';

  constructor(private proyectoService: Proyecto) {}
  
  ngOnInit(): void {
    const datosUsuario = localStorage.getItem('usuario');
    if (datosUsuario) {
      const usuarioObj = JSON.parse(datosUsuario); 
      this.nuevoProyecto.idCliente = usuarioObj.idUsuario;
    }
  }

  onPublicar(): void {
    this.proyectoService.publicarProyecto(this.nuevoProyecto).subscribe({
      next: (respuesta) => {
        if(respuesta.status === 'success') {
          this.mensajeExito = 'Proyecto publicado exitosamente.';
          this.mensajeError = '';

          //limpiar el formulario

          this.nuevoProyecto.titulo = '';
          this.nuevoProyecto.descripcion = '';
          this.nuevoProyecto.presupuestoMaximo = null;
          this.nuevoProyecto.fechaLimite = '';
        }
      },
      error: (error) => {
        this.mensajeError = 'Error al publicar el proyecto. Por favor, inténtalo de nuevo.';
        this.mensajeExito = '';
      }
    });  
  }
}
