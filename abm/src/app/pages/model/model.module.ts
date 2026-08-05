import { ParameterComponent } from './model-edit/function/Parameter/Parameter.component';
import { FunctionComponent } from './model-edit/function/function.component';
import { CodeEditorComponent } from './model-edit/code-editor/code-editor.component';
import { SelectedPropertiesComponent } from './model-edit/selected-properties/selected-properties.component';
import { GraphEditComponent } from './model-edit/graph-edit/graph-edit.component';
import { PropertyEditComponent } from './model-edit/property-edit/property-edit.component';
import { ActionEditComponent } from './model-edit/action-edit/action-edit.component';
import { RuleEditComponent } from './model-edit/rule-edit/rule-edit.component';
import { AgentEditComponent } from './model-edit/agent-edit/agent-edit.component';
import { ModelEditComponent } from './model-edit/model-edit.component';
import { ModelRoutingModule } from './model-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModelComponent } from './model.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/@fury/shared/material-components.module';
import { FurySharedModule } from 'src/@fury/fury-shared.module';
import { MantenedorBaseModule } from '../mantenedor-base/mantenedor-base.module';
import { ListModule } from 'src/@fury/shared/list/list.module';
import { FuryCardModule } from 'src/@fury/shared/card/card.module';
import { MatTabsModule } from '@angular/material/tabs';
import { NumberFormatModule } from 'src/app/utils/number-format/number-format.module';
import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
  imports: [
       CommonModule 
     , ModelRoutingModule
     , ReactiveFormsModule
     , MaterialModule
     , FormsModule
     , FurySharedModule
     , MantenedorBaseModule
     , ListModule
     , FuryCardModule
     , MatTabsModule
     , NumberFormatModule
     , NgSelectModule

  ], 
  declarations: [ModelComponent, ModelEditComponent, AgentEditComponent
              , RuleEditComponent, ActionEditComponent, PropertyEditComponent
              , GraphEditComponent, SelectedPropertiesComponent, CodeEditorComponent
              , FunctionComponent, ParameterComponent]
})
export class ModelModule { }
