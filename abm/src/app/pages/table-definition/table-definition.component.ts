import { formatDateObject } from 'src/app/utils/date-format/date-format.directive';
import { Component, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ComponentType } from 'ngx-toastr';
import { ListColumn } from 'src/@fury/shared/list/list-column.model';
import { global } from 'src/globals/global';
import { MantenedorBaseComponent } from '../mantenedor-base/mantenedor-base.component';
import { formatDate } from '@angular/common';
import { TableDefinitionEditComponent } from './table-definition-edit/table-definition-edit.component';
import { TableDefinitionFilterComponent } from './table-definition-filter/table-definition-filter.component';

@Component({
  selector: 'app-table-definition',
  templateUrl: './table-definition.component.html',
  styleUrls: ['./table-definition.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TableDefinitionComponent implements OnInit {
  app:any = global;

  type:string;
  query:string;
  title:string;

  filterClass:any;
  parentId:number = null;

  @Output() multiplesParametros:any = {codigoPadre: ['TABLE_TYPES']};
  @Output() editDialog:ComponentType<any> = TableDefinitionEditComponent;
  @Output() columns: ListColumn[] = [
    { name: this.app.screen.Id, property: 'id', visible: true, isModelProperty: true },
    { name: this.app.screen.Descripcion, property: 'description', visible: true, isModelProperty: true },
    { name: this.app.screen.Valid, property: 'valid', visible: true, isModelProperty: true, type: 'CHECKED' },
    { name: this.app.screen.Timestamp, property: 'timestamp', visible: true, isModelProperty: true },
    { name: this.app.screen.Accion, property: 'accion', visible: true},
  ] as ListColumn[];

  @ViewChild(MantenedorBaseComponent, { static: true }) mantenedor: MantenedorBaseComponent;

  constructor(private actRoute: ActivatedRoute) { 
    this.type = actRoute.snapshot.data.type;
    if (this.type == 'TableType'){
      this.query = 'tableTypeQuery'      
    }
    else{
      this.query = 'tableQuery'
      this.filterClass = TableDefinitionFilterComponent;
    }
    global.currentComponent = this;
    this.title = global.selectedMenuItem ? global.selectedMenuItem.name : '';
  }

  ngOnInit() {
  }
  @Output() newItem():any{
    return {isNew:true
      , editable: true
      , valid: true
      , parentId: this['parent'].parentId
      }
  }
  @Output() processRow(row:any, hsArray:any):void{
    row['timestamp'] = formatDate(row['timestamp'], 'yyyy/MM/dd HH:mm:ss', global.currentLocale);
  }
  isValidCreate(){
    return this['parent'].type == 'TableType' || this['parent'].parentId;
  }

}
