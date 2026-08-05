import { Component, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { ListColumn } from 'src/@fury/shared/list/list-column.model';
import { global } from 'src/globals/global';
import { MantenedorBaseComponent } from '../mantenedor-base/mantenedor-base.component';
import { formatDate } from '@angular/common';
import { ComponentType } from 'ngx-toastr';
import { ModelEditComponent } from './model-edit/model-edit.component';

@Component({
  selector: 'app-model',
  templateUrl: './model.component.html',
  styleUrls: ['./model.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ModelComponent implements OnInit {
  app:any = global;

  @Output() multiplesParametros:any = {codigoPadre: ['TABLE_TYPES', 'ALL_TABLES', 'GRAPH_TYPES'
                                                  , 'FUNCTION_TYPES', 'VARIABLE_TYPES'
                                                  , 'GRAPH_TOPOLOGYS', 'GRAPH_ALGORITHMS', 'GENERAL_ANALYSIS_TYPE']};
  @Output() editDialog:ComponentType<any> = ModelEditComponent;
  @Output() columns: ListColumn[] = [
    { name: this.app.screen.Name, property: 'name', visible: true, isModelProperty: true },
    { name: this.app.screen.Descripcion, property: 'description', visible: true, isModelProperty: true },
    { name: this.app.screen.RunWith, property: 'runWith', visible: true, isModelProperty: true },
    { name: this.app.screen.Privated, property: 'private', visible: true, isModelProperty: true, type: 'CHECKED' },
    { name: this.app.screen.Activated, property: 'activate', visible: true, isModelProperty: true, type: 'CHECKED' },
    { name: this.app.screen.Kqml, property: 'kqmlIntegration', visible: true, isModelProperty: true, type: 'CHECKED' },
    { name: this.app.screen.Synchronized, property: 'synchronized', visible: true, isModelProperty: true, type: 'CHECKED' },
    { name: this.app.screen.Timestamp, property: 'timestamp', visible: true, isModelProperty: true },
    { name: this.app.screen.User, property: 'userId', visible: true, isModelProperty: true },
    { name: this.app.screen.Accion, property: 'accion', visible: true},
  ] as ListColumn[];

  @ViewChild(MantenedorBaseComponent, { static: true }) mantenedor: MantenedorBaseComponent;
  constructor() { 
    global.currentComponent = this
  }

  ngOnInit() {
  }
  @Output() newItem():any{
    return {isNew:true
      , editable: true
      , activate: false
      , private: true
      , agents:[]
      , rules:[]
      , actions: []
      , properties:[]
      , variables: []
      , graphs: []
      , userId: global.user
      }
  }
  @Output() processRow(row:any, hsArray:any):void{
    row['timestamp'] = formatDate(row['timestamp'], 'yyyy/MM/dd HH:mm:ss', global.currentLocale);
  }
  getEditDialogSize(){
    return {height: '800px', width: '1500px'}
  }
  hasCommand(action:string, row:any){
    if (action === 'Editar' || action === 'Eliminar'){
      return row.userId == global.user;
    }
    return action != 'Agregar';
  }

}
