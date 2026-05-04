import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

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

  constructor(private navegador: Router) {}

  ngOnInit(): void {
    const datosUsuario = localStorage.getItem('usuario');
    if (datosUsuario) {
      const usuarioObj = JSON.parse(datosUsuario);
      this.nombreUsuarioActual = usuarioObj.nombreCompleto;
      this.rolActual = usuarioObj.tipoUsuario;
    }
  }

  salirDelSistema(): void {
    localStorage.removeItem('usuario');
    localStorage.removeItem('token');
    this.navegador.navigate(['/login']);
  }
}
