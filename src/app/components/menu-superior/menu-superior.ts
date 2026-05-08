import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Proyecto } from '../../services/proyecto/proyecto';

@Component({
  selector: 'app-menu-superior',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './menu-superior.html',
  styleUrl: './menu-superior.css',
})

export class MenuSuperior implements OnInit {
  nombreUsuarioActual: string = '';
  rolActual: string = '';
  idUsuarioActual: number = 0;
  saldoActual: number = 0;

  constructor(private navegador: Router, private proyectoService: Proyecto) {}

  ngOnInit(): void {
    const datosUsuario = localStorage.getItem('usuario');
    if (datosUsuario) {
      const usuarioObj = JSON.parse(datosUsuario);
      this.nombreUsuarioActual = usuarioObj.nombreCompleto;
      this.rolActual = usuarioObj.tipoUsuario;
      this.idUsuarioActual = usuarioObj.idUsuario;

      this.cargarSaldo();

      this.proyectoService.saldoActualizado$.subscribe(() => {
        this.cargarSaldo();
      });
    }
  }

  cargarSaldo(): void {
    if (this.rolActual !== 'ADMIN' && this.rolActual !== 'ADMINISTRADOR') {
      this.proyectoService.obtenerSaldo(this.idUsuarioActual, this.rolActual).subscribe({
        next: (res) => {
          this.saldoActual = res.saldo;
        },
        error: (err) => console.error('Error al cargar saldo', err)
      });
    }
  }

  salirDelSistema(): void {
    localStorage.removeItem('usuario');
    localStorage.removeItem('token');
    this.navegador.navigate(['/login']);
  }
}
