import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Dashboard } from './components/dashboard/dashboard';
import { authGuard } from './guards/auth-guard';
import { RegistroUsuarios } from './components/registro-usuarios/registro-usuarios'; 
import { PublicarProyecto } from './components/publicar-proyecto/publicar-proyecto';     
import { BuscarTrabajos } from './components/buscar-trabajos/buscar-trabajos'; 
import { MisPublicaciones } from './components/mis-publicaciones/mis-publicaciones';
import { MisContratos } from './components/mis-contratos/mis-contratos';
import { Billetera } from './components/billetera/billetera';   
import { ReportesCliente } from './components/reportes-cliente/reportes-cliente';
import { AdminUsuarios } from './components/admin-usuarios/admin-usuarios';
import { AdminCategorias } from './components/admin-categorias/admin-categorias';
import { AdminHabilidadesComponent } from './components/admin-habilidades/admin-habilidades';
import { AdminComisionComponent } from './components/admin-comision/admin-comision';
import { AdminReportesComponent } from './components/admin-reportes/admin-reportes';
import { CompletarPerfilComponent } from './components/completar-perfil/completar-perfil';
import { ReportesFreelancer } from './components/reportes-freelancer/reportes-freelancer'; 

export const routes: Routes = [

    { path: '', redirectTo: '/login', pathMatch: 'full' },

    { path: 'login', component: Login },

    { path: 'registro', component: RegistroUsuarios },

    { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },

    { path: 'publicar-proyecto', component: PublicarProyecto },

    { path: 'buscar-trabajos', component: BuscarTrabajos },

    { path: 'mis-publicaciones', component: MisPublicaciones },

    { path: 'mis-contratos', component: MisContratos},

    { path : 'billetera', component: Billetera},

    { path: 'reportes-cliente', component: ReportesCliente },

    { path: 'admin-usuarios', component: AdminUsuarios },

    { path: 'admin-categorias', component: AdminCategorias },

    { path: 'admin-habilidades', component: AdminHabilidadesComponent },

    { path: 'admin-comision', component: AdminComisionComponent },

    { path: 'admin-reportes', component: AdminReportesComponent },

    { path: 'completar-perfil', component: CompletarPerfilComponent }, 

    { path: 'reportes-freelancer', component: ReportesFreelancer },

    {path: '**', redirectTo: '/login'},
    
];
