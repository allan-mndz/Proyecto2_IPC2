import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Proyecto } from '../../services/proyecto/proyecto';

@Component({
  selector: 'app-completar-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './completar-perfil.html',
  styleUrl: './completar-perfil.css',
})
export class CompletarPerfilComponent implements OnInit {
  usuarioActual: any;
  rol: string = '';

  descripcionEmpresa: string = '';
  sector: string = '';
  sitioWeb: string = '';

  biografia: string = '';
  nivelExperiencia: string = 'JUNIOR';
  tarifaHora: number = 0;

  constructor(private proyectoService: Proyecto, private router: Router) {}

  ngOnInit(): void {
    const datosGuardados = localStorage.getItem('usuario');
    if (datosGuardados) {
      this.usuarioActual = JSON.parse(datosGuardados);
      this.rol = this.usuarioActual.tipoUsuario.toUpperCase(); 
    }
  }

  guardarDatos(): void {
    const paquete = {
      tipoUsuario: this.rol,
      idUsuario: this.usuarioActual.idUsuario,
      descripcion: this.descripcionEmpresa,
      sector: this.sector,
      sitioWeb: this.sitioWeb,
      biografia: this.biografia,
      nivelExperiencia: this.nivelExperiencia,
      tarifaHora: this.tarifaHora
    };

    this.proyectoService.guardarPerfil(paquete).subscribe({
      next: (res: any) => {
        if (res.status === 'success') {
          localStorage.setItem('perfilCompletado', 'true');
          
          alert('¡Perfil completado con éxito!');
          this.router.navigate(['/dashboard']);
        } else {
          alert('Error al guardar el perfil.');
        }
      }
    });
  }
}