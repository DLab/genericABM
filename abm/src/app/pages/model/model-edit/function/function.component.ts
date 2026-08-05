import { AfterViewInit, Component, Inject, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { ListColumn } from 'src/@fury/shared/list/list-column.model';
import { MessageBox } from 'src/app/pages/message-box/message.box';
import { BaseEditComponent } from 'src/app/pages/util/base-edit-component';
import { FilterList } from 'src/app/utils/filter-list';
import { ParameterComponent } from './Parameter/Parameter.component';
import { global } from 'src/globals/global';
import { MatMenuTrigger } from '@angular/material/menu';
import { ActionBase } from '../action-base';

@Component({
  selector: 'app-function',
  templateUrl: './function.component.html',
  styleUrls: ['./function.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class FunctionComponent extends ActionBase implements OnInit, AfterViewInit {

  app:any = global;

  form: FormGroup;
  resultTypeFilter:FilterList = new FilterList('description');

  parameterDataSource: MatTableDataSource<any> | null;
  parameterColumns: ListColumn[] = [
    { name: global.screen['Name'], property: 'name', visible: true, isModelProperty: true }
  , { name: global.screen['Tipo'], property: 'typeId', visible: true, isModelProperty: true }
  , { name: global.screen['Accion'], property: 'accion', visible: true},
  ] as ListColumn[];

  @ViewChild(MatMenuTrigger) contextMenu: MatMenuTrigger;  
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
            private dialogRef: MatDialogRef<FunctionComponent>,
            private fb: FormBuilder,
            public messageBox: MessageBox,
            public dialog: MatDialog) { 
              super(data);
  }

  ngOnInit() {
    this.form = this.fb.group({
      name: [{value: this.item.name, disabled: !this.item.isNew}],
      aliasName: [{value: this.item.aliasName, disabled: !this.item.editable}], 
      resultTypeId: [{value: this.getItem('resultTypeId', 'id', 'VARIABLE_TYPES'), disabled: !this.item.editable}], 
    });
    this.resultTypeFilter.init(this.hsArray['VARIABLE_TYPES'].filter(e=>e.id < 4 || e.id == 7), this.form.get('resultTypeId'));    
    this.parameterDataSource = new MatTableDataSource();    
    this.parameterDataSource.data = this.item.parameters;
  }
  ngAfterViewInit(): void {
    //console.log('create::')
  }
  selectedTabChange(event:any){
    if (event.index == 1){
      this.actionContainer = document.getElementById('action-container');
    }
    else{
      this.actionContainer = null;
    }
  }
  changeName(){
    let alias:any = this.form.get('aliasName');
    if (!alias.value){
      alias.setValue(this.form.get('name').value);
    }
  }

  getParameterDescription(row:any, column:ListColumn){
    let value:any = row[column.property]
    if (value && (column.property == 'typeId')){
      if (row['agentType']){
        return row['agentType']
      }
      return this.hsArray['VARIABLE_TYPES'].find(e=>e.id == value)['description']
    }
    return value;
  }
  addParameter(){
    this.editParameter({isNew:true}, true);
  }
  editParameter(param:any, editable:boolean){
    param.hsArray = this.hsArray
    param.hsArray['AGENTS'] = this.item.model.agents
    param.editable = editable
    param.all = this.item.parameters
    param.model = this.item.model
    this.dialog.open(ParameterComponent, {
      data: {data: param, parent: this},
      height: '210px', width: '800px'
    }).afterClosed().subscribe(result => {
      if (result != undefined && result != true && this.item.parameters.indexOf(result) == -1){
        this.item.parameters.push(result)
        this.parameterDataSource.data = this.item.parameters;
      }
      delete param.hsArray
      delete param.all
      delete param.model
    });

  }    
  deleteParameter(row:any){
    this.parameterDataSource.data = this.deleteItem(row, this.item.parameters)
  }
  get parameterVisibleColumns() {
    return this.parameterColumns.filter(column => column.visible).map(column => column.property);
  }

  isFormValid(){
    return this.form.valid
  }
  save(){
    let value:any = this.item;
    for(var e in this.form.value){
      value[e] = this.form.value[e];
    }
    value.code = JSON.stringify(this.actions)
    value['resultTypeId'] = value['resultTypeId']['id'];
    value.isNew = false
    this.dialogRef.close(value);
    
  }

}
