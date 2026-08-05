import { AfterViewInit, Component, ComponentRef, Inject, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { global } from 'src/globals/global';
import { BaseEditComponent } from '../../../util/base-edit-component';
import { FilterList } from 'src/app/utils/filter-list';
import { MessageBox } from 'src/app/pages/message-box/message.box';

@Component({
  selector: 'app-rule-edit',
  templateUrl: './rule-edit.component.html',
  styleUrls: ['./rule-edit.component.scss'], 
  encapsulation: ViewEncapsulation.None
})
export class RuleEditComponent extends BaseEditComponent implements OnInit, AfterViewInit {
  app:any = global;
  conditionalCode:any;
  trueAction:any;
  falseAction:any;
  beforeAction:any;
  afterAction:any;


  form: FormGroup;
  trueActionFilter:FilterList = new FilterList('name');
  falseActionFilter:FilterList = new FilterList('name');
  beforeActionFilter:FilterList = new FilterList('name');
  afterActionFilter:FilterList = new FilterList('name');
  iterateFilter:FilterList = new FilterList('name');
  meetFilter:FilterList = new FilterList('name');

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
            private dialogRef: MatDialogRef<RuleEditComponent>,
            public messageBox: MessageBox,
            private fb: FormBuilder) { 
              super(data);
  }

  ngOnInit() {
    this.form = this.fb.group({
      name: [{value: this.item.name, disabled: !this.item.isNew}],
      description: [{value: this.item.description, disabled: !this.item.editable}], 
      position: [{value: this.item.position, disabled: !this.item.editable}], 
      initial: [{value: this.item.initial, disabled: !this.item.editable}],       
      iterate:[{value: this.getItem('iterate', 'name', 'AGENTS'), disabled: !this.item.editable}], 
      meet:[{value: this.getItem('meet', 'name', 'AGENTS'), disabled: !this.item.editable}], 
      trueActionName: [{value: this.trueAction = this.getItem('trueActionName', 'name', 'ACTIONS'), disabled: true}], 
      falseActionName: [{value: this.falseAction = this.getItem('falseActionName', 'name', 'ACTIONS'), disabled: true}], 
      beforeActionName: [{value: this.trueAction = this.getItem('beforeActionName', 'name', 'ACTIONS'), disabled: true}], 
      afterActionName: [{value: this.falseAction = this.getItem('afterActionName', 'name', 'ACTIONS'), disabled: true}], 
    });
    this.trueActionFilter.init(this.hsArray['ACTIONS'], this.form.get('trueActionName'));
    this.falseActionFilter.init(this.hsArray['ACTIONS'], this.form.get('falseActionName'));
    this.beforeActionFilter.init(this.hsArray['ACTIONS'], this.form.get('beforeActionName'));
    this.afterActionFilter.init(this.hsArray['ACTIONS'], this.form.get('afterActionName'));
    this.iterateFilter.init(this.hsArray['AGENTS'], this.form.get('iterate'));
    this.meetFilter.init(this.hsArray['AGENTS'], this.form.get('meet'));
    this.conditionalCode = {code: this.item.conditionalCode ? this.item.conditionalCode : '', type:'action', indent: 0};
    this.iterateChange()
    this.initialChange()
  }
  ngAfterViewInit(): void {
  }
  initialChange():void{
    let initial:boolean = this.form.get("initial").value
    if (initial){
      this.form.get("meet").disable()
      this.form.get("meet").reset()
    }
    else{
      this.form.get("meet").enable()
    }
  }
  changeName(){
    if (this.item.isNew){
      if (!this.trueAction){
        this.form.get("trueActionName").setValue(this.trueAction  = {name: '', isNew:false});
        this.form.get("falseActionName").setValue(this.falseAction  = {name: '', isNew:false});
        this.form.get("beforeActionName").setValue(this.beforeAction  = {name: '', isNew:false});
        this.form.get("afterActionName").setValue(this.afterAction  = {name: '', isNew:false});
      }
      let name:string = this.form.get("name").value;
      if (name && name != ''){  
        this.trueAction.name = 'True Action ' + name
        this.falseAction.name = 'False Action ' + name
        this.beforeAction.name = 'Before Action ' + name
        this.afterAction.name = 'After Action ' + name
      }
      else{
        this.trueAction.name = ''
        this.falseAction.name = ''
        this.beforeAction.name = ''
        this.afterAction.name = ''
      }
    }
  }
  iterateChange(){
    this.item.editable && this.form.get('iterate').value ? this.form.get('meet').enable() : this.form.get('meet').disable()
  }

  isFormValid(){
    return this.form.valid && this.conditionalCode.code.length > 0
  }
  save(){
    let value:any = this.item;
    for(var e in this.form.value){
      value[e] = this.form.value[e];
    }
    value['conditionalCode'] = this.conditionalCode.code;
    if (!this.validate(value)){
      return;
    }
    if (this.item.isNew){
      if (this.hsArray['ACTIONS'].indexOf(this.trueAction) == -1){
        this.hsArray['ACTIONS'].push(this.trueAction)
        this.hsArray['ACTIONS'].push(this.falseAction)  
        this.hsArray['ACTIONS'].push(this.beforeAction)  
        this.hsArray['ACTIONS'].push(this.afterAction)  
      }
      value['trueActionName'] = this.trueAction['name']
      value['falseActionName'] = this.falseAction['name']
      value['beforeActionName'] = this.beforeAction['name']
      value['afterActionName'] = this.afterAction['name']
    }
    if (value.initial == undefined){
      value.initial = false
    }
    value['iterate'] = value['iterate'] ? value['iterate']['name'] : null
    value['meet'] = value['meet'] ? value['meet']['name'] : null
    value.isNew = false
    this.dialogRef.close(value);
    
  }

}
