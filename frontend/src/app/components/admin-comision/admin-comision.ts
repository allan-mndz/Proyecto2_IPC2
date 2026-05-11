import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Proyecto } from '../../services/proyecto/proyecto';

@Component({
  selector: 'app-admin-comision',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-comision.html',
  styleUrl: './admin-comision.css',
})
export class AdminComisionComponent implements OnInit {
  historialComisiones: any[] = [];
  comisionActual: any = null;
  nuevoPorcentaje: number | null = null;

  constructor(private proyectoService: Proyecto, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.cargarComisiones();
  }

  cargarComisiones(): void {
    this.proyectoService.obtenerComisionesAdmin().subscribe({
      next: (datos: any[]) => {
        this.historialComisiones = datos;
        // La comisión actual es la que no tiene fechaFin
        this.comisionActual = datos.find(c => c.fechaFin === null || c.fechaFin === undefined) || datos[0];
        this.cdr.detectChanges(); // Detectamos cambios después de actualizar las comisiones
      },
      error: (err: any) => console.error(err)
    });
  }

  actualizarComision(): void {
    if (this.nuevoPorcentaje === null || this.nuevoPorcentaje < 0 || this.nuevoPorcentaje > 100) {
      alert('Por favor, ingresa un porcentaje válido entre 0 y 100.');
      return;
    }

    if (confirm(`¿Estás seguro de cambiar la comisión global a ${this.nuevoPorcentaje}%? Este cambio solo afectará a los contratos nuevos.`)) {
      const paquete = { nuevoPorcentaje: this.nuevoPorcentaje };
      
      this.proyectoService.cambiarComisionAdmin(paquete).subscribe({
        next: (res: any) => {
          if (res.status === 'success') {
            alert('¡Comisión actualizada con éxito!');
            this.nuevoPorcentaje = null;
            this.cargarComisiones(); // Recarga la tabla para ver el cambio inmediato
          } else {
            alert('Hubo un error al actualizar la comisión.');
          }
        },
        error: (err: any) => alert('Error de conexión con el servidor.')
      });
    }
  }
}