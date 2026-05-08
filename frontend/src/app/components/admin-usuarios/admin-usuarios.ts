import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Proyecto } from '../../services/proyecto/proyecto';

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-usuarios.html',
  styleUrl: './admin-usuarios.css',
})
export class AdminUsuarios implements OnInit {
  listaUsuarios: any[] = [];

  constructor(private proyectoService: Proyecto, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.proyectoService.obtenerUsuariosAdmin().subscribe({
      next: (datos) => {
        this.listaUsuarios = datos;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error cargando usuarios', err)
    });
  }

  esActivo(estado: any): boolean {
    return estado == 1 || estado === '1' || estado === 'ACTIVO';
  }

  alternarEstado(usuario: any): void {
    const estadoActual = this.esActivo(usuario.estado);
    // Si está activo, mandamos un '0' (Inactivo), si no, un '1' (Activo)
    const nuevoEstadoBD = estadoActual ? '0' : '1'; 
    const accion = estadoActual ? 'desactivar' : 'activar';
    
    if (confirm(`¿Estás seguro de ${accion} a ${usuario.username}?`)) {
      const paquete = { idUsuario: usuario.idUsuario, nuevoEstado: nuevoEstadoBD };
      
      this.proyectoService.cambiarEstadoUsuario(paquete).subscribe({
        next: (res) => {
          if (res.status === 'success') {
            usuario.estado = nuevoEstadoBD;
            this.cdr.detectChanges();
          } else {
            alert('Error al cambiar el estado en la base de datos.');
          }
        },
        error: (err) => alert('Error de red.')
      });
    }
  }
}
