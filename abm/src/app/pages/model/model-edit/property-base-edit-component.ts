import { MatTableDataSource } from "@angular/material/table";
import { BaseEditComponent } from "../../util/base-edit-component";
import { ListColumn } from "../../../../@fury/shared/list/list-column.model";
import { PropertyEditComponent } from "./property-edit/property-edit.component";
import { MatDialog } from "@angular/material/dialog";
import { Constantes, global } from "src/globals/global";
import { FunctionComponent } from "./function/function.component";

export class PropertyBaseEditComponent extends BaseEditComponent {

    editTitle:string;
    isAgent:boolean;
    propertiesDataSource: MatTableDataSource<any> | null;
    propertiesColumns: ListColumn[] = [
      { name: global.screen['Name'], property: 'name', visible: true, isModelProperty: true }
    , { name: global.screen['AliasName'], property: 'aliasName', visible: true, isModelProperty: true }
    , { name: global.screen['Tipo'], property: 'variableTypeId', visible: true, isModelProperty: true }
    , { name: global.screen['RangeValues'], property: 'rangeValues', visible: true, isModelProperty: true , type: 'CHECKED' }
    , { name: global.screen['DetailedAnalysis'], property: 'detailedAnalysis', visible: true, isModelProperty: true, type: 'CHECKED' }
    , { name: global.screen['GeneralAnalysis'], property: 'generalAnalysisId', visible: true, isModelProperty: true}
    , {/* reference property*/}
    , { name: global.screen['InitialValue'], property: 'initialValue', visible: true, isModelProperty: true }
    , { name: global.screen['FunctionType'], property: 'functionTypeId', visible: true, isModelProperty: true }
    , { name: global.screen['MemoryType'], property: 'memoryTypeId', visible: false, isModelProperty: true }
    , { name: global.screen['Table_Type'], property: 'tableId', visible: false, isModelProperty: true }
    , { name: global.screen['Accion'], property: 'accion', visible: true},
    ] as ListColumn[];

    functionsDataSource: MatTableDataSource<any> | null;
    functionsColumns: ListColumn[] = [
      { name: global.screen['Name'], property: 'name', visible: true, isModelProperty: true }
    , { name: global.screen['AliasName'], property: 'aliasName', visible: true, isModelProperty: true }  
    , { name: global.screen['Parameters'], property: 'parameters', visible: true, isModelProperty: true }
    , { name: global.screen['Result'], property: 'resultTypeId', visible: true, isModelProperty: true }
    , { name: global.screen['Accion'], property: 'accion', visible: true},
    ] as ListColumn[];
    
    //functions:any[];

    constructor(public data: any, public dialog: MatDialog, isAgent:boolean) { 
        super(data);
        this.propertiesDataSource = new MatTableDataSource();
        this.isAgent = isAgent;
        this.editTitle = this.isAgent ? 'Site' : 'Observable'
        
        this.functionsDataSource = new MatTableDataSource();    
        this.item.functions = this.item.additionalCode ? JSON.parse(this.item.additionalCode) : [];
        this.functionsDataSource.data = this.item.functions;
    
        if (this.editTitle == 'Site'){
          this.propertiesColumns[6] = { name: global.screen['Reference'], property: 'modelNameRef', visible: true, isModelProperty: true } as ListColumn
        }
        else{
          this.propertiesColumns[6] = { name: global.screen['Referenced'], property: 'isRef', visible: true, isModelProperty: true , type: 'CHECKED' } as ListColumn
        }
        if (this.item.kqmlIntegration || (this.item.model && this.item.model.kqmlIntegration)){
          this.propertiesColumns.splice(6, 0, { name: global.screen['Kqml'], property: 'kqmlIntegration', visible: true, isModelProperty: true, type: 'CHECKED' } as ListColumn);
        }
    }
    modelChangeKqmlProperty(){
        if (this.item.kqmlIntegration){
          this.propertiesColumns.splice(6, 0, { name: global.screen['Kqml'], property: 'kqmlIntegration', visible: true, isModelProperty: true, type: 'CHECKED' } as ListColumn);
        }
        else{
          this.propertiesColumns.splice(6, 1)
        }
    }
    getPropertyDescription(row:any, column:ListColumn){
        let value:any = row[column.property]
        if (value && (column.property == 'variableTypeId' || column.property == 'memoryTypeId')){
          return this.hsArray['VARIABLE_TYPES'].find(e=>e.id == value)['description']
        }
        else if (value && column.property == 'initialValue'){
          let type:number = row['variableTypeId']
          return type == 3 ? Constantes.booleanArrays.find(e=>e.id == value)['description'] :value
        }
        else if (value && column.property == 'functionTypeId'){
          return this.hsArray['FUNCTION_TYPES'].find(e=>e.id == value)['description']
        }
        else if (value && column.property == 'tableId'){
          return this.hsArray['TABLE_TYPES'].find(e=>e.id == value)['description']
        }
        else if (value && column.property == 'generalAnalysisId'){
          return this.hsArray['GENERAL_ANALYSIS_TYPE'].find(e=>e.id == value)['description']
        }
          return value;
      }
      addProperty(){
        this.editProperty({isNew:true}, true);
      }
      editProperty(property:any, editable:boolean){
        property.hsArray = this.hsArray
        property.editable = editable
        property.all = this.item.variables
        property.model = this.editTitle == 'Site' ? this.item.model : this.item;
        this.dialog.open(PropertyEditComponent, {
          data: {data: property, parent: this, title: this.editTitle},
          height: '305px', width: '800px'
        }).afterClosed().subscribe(result => {
          if (result != undefined && result != true && this.item.variables.indexOf(result) == -1){
            this.item.variables.push(result)
            this.propertiesDataSource.data = this.item.variables;
          }
          delete property.hsArray
          delete property.all
          delete property.model;
        });
    
      }    
      refreshModelProperties(){
        if (this.editTitle == 'Site'){
          this.parent['refreshModelProperties']();
        }
        else{
          this.propertiesDataSource.data = this.item.variables;
        }
        
      }
      deleteProperty(row:any){
        if (this.isAgent && row['modelNameRef']){
          let ref:any = this.item.model.variables.find(e=>e.name == row['modelNameRef'])
          this.deleteItem(ref, this.item.model.variables)  
          this.refreshModelProperties()
        }
        else if (!this.isAgent && row['isRef'] == true){
          this.item.agents.forEach(agent => {
            agent.variables.forEach(e => {
              if (e['modelNameRef'] == row['name']){
                this.deleteItem(e, agent.variables)  
              }
            });
          });
        }
        this.propertiesDataSource.data = this.deleteItem(row, this.item.variables)
      }
      get propertiesVisibleColumns() {
        return this.propertiesColumns.filter(column => column.visible).map(column => column.property);
      }
    
      getFunctionDescription(row:any, column:ListColumn){
        let value:any = row[column.property]
        if (value && column.property == 'parameters'){
          let s:string = '';
          let sep:string = '';
          value.forEach(e => {
            s += sep + e.name;
            sep = ', ';
          });
          return s;
        }
        else if (value && column.property == 'resultTypeId'){
          return this.hsArray['VARIABLE_TYPES'].find(e=>e.id == value)['description']
        }
        return value;
      }
      addFunction(){
        this.editFunction({isNew:true, parameters:[]}, true);
      }
      editFunction(fn:any, editable:boolean){
        fn.hsArray = this.hsArray
        fn.editable = editable
        fn.all = this.item.functions
        fn.model = this.item;
        this.dialog.open(FunctionComponent, {
          data: {data: fn, parent: this},
          height: '800px', width: '1200px'
        }).afterClosed().subscribe(result => {
          if (result != undefined && result != true && this.item.functions.indexOf(result) == -1){
            this.item.functions.push(result)
            this.functionsDataSource.data = this.item.functions;
          }
          delete fn.hsArray
          delete fn.all
          delete fn.model
    
        });
      }
      deleteFunction(row:any){
        this.functionsDataSource.data = this.deleteItem(row, this.item.functions)
      }
    
      get functionVisibleColumns() {
        return this.functionsColumns.filter(column => column.visible).map(column => column.property);
      }      
}
