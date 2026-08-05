import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TableDefinitionComponent } from './table-definition.component';

const routes: Routes = [
  {
    path: '',
    component: TableDefinitionComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TableDefinitionRoutingModule {
}
