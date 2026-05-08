import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Proyecto } from '../../services/proyecto/proyecto';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-admin-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-reportes.html',
  styleUrls: ['../reportes-cliente/reportes-cliente.css'] // Reusamos el estilo de reportes
})
export class AdminReportesComponent {
  tipoSeleccionado: string = 'comisiones';
  fechaInicio: string = '';
  fechaFin: string = '';
  datos: any = null;

  constructor(private proyectoService: Proyecto) {}

  generarReporte() {
    this.proyectoService.obtenerReportesAdmin(this.tipoSeleccionado, this.fechaInicio, this.fechaFin).subscribe({
      next: (res) => {
        this.datos = res;
        this.exportarPDF();
      }
    });
  }

  exportarPDF() {
    const doc = new jsPDF();
    doc.text("ConnectWork - Reporte Administrativo", 14, 15);
    
    let columns: string[] = [];
    let rows: any[] = [];

    if (this.tipoSeleccionado === 'comisiones') {
      columns = ["Porcentaje", "Inicio", "Fin"];
      rows = this.datos.map((c: any) => [c.porcentaje + "%", c.fecha_inicio, c.fecha_fin || "Vigente"]);
    } 
    else if (this.tipoSeleccionado === 'freelancers') {
      columns = ["Nombre del Freelancer", "Contratos Completados", "Total Generado"];
      rows = this.datos.map((f: any) => [f.nombre, f.contratos, "Q" + (f.total || 0).toFixed(2)]);
    } 
    else if (this.tipoSeleccionado === 'categorias') {
      columns = ["Categoría", "Cantidad de Contratos Completados"];
    
      rows = this.datos.map((c: any) => [c.nombre, c.contratos]);
    } 
    else if (this.tipoSeleccionado === 'ingresos') {
      columns = ["Total de Contratos Completados", "Ingresos Totales (Comisiones)"];
      if (this.datos && this.datos.contratos !== undefined) {
        rows = [
          [this.datos.contratos, "Q" + (this.datos.total || 0).toFixed(2)]
        ];
      } else {
        // Por si no hay ingresos en esas fechas
        rows = [["0", "Q0.00"]];
      }
    }

    autoTable(doc, { head: [columns], body: rows, startY: 25 });
    doc.save(`Reporte_${this.tipoSeleccionado}.pdf`);
  }
}