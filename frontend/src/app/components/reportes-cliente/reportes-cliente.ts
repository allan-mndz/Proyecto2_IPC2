import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Proyecto } from '../../services/proyecto/proyecto';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-reportes-cliente',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes-cliente.html',
  styleUrls: ['./reportes-cliente.css']
})
export class ReportesCliente implements OnInit {
  usuarioActual: any;
  datosReporte: any[] = [];
  
  // Controles del formulario
  tipoReporteSeleccionado: string = 'recargas';
  fechaInicio: string = '';
  fechaFin: string = '';

  constructor(private proyectoService: Proyecto, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    const datos = localStorage.getItem('usuario');
    if (datos) {
      this.usuarioActual = JSON.parse(datos);
      this.cargarReporte();
    }
  }

  cargarReporte(): void {
    this.datosReporte = [];
    if ((this.tipoReporteSeleccionado === 'proyectos' || this.tipoReporteSeleccionado === 'gastos') && (!this.fechaInicio || !this.fechaFin)) {
      return; // No carga hasta que seleccione fechas
    }

    this.proyectoService.obtenerReporteCliente(this.tipoReporteSeleccionado, this.usuarioActual.idUsuario, this.fechaInicio, this.fechaFin).subscribe({
      next: (datos) => {
        this.datosReporte = datos;
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        console.error('Error al cargar reporte', err);
        this.cdr.detectChanges();
      }
    });
  }

  exportarPDF(): void {
    const doc = new jsPDF();
    doc.setFontSize(18);
    
    let titulo = '';
    let columnas: any[] = [];
    let filas: any[] = [];

    // Configuramos el PDF dependiendo del reporte seleccionado
    if (this.tipoReporteSeleccionado === 'recargas') {
      titulo = 'Historial de Recargas';
      columnas = [['No.', 'Fecha y Hora', 'Monto Recargado']];
      filas = this.datosReporte.map((r, i) => [i + 1, r.fecha_hora, `Q${r.monto.toFixed(2)}`]);
    } 
    else if (this.tipoReporteSeleccionado === 'proyectos') {
      titulo = `Historial de Proyectos (${this.fechaInicio} al ${this.fechaFin})`;
      columnas = [['No.', 'Proyecto', 'Estado', 'Freelancer', 'Monto']];
      filas = this.datosReporte.map((p, i) => [i + 1, p.titulo, p.estado, p.freelancer, `Q${p.monto.toFixed(2)}`]);
    } 
    else if (this.tipoReporteSeleccionado === 'gastos') {
      titulo = `Gasto por Categoría (${this.fechaInicio} al ${this.fechaFin})`;
      columnas = [['No.', 'Categoría', 'Total Gastado']];
      filas = this.datosReporte.map((g, i) => [i + 1, g.categoria, `Q${g.total.toFixed(2)}`]);
    }

    doc.text(`${titulo} - ConnectWork`, 14, 20);
    doc.setFontSize(12);
    doc.text(`Cliente: ${this.usuarioActual.nombreCompleto || this.usuarioActual.username}`, 14, 30);
    doc.text(`Fecha de emisión: ${new Date().toLocaleDateString()}`, 14, 38);

    autoTable(doc, {
      head: columnas,
      body: filas,
      startY: 45,
      theme: 'grid',
      headStyles: { fillColor: [0, 86, 179] }
    });

    doc.save(`Reporte_${this.tipoReporteSeleccionado}.pdf`);
  }
}