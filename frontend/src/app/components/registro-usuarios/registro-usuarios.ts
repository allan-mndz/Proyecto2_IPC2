import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Auth } from '../../services/auth';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-registro-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './registro-usuarios.html',
  styleUrl: './registro-usuarios.css',
})
export class RegistroUsuarios {
  nuevoUsuario = {
    tipoUsuario: 'CLIENTE',
    username: '',
    password: '',
    nombreCompleto: '',
    correo: '',
    telefono: '',
    direccion: '',
    cui: '',
    fechaNacimiento: '',
  };

  mensajeExito: string = '';
  mensajeError: string = '';

  constructor(private authService: Auth, private router: Router) {}

  onRegistro(): void {
    this.authService.registrar(this.nuevoUsuario).subscribe({
      next: (response) => {
        this.mensajeExito = 'Registro exitoso. Redirigiendo al inicio de sesión...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (error) => {
        this.mensajeError = 'Error en el registro: ' + (error.error?.message || 'Error desconocido');
      },
    }); 
  }
}
