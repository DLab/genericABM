import { TableDefinitionFilterComponent } from './table-definition-filter/table-definition-filter.component';
import { TableDefinitionEditComponent } from './table-definition-edit/table-definition-edit.component';
import { TableDefinitionRoutingModule } from './table-definition-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableDefinitionComponent } from './table-definition.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/@fury/shared/material-components.module';
import { FurySharedModule } from 'src/@fury/fury-shared.module';
import { MantenedorBaseModule } from '../mantenedor-base/mantenedor-base.module';
import { ListModule } from 'src/@fury/shared/list/list.module';

@NgModule({
  imports: [
    CommonModule
  , TableDefinitionRoutingModule
  , ReactiveFormsModule
  , MaterialModule
  , FormsModule
  , FurySharedModule
  , MantenedorBaseModule
  , ListModule

  ],
  declarations: [TableDefinitionComponent, TableDefinitionEditComponent, TableDefinitionFilterComponent]
})
export class TableDefinitionModule { }
