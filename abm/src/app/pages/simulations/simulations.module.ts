import { AdditionalDataComponent } from './simulation-edit/analysis2D/AdditionalData/AdditionalData.component';
import { Analysis2DComponent } from './simulation-edit/analysis2D/analysis2D.component';
import { GraphComponentComponent } from './simulation-edit/detail-simulation-edit/graph-component/graph-component.component';
import { SettingsColorComponent } from './simulation-edit/detail-simulation-edit/settings-color/settings-color.component';
import { DetailSimulationEditComponent } from './simulation-edit/detail-simulation-edit/detail-simulation-edit.component';
import { SimulationEditComponent } from './simulation-edit/simulation-edit.component';
import { SimulationsRoutingModule } from './simulations-routing.module';
import { SimulationsComponent } from './simulations.component';
import { ListModule } from 'src/@fury/shared/list/list.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/@fury/shared/material-components.module';
import { FurySharedModule } from 'src/@fury/fury-shared.module';
import { MantenedorBaseModule } from '../mantenedor-base/mantenedor-base.module';
import { NumberFormatModule } from 'src/app/utils/number-format/number-format.module';
import { NgxEchartsModule } from 'ngx-echarts';
import { NgxSliderModule } from '@angular-slider/ngx-slider';
import { NgxMatColorPickerModule } from '@angular-material-components/color-picker';
import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
  imports: [
    CommonModule
    , ReactiveFormsModule
    , MaterialModule
    , FormsModule
    , SimulationsRoutingModule
    , FurySharedModule
    , MantenedorBaseModule
    , ListModule
    , NumberFormatModule
    , NgxSliderModule
    , NgxMatColorPickerModule
    , NgSelectModule

    , NgxEchartsModule.forRoot({
      echarts: () => import('echarts')
    })

  ],
  declarations: [SimulationsComponent
              , SimulationEditComponent
              , DetailSimulationEditComponent
              , SettingsColorComponent
              , GraphComponentComponent
              , Analysis2DComponent
              , AdditionalDataComponent]
})
export class SimulationsModule { }
