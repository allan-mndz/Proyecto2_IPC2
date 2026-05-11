import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Proyecto } from '../../services/proyecto/proyecto';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-reportes-freelancer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes-freelancer.html',
  styleUrls: ['./reportes-freelancer.css']
})
export class ReportesFreelancer implements OnInit {
  usuarioActual: any;
  saldoActual: number = 0;
  datosReporte: any[] = [];
  
  // Controles
  tipoReporteSeleccionado: string = 'contratos';
  fechaInicio: string = '';
  fechaFin: string = '';

  constructor(private proyectoService: Proyecto, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    const datos = localStorage.getItem('usuario');
    if (datos) {
      this.usuarioActual = JSON.parse(datos);
      this.cargarSaldo();
      this.cargarReporte();
    }
  }

  cargarSaldo(): void {

    this.proyectoService.obtenerSaldoFreelancer(this.usuarioActual.idUsuario).subscribe({
      next: (res: any) => this.saldoActual = res.saldo,
      error: (err) => console.error('Error al cargar saldo', err)
    });
  }

  cargarReporte(): void {
    this.datosReporte = [];
    if ((this.tipoReporteSeleccionado === 'contratos' || this.tipoReporteSeleccionado === 'propuestas') && (!this.fechaInicio || !this.fechaFin)) {
      return; 
    }

    this.proyectoService.obtenerReporteFreelancer(this.tipoReporteSeleccionado, this.usuarioActual.idUsuario, this.fechaInicio, this.fechaFin).subscribe({
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

    if (this.tipoReporteSeleccionado === 'contratos') {
      titulo = `Historial de Contratos (${this.fechaInicio} al ${this.fechaFin})`;
      columnas = [['No.', 'Cliente', 'Proyecto', 'Monto Recibido', 'Calificación']];
      filas = this.datosReporte.map((r, i) => [i + 1, r.cliente, r.proyecto, `Q${r.monto_recibido.toFixed(2)}`, r.calificacion]);
    } 
    else if (this.tipoReporteSeleccionado === 'top_categorias') {
      titulo = 'Top 5 Categorías Trabajadas';
      columnas = [['No.', 'Categoría', 'Contratos', 'Total Ingresos']];
      filas = this.datosReporte.map((c, i) => [i + 1, c.categoria, c.cantidad_contratos, `Q${c.total_ingresos.toFixed(2)}`]);
    } 
    else if (this.tipoReporteSeleccionado === 'propuestas') {
      titulo = `Reporte de Propuestas (${this.fechaInicio} al ${this.fechaFin})`;
      columnas = [['No.', 'Proyecto', 'Monto Ofertado', 'Estado']];
      filas = this.datosReporte.map((p, i) => [i + 1, p.proyecto, `Q${p.monto_ofertado.toFixed(2)}`, p.estado]);
    }

    doc.text(`${titulo} - ConnectWork`, 14, 20);
    doc.setFontSize(12);
    doc.text(`Freelancer: ${this.usuarioActual.nombreCompleto || this.usuarioActual.username}`, 14, 30);
    doc.text(`Saldo actual: Q${this.saldoActual.toFixed(2)}`, 14, 38);

    autoTable(doc, {
      head: columnas,
      body: filas,
      startY: 45,
      theme: 'grid',
      headStyles: { fillColor: [0, 86, 179] }
    });

    doc.save(`Reporte_Freelancer_${this.tipoReporteSeleccionado}.pdf`);
  }
}