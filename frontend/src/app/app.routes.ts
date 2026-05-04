import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Dashboard } from './components/dashboard/dashboard';
import { authGuard } from './guards/auth-guard';
import { RegistroUsuarios } from './components/registro-usuarios/registro-usuarios'; 
import { PublicarProyecto } from './components/publicar-proyecto/publicar-proyecto';     
import { BuscarTrabajos } from './components/buscar-trabajos/buscar-trabajos'; 
import { MisPublicaciones } from './components/mis-publicaciones/mis-publicaciones';
import { MisContratos } from './components/mis-contratos/mis-contratos';

export const routes: Routes = [

    { path: '', redirectTo: '/login', pathMatch: 'full' },

    { path: 'login', component: Login },

    { path: 'registro', component: RegistroUsuarios },

    { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },

    { path: 'publicar-proyecto', component: PublicarProyecto },

    { path: 'buscar-trabajos', component: BuscarTrabajos },

    { path: 'mis-publicaciones', component: MisPublicaciones },

    { path: 'mis-contratos', component: MisContratos},

    {path: '**', redirectTo: '/login'},
    
];
