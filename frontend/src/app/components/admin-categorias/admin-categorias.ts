import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Proyecto } from '../../services/proyecto/proyecto';

@Component({
  selector: 'app-admin-categorias',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-categorias.html',
  styleUrl: './admin-categorias.css',
})
export class AdminCategorias implements OnInit {
  categorias: any[] = [];
  nuevaCategoriaNombre: string = '';
  nuevaCategoriaDesc: string = '';
  categoriaEnEdicion: any = null;

  constructor(private proyectoService: Proyecto, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.proyectoService.obtenerCategoriasAdmin().subscribe({
      next: (datos) => {
        this.categorias = datos;
        this.cdr.detectChanges(); 
      },
      error: (err) => console.error(err)
    });
  }

  crearCategoria(): void {
    if (!this.nuevaCategoriaNombre.trim() || !this.nuevaCategoriaDesc.trim()) {
      alert('Debes ingresar nombre y descripción.');
      return;
    }
    
    const paquete = { 
      accion: 'crear', 
      nombre: this.nuevaCategoriaNombre,
      descripcion: this.nuevaCategoriaDesc // <-- LO ENVIAMOS
    };
    
    this.proyectoService.gestionarCategoriaAdmin(paquete).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.nuevaCategoriaNombre = '';
          this.nuevaCategoriaDesc = '';
          this.cargarCategorias();
          this.cdr.detectChanges();
        }
      }
    });
  }

  iniciarEdicion(cat: any): void {
    this.categoriaEnEdicion = { ...cat };
    this.cdr.detectChanges();
  }

  guardarEdicion(): void {
    if (!this.categoriaEnEdicion.nombre.trim() || !this.categoriaEnEdicion.descripcion.trim()) return;

    const paquete = { 
      accion: 'editar', 
      idCategoria: this.categoriaEnEdicion.idCategoria, 
      nombre: this.categoriaEnEdicion.nombre,
      descripcion: this.categoriaEnEdicion.descripcion // <-- LO ENVIAMOS
    };
    
    this.proyectoService.gestionarCategoriaAdmin(paquete).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.categoriaEnEdicion = null;
          this.cargarCategorias();
          this.cdr.detectChanges();
        }
      }
    });
  }

  alternarEstado(cat: any): void {
    const nuevoEstado = cat.estado == 1 ? 0 : 1;
    const paquete = { accion: 'estado', idCategoria: cat.idCategoria, estado: nuevoEstado };
    
    this.proyectoService.gestionarCategoriaAdmin(paquete).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          cat.estado = nuevoEstado;
          this.cdr.detectChanges();
        }
      }
    });
  }
}
