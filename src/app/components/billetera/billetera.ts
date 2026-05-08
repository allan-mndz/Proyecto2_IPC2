import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Proyecto } from '../../services/proyecto/proyecto';  

@Component({
  selector: 'app-billetera',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './billetera.html',
  styleUrl: './billetera.css',
})

export class Billetera implements OnInit {
  usuarioActual: any;
  saldoActual: number = 0;
  montoRecarga: number | null = null;
  mensaje: string = '';

  constructor(private proyectoService: Proyecto, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    const datos = localStorage.getItem('usuario');
    if (datos) {
      this.usuarioActual = JSON.parse(datos);
      this.cargarSaldo();
    }
  }

  cargarSaldo(): void {
    this.proyectoService.obtenerSaldo(this.usuarioActual.idUsuario, this.usuarioActual.tipoUsuario).subscribe({
      next: (res) => {
        this.saldoActual = res.saldo;
        this.cdr.detectChanges(); 
      },
      error: (err) => console.error('Error al obtener saldo', err)
    });
  }

  realizarRecarga(): void {
    if (!this.montoRecarga || this.montoRecarga <= 0) {
      this.mensaje = 'Ingresa un monto válido mayor a Q0.';
      return;
    }

    const paquete = {
      idUsuario: this.usuarioActual.idUsuario,
      monto: this.montoRecarga
    };

    this.proyectoService.recargarSaldo(paquete).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.mensaje = `¡Recarga de Q${this.montoRecarga} exitosa!`;
          this.montoRecarga = null;
          this.cargarSaldo(); // Actualizamos el saldo en pantalla
          
        } else {
          this.mensaje = 'Error al procesar la recarga.';
        }
      },
      error: (err) => {
        this.mensaje = 'Error de conexión con el servidor.';
      }
    });
  }

  esCliente(): boolean {
    return this.usuarioActual?.tipoUsuario === 'CLIENTE';
  }
} 
