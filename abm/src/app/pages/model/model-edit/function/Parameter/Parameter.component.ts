import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MessageBox } from 'src/app/pages/message-box/message.box';
import { BaseEditComponent } from 'src/app/pages/util/base-edit-component';
import { FilterList } from 'src/app/utils/filter-list';
import { global } from 'src/globals/global';

@Component({
  selector: 'app-Parameter',
  templateUrl: './Parameter.component.html',
  styleUrls: ['./Parameter.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ParameterComponent extends BaseEditComponent implements OnInit {

  app:any = global;

  form: FormGroup;
  typeFilter:FilterList = new FilterList('description');
  agentTypeFilter:FilterList = new FilterList('name');


  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
            private dialogRef: MatDialogRef<ParameterComponent>,
            private fb: FormBuilder,
            public messageBox: MessageBox,
            public dialog: MatDialog) { 
              super(data);
  }

  ngOnInit() {
    this.form = this.fb.group({
      name: [{value: this.item.name, disabled: !this.item.isNew}],
      typeId: [{value: this.getItem('typeId', 'id', 'VARIABLE_TYPES'), disabled: !this.item.editable}], 
      agentType: [{value: this.getItem('agentType', 'name', 'AGENTS'), disabled: !this.item.editable}], 
    });
    this.typeFilter.init(this.hsArray['VARIABLE_TYPES'].filter(e=>e.id < 4 || e.id > 7), this.form.get('typeId'));
    this.agentTypeFilter.init(this.hsArray['AGENTS'], this.form.get('agentType'));
  }

  isFormValid(){
    return this.form.valid
  }
  save(){
    let value:any = this.item;
    for(var e in this.form.value){
      value[e] = this.form.value[e];
    }
    value['typeId'] = value['typeId']['id'];
    value['agentType'] = value['typeId'] == 8 && value['agentType'] ? value['agentType']['name'] : null;
    if (!this.validate(value)){
      return;
    }
    value.isNew = false
    this.dialogRef.close(value);
    
  }

}
