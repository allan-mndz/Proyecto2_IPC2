import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Auth } from '../../services/auth';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  username = '';
  password = '';
  errorMessage = '';

  constructor(private authService: Auth, private router: Router) {}

  onLogin(): void {
    this.errorMessage = ''; // Limpiar mensaje de error antes de intentar el login

    if (!this.username || !this.password) {
      this.errorMessage = 'Por favor, ingrese su nombre de usuario y contraseña.';
      return;
    }

    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {   
        console.log('Login exitoso:', response);
        alert(`¡Bienvenido ${response.usuario.nombreCompleto}!`);

        localStorage.setItem('token', response.token);
        localStorage.setItem('usuario', JSON.stringify(response.usuario));

        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Error durante el login:', err);
        this.errorMessage = 'Ocurrió un error durante el login. Por favor, intente nuevamente más tarde.';
      },
    }); 
  }
}
