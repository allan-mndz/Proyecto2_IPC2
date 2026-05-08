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

  entregaAnterior: any = null;

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
    this.entregaAnterior = null; // Reseteamos el estado cada vez que abrimos

    // Aseguramos que el id exista y se mapee correctamente
    const idContrato = contrato.id_contrato || contrato.idContrato;
    
    // Consultamos la última entrega para ver si el cliente la rechazó
    if (idContrato) {
      this.proyectoService.getUltimaEntrega(idContrato).subscribe({
        next: (entrega) => {
          // Si hay una entrega y su estado es RECHAZADA, la guardamos para mostrar la alerta
          if (entrega && entrega.estado === 'RECHAZADA') {
            this.entregaAnterior = entrega;
          }
        },
        error: (err) => console.error("Error al obtener el historial de la entrega", err)
      });
    }
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
          alert('Trabajo enviado exitosamente. El cliente lo revisará pronto.');
          this.contratoViendoEntrega = null;
          this.cargarContratos();
        } else {
          alert('Hubo un problema al enviar la entrega.');
        }
      },
      error: (err) => alert('Error de conexión con el servidor.')
    });
  } 

  retirarMiPropuesta(idPropuesta: number): void {
    if(confirm('¿Estás seguro de que deseas retirar esta propuesta? El cliente ya no podrá verla.')) {
      this.proyectoService.retirarPropuesta(idPropuesta).subscribe({
        next: (res: any) => {
          if(res.status === 'success') {
            alert('Propuesta retirada con éxito.');
            this.cargarContratos(); 
          }
        }
      });
    }
  }
}
