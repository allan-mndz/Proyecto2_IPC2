import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
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
    idCategoria: 0,
    titulo: '',
    descripcion: '',
    presupuestoMaximo: null,
    fechaLimite: ''
  }

  categoriasActivas: any[] = [];
  mensajeExito: string = '';
  mensajeError: string = '';

  constructor(private proyectoService: Proyecto, private cdr: ChangeDetectorRef) {}
  
ngOnInit(): void {
    const datosUsuario = localStorage.getItem('usuario');
    if (datosUsuario) {
      const usuarioObj = JSON.parse(datosUsuario); 
      this.nuevoProyecto.idCliente = usuarioObj.idUsuario;
    }
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.proyectoService.obtenerCategoriasAdmin().subscribe({
      next: (datos) => {
        this.categoriasActivas = datos.filter((c: any) => c.estado == 1);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error al cargar categorías', err)
    });
  }

  onPublicar(): void {
    this.proyectoService.publicarProyecto(this.nuevoProyecto).subscribe({
      next: (respuesta) => {
        if(respuesta.status === 'success') {
          this.mensajeExito = 'Proyecto publicado exitosamente.';
          this.mensajeError = '';

          //limpiar el formulario

          this.nuevoProyecto.titulo = '';
          this.nuevoProyecto.idCategoria = 0;
          this.nuevoProyecto.descripcion = '';
          this.nuevoProyecto.presupuestoMaximo = null;
          this.nuevoProyecto.fechaLimite = '';

          this.cdr.detectChanges();
        }
      },
      error: (error) => {
        this.mensajeError = 'Error al publicar el proyecto. Por favor, inténtalo de nuevo.';
        this.mensajeExito = '';
        this.cdr.detectChanges();
      }
    });  
  }
}
