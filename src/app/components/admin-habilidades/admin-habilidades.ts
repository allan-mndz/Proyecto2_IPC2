import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Proyecto } from '../../services/proyecto/proyecto';

@Component({
  selector: 'app-admin-habilidades',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-habilidades.html',
  styleUrl: './admin-habilidades.css',
})
export class AdminHabilidadesComponent implements OnInit {
  habilidades: any[] = [];
  categoriasActivas: any[] = [];
  
  nuevaHabilidadCat: number = 0;
  nuevaHabilidadNombre: string = '';
  nuevaHabilidadDesc: string = ''; 
  
  habilidadEnEdicion: any = null;

  constructor(private proyectoService: Proyecto, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.cargarCategorias(); // Primero cargamos las categorías
    this.cargarHabilidades(); // Luego las habilidades
    this.cdr.detectChanges(); // Aseguramos que Angular detecte los cambios después de cargar datos
  }

  cargarCategorias(): void {
    this.proyectoService.obtenerCategoriasAdmin().subscribe({
      next: (datos: any) => {
        this.categoriasActivas = datos.filter((c: any) => c.estado == 1);
        this.cdr.detectChanges(); // Detectamos cambios después de actualizar las categorías
      },
      error: (err: any) => console.error(err)
    });
  }

  cargarHabilidades(): void {
    this.proyectoService.obtenerHabilidadesAdmin().subscribe({
      next: (datos: any) => {
        this.habilidades = datos;
        this.cdr.detectChanges(); // Detectamos cambios después de actualizar las habilidades
      },
      error: (err: any) => console.error(err)
    });
  }

  crearHabilidad(): void {
    if (!this.nuevaHabilidadNombre.trim() || !this.nuevaHabilidadDesc.trim() || this.nuevaHabilidadCat == 0) {
      alert('Debes seleccionar una categoría, ingresar nombre y descripción.');
      return;
    }
    
    const paquete = { 
      accion: 'crear', 
      idCategoria: Number(this.nuevaHabilidadCat),
      nombre: this.nuevaHabilidadNombre,
      descripcion: this.nuevaHabilidadDesc 
    };
    
    this.proyectoService.gestionarHabilidadAdmin(paquete).subscribe({
      next: (res: any) => {
        if (res.status === 'success') {
          this.nuevaHabilidadCat = 0;
          this.nuevaHabilidadNombre = '';
          this.nuevaHabilidadDesc = '';
          this.cargarHabilidades();
        }
      },
      error: (err: any) => console.error(err)
    });
  }

  iniciarEdicion(hab: any): void {
    this.habilidadEnEdicion = { ...hab };
  }

  guardarEdicion(): void {
    if (!this.habilidadEnEdicion.nombre.trim() || !this.habilidadEnEdicion.descripcion.trim()) return;

    const paquete = { 
      accion: 'editar', 
      idHabilidad: this.habilidadEnEdicion.idHabilidad,
      idCategoria: Number(this.habilidadEnEdicion.idCategoria),
      nombre: this.habilidadEnEdicion.nombre,
      descripcion: this.habilidadEnEdicion.descripcion 
    };
    
    this.proyectoService.gestionarHabilidadAdmin(paquete).subscribe({
      next: (res: any) => {
        if (res.status === 'success') {
          this.habilidadEnEdicion = null;
          this.cargarHabilidades();
        }
      },
      error: (err: any) => console.error(err)
    });
  }

  alternarEstado(hab: any): void {
    const nuevoEstado = hab.estado == 1 ? 0 : 1;
    const paquete = { accion: 'estado', idHabilidad: hab.idHabilidad, estado: nuevoEstado };
    
    this.proyectoService.gestionarHabilidadAdmin(paquete).subscribe({
      next: (res: any) => {
        if (res.status === 'success') {
          hab.estado = nuevoEstado;
          this.cdr.detectChanges(); // Detectamos cambios después de actualizar el estado
        }
      },
      error: (err: any) => console.error(err)
    });
  }
}