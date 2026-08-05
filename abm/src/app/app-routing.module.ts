import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CargaMasivaType } from 'src/globals/global';
import { LayoutComponent } from './layout/layout.component';


const routes: Routes = [
  {
    path: '', 
    loadChildren: () => import('./pages/authentication/login/login.module').then(m => m.LoginModule),
  },
  {
    path: 'register',
    loadChildren: () => import('./pages/authentication/register/register.module').then(m => m.RegisterModule),
  },
  {
    path: 'forgot-password',
    loadChildren: () => import('./pages/authentication/forgot-password/forgot-password.module').then(m => m.ForgotPasswordModule),
  },
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: 'presentacion',
        loadChildren: () => import('./pages/presentacion/presentacion.module').then(m => m.PresentacionModule)
      },
      {
        path: 'administracion/usuarios',
        loadChildren: () => import('./pages/administracion/usuarios/usuarios.module').then(m => m.UsuariosModule)
      },
      {
        path: 'administracion/roles',
        loadChildren: () => import('./pages/administracion/roles/roles.module').then(m => m.RolesModule)
      },
      {
        path: 'process/simulations',
        loadChildren: () => import('./pages/simulations/simulations.module').then(m => m.SimulationsModule)
      },
      {
        path: 'configuration/table_types',
        data:{type: 'TableType'},
        loadChildren: () => import('./pages/table-definition/table-definition.module').then(m => m.TableDefinitionModule)
      },
      {
        path: 'configuration/tables',
        data:{type: 'Tables'},
        loadChildren: () => import('./pages/table-definition/table-definition.module').then(m => m.TableDefinitionModule)
      },
      {
        path: 'configuration/models',
        data:{type: 'Tables'},
        loadChildren: () => import('./pages/model/model.module').then(m => m.ModelModule)
      }

    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    initialNavigation:  'enabledNonBlocking',
    // preloadingStrategy: PreloadAllModules,
    scrollPositionRestoration: 'enabled',
    anchorScrolling: 'enabled',
    relativeLinkResolution: 'legacy'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
