import { filter } from 'rxjs/operators';
import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Constantes, global } from 'src/globals/global';
import { BaseEditComponent } from '../../../util/base-edit-component';
import { FilterList } from 'src/app/utils/filter-list';
import { MessageBox, MessageBoxType } from 'src/app/pages/message-box/message.box';
import { number } from 'echarts';

@Component({
  selector: 'app-property-edit',
  templateUrl: './property-edit.component.html',
  styleUrls: ['./property-edit.component.scss'],
  encapsulation:ViewEncapsulation.None
})
export class PropertyEditComponent extends BaseEditComponent implements OnInit {
  app:any = global;

  title:string;
  form: FormGroup;
  isAgent:boolean;
  variableTypeFilter:FilterList = new FilterList('description');
  booleanFilter:FilterList = new FilterList('description');
  functionTypeFilter:FilterList = new FilterList('description');
  memoryTypeFilter:FilterList = new FilterList('description');
  tableTypeFilter:FilterList = new FilterList('description');
  tableFilter:FilterList = new FilterList('description');
  generalAnalysisTypeFilter:FilterList = new FilterList('description');

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
            private dialogRef: MatDialogRef<PropertyEditComponent>,
            public messageBox: MessageBox,
            private fb: FormBuilder) { 
              super(data);
      this.title  = data.title;
      this.isAgent = this.title == 'Site';
  }

  ngOnInit() {
    let initialValue:any = this.getInitialValue()
    this.form = this.fb.group({
      name: [{value: this.item.name, disabled: !this.item.editable || this.item.isRef}],
      aliasName: [{value: this.item.aliasName, disabled: !this.item.editable || this.item.isRef}],
      rangeValues: [{value: this.item.rangeValues, disabled: !this.item.editable}],
      detailedAnalysis: [{value: this.item.detailedAnalysis, disabled: !this.item.editable}],
      kqmlIntegration: [{value: this.item.kqmlIntegration, disabled: !this.item.editable}],
      generalAnalysisId: [{value: this.getItem('generalAnalysisId', 'id', 'GENERAL_ANALYSIS_TYPE'), disabled: !this.item.editable}],
      initialValue: [{value: initialValue ? initialValue[0] : null, disabled: !this.item.editable}],
      variableTypeId: [{value: this.getItem('variableTypeId', 'id', 'VARIABLE_TYPES'), disabled: !this.item.editable}], 
      functionTypeId: [{value: this.getItem('functionTypeId', 'id', 'FUNCTION_TYPES'), disabled: !this.item.editable}], 
      memoryTypeId: [{value: this.getItem('memoryTypeId', 'id', 'VARIABLE_TYPES'), disabled: !this.item.editable}],       
      tableId: [{value: this.getItem('tableId', 'id', 'TABLE_TYPES'), disabled: !this.item.editable}],       
      mean: [{value: initialValue ? initialValue[0] : null, disabled: !this.item.editable}],       
      stdDev: [{value: initialValue ? initialValue[1] : null, disabled: !this.item.editable}],       
    });
    this.variableTypeFilter.init(this.hsArray['VARIABLE_TYPES'].filter(e=>e.id < 7), this.form.get('variableTypeId'));
    this.booleanFilter.init(Constantes.booleanArrays, this.form.get('initialValue'));
    this.functionTypeFilter.init(this.hsArray['FUNCTION_TYPES'], this.form.get('functionTypeId'));
    this.memoryTypeFilter.init(this.hsArray['VARIABLE_TYPES'], this.form.get('memoryTypeId'));
    this.tableTypeFilter.init(this.hsArray['TABLE_TYPES'], this.form.get('tableId'));
    this.generalAnalysisTypeFilter.init(this.hsArray['GENERAL_ANALYSIS_TYPE'], this.form.get('generalAnalysisId'));
    this.tableTypeChange();
  }
  keyDown(event:KeyboardEvent){
    if (!((event.key >= 'a' && event.key <= 'z') || (event.key >= 'A' && event.key <= 'Z') || (event.key >= '0' && event.key <= '9'))){
      event.stopImmediatePropagation ();
      event.preventDefault();
    }
  }
  getInitialValue(){
    let value:any = this.item.initialValue;
    if (value){
      
      if (this.item.variableTypeId == 3){
        return Constantes.booleanArrays.find(e=>e.id == value);
      }
      else if (this.item.variableTypeId == 4){
        return this.hsArray['ALL_TABLES'].find(e=>e.id == value);
      }
      else {
        return value.split(';')
      }
    }
    return value;
  }
  tableTypeChange(){
    let tableId:any = this.form.get('tableId').value;
    if (tableId){
      this.tableFilter.init(this.hsArray['ALL_TABLES'].filter(e=>e.parentId == tableId.id), this.form.get('initialValue'));
    }
    else{
      this.tableFilter.init([], this.form.get('initialValue'));
    }
  }
  isFormValid(){
    return this.form.valid
  }
  changeName(){
    let alias:any = this.form.get('aliasName');
    if (!alias.value){
      alias.setValue(this.form.get('name').value);
    }
  }
  addModelPropertyRef(value:any){
    let modelNameRef:string = value['modelNameRef']
    let name:string = value['name']
    let nameRef:string = 'init' + name.charAt(0).toUpperCase() + name.substring(1);
    console.log('addmodelpropertyref', this.item)
    for (let index = 0; index < this.item.model.variables.length; index++) {
      const e = this.item.model.variables[index];
      if (e['name'] == nameRef){
        console.log('ref:', e, nameRef)
        if (!e['isRef']){
          e['isRef'] = true
          this.messageBox.showMessageBox(MessageBoxType.Ok, global.getScreen('The_property_of_the_model_has_been_automatically_referenced', nameRef))
        }   
        return null;
      }
    };
    let modelValue:any = modelNameRef ? this.item.model.variables.find(e=>e.name == modelNameRef) : {isNew:true};
    for(let e in value){
      if (e != 'isNew'){
        modelValue[e] = value[e]
      }
    }
    value['modelNameRef'] = nameRef
    modelValue['name'] = nameRef;
    modelValue['aliasName'] = nameRef;
    modelValue['isRef'] = true
    if (modelValue.isNew){
      this.item.model.variables.push(modelValue);
    }
    delete modelValue.isNew
    this.parent['refreshModelProperties']();
    delete modelValue.hsArray
    delete modelValue.all
    delete modelValue.model;    
    return nameRef;
  }
  removeModelPropertyRef(value:any){
    let aliasNameRef:string = value['modelNameRef']
    delete value['modelNameRef']
    let prop:any = this.item.model.variables.find(e=>e.name == aliasNameRef);
    if (prop){
      this.deleteItem(prop, this.item.model.variables)
      this.parent['refreshModelProperties']();
    }
  }
  removeZero(value:string){
    let i:number = value.indexOf(',')
    if (i != -1){
      value = value.replace(',', '.')
      return String(Number(value)).replace('.', ',')
    }
    return value;
  }
  changeCode(code:string, oldAliasName:string, aliasName:string){
    let s:string = '';
    let word:string = '';
    let i:number = 0
    let n:number = code.length;
    while(i < n){
      let c:string = code.charAt(i++)      
      if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')){
        word = word + c;
      }
      else{
        if (word != ''){
          if (word == oldAliasName){
            s = s + aliasName
          }
          else{
            s = s + word
          }
          word = ''
        }
        s = s + c
      }
    }
    s = s + word
    return s;
  }
  changeAliasName(oldAliasName:string, aliasName:string){
    let actions:any[] = this.item.model.actions
    actions.forEach(e=>{
      if (e.code){
        let rows:any[] = JSON.parse(e.code)
        console.log('actrionss:', e.code, rows)
        rows.forEach(r=>{
          r.code = this.changeCode(r.code, oldAliasName, aliasName);
        })
        e.code = JSON.stringify(rows)
      }
    })
    this.item.model.rules.forEach(e=>{
      if (e.conditionalCode){
        e.conditionalCode = this.changeCode(e.conditionalCode, oldAliasName, aliasName);
      }
    })
    this.item.model.functions.forEach(e=>{
      if (e.code){
        let rows:any[] = JSON.parse(e.code)
        rows.forEach(r=>{
          r.code = this.changeCode(r.code, oldAliasName, aliasName);
        })
        e.code = JSON.stringify(rows)
      }
    })
  }
  save(){
    let aliasName:string = this.item.aliasName;
    let rangeValues:boolean = this.item.rangeValues;
    let value:any = this.item;
    for(var e in this.form.value){
      value[e] = this.form.value[e];
    }
    value['variableTypeId'] = value['variableTypeId']['id'];
    value['functionTypeId'] = value['functionTypeId'] ? value['functionTypeId']['id'] : null;
    value['memoryTypeId'] = value['memoryTypeId'] ? value['memoryTypeId']['id'] : null;
    value['tableId'] = value['tableId'] ? value['tableId']['id'] : null;
    value['generalAnalysisId'] = value['generalAnalysisId'] ? value['generalAnalysisId']['id'] : null;
    if (value['rangeValues'] == undefined){
      value['rangeValues'] = false;
    }
    
    if (value['kqmlIntegration'] == undefined){
      value['kqmlIntegration'] = false;
    }
     
    if (value['initialValue']){
      if (value['variableTypeId'] == 3 ){
        value['initialValue'] = value['initialValue']['id'] + ''
      }
    }
    if (value['variableTypeId'] == 5 && value['mean']){
      value['initialValue'] = this.removeZero(value['mean']) + ';' + this.removeZero(value['stdDev'])
    }
    if (!this.validate(value)){
      return;
    }
    value.isNew = false
    let nameRef:string;
    if (this.isAgent && rangeValues && !value['rangeValues']){
      this.removeModelPropertyRef(value)
    }
    else if (this.isAgent && value['rangeValues'] && (nameRef = this.addModelPropertyRef(value))){
      this.messageBox.showMessageBox(MessageBoxType.Ok, global.getScreen('The_variable_has_been_automatically_generated_for_the_model', nameRef))
    }
    else if (!this.isAgent && value['isRef']){
      value['rangeValues'] = true;
    }
    if (aliasName && aliasName != value.aliasName){
      console.log(this.item.model)
      this.changeAliasName(aliasName, value.aliasName)
    }
    this.dialogRef.close(value);
    
  }

}
