import { Component, ChangeDetectorRef} from '@angular/core';
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

  constructor(private authService: Auth, private router: Router, private cdr: ChangeDetectorRef) {}

  onLogin(): void {
    this.errorMessage = ''; 

    if (!this.username || !this.password) {
      this.errorMessage = 'Por favor, ingrese su nombre de usuario y contraseña.';
      this.cdr.detectChanges(); 
      return;
    }

    this.authService.login(this.username, this.password).subscribe({
      next: (response: any) => { 
        console.log('Login exitoso:', response);
        alert(`¡Bienvenido ${response.usuario.nombreCompleto}!`);

        localStorage.setItem('token', response.token);
        localStorage.setItem('usuario', JSON.stringify(response.usuario));
        
        
        localStorage.setItem('perfilCompletado', String(response.perfilCompletado));

        if (response.perfilCompletado) {
          this.router.navigate(['/dashboard']);
        } else {
          this.router.navigate(['/completar-perfil']);
        }
      },
      error: (err) => {
        console.error('Error durante el login:', err);
        this.errorMessage = 'Ocurrió un error durante el login. Por favor, intente nuevamente más tarde.';
        this.cdr.detectChanges(); 
      },
    }); 
  }
}