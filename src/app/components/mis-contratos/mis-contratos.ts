import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Proyecto } from '../../services/proyecto/proyecto';

@Component({
  selector: 'app-mis-contratos',
  imports: [CommonModule, FormsModule],
  templateUrl: './mis-contratos.html',
  styleUrl: './mis-contratos.css',
})
export class MisContratos implements OnInit {
  misContratosActivos: any[] = [];

  contratoViendoEntrega: any = null;
  nuevaEntrega = { descripcion: '', archivosUrl: '' };

  constructor(private proyectoService: Proyecto, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.cargarContratos();
  }

  cargarContratos(): void {
    const datos = localStorage.getItem('usuario');
    if (datos) {
      const user = JSON.parse(datos);
      this.proyectoService.obtenerContratosFreelancer(user.idUsuario).subscribe({
        next: (data) => {
          this.misContratosActivos = typeof data === 'string' ? JSON.parse(data) : data;
          this.cdr.detectChanges();
        },
        error: (err) => console.error("Error al cargar contratos: ", err)
      });
    }
  }

  abrirFormularioEntrega(contrato: any): void {
    this.contratoViendoEntrega = contrato;
    this.nuevaEntrega = { descripcion: '', archivosUrl: '' };
  }

  cancelarEntrega(): void {
    this.contratoViendoEntrega = null;
  }

  enviarTrabajo(): void {
    if (!this.nuevaEntrega.descripcion || !this.nuevaEntrega.archivosUrl) {
      alert("Por favor, llena la descripción y pega el enlace de tus archivos.");
      return;
    }

    const paquete = {
      idContrato: this.contratoViendoEntrega.idContrato,
      idProyecto: this.contratoViendoEntrega.idProyecto,
      descripcion: this.nuevaEntrega.descripcion,
      archivosUrl: this.nuevaEntrega.archivosUrl
    };

    this.proyectoService.subirEntrega(paquete).subscribe({
      next: (respuesta) => {
        if (respuesta.status === 'success') {
          alert('¡Trabajo enviado exitosamente! El cliente lo revisará pronto.');
          this.contratoViendoEntrega = null;
          this.cargarContratos();
        } else {
          alert('Hubo un problema al enviar la entrega.');
        }
      },
      error: (err) => alert('Error de conexión con el servidor.')
    });
  } 
}
