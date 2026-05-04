import { Component } from '@angular/core';
import { MenuSuperior } from '../menu-superior/menu-superior'; 

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MenuSuperior],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {}
