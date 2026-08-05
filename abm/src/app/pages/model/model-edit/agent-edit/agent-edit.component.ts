import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { global } from 'src/globals/global';
import { FilterList } from 'src/app/utils/filter-list';
import { PropertyBaseEditComponent } from '../property-base-edit-component';
import { MessageBox } from 'src/app/pages/message-box/message.box';

@Component({
  selector: 'app-agent-edit',
  templateUrl: './agent-edit.component.html',
  styleUrls: ['./agent-edit.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AgentEditComponent extends PropertyBaseEditComponent implements OnInit {

  app:any = global;

  form: FormGroup;
  graphFilter:FilterList = new FilterList('name');
  extendsFromFilter:FilterList = new FilterList('name');

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
            private dialogRef: MatDialogRef<AgentEditComponent>,
            private fb: FormBuilder,
            public messageBox: MessageBox,
            public dialog: MatDialog) { 
              super(data, dialog, true);
  }

  ngOnInit() {
    this.form = this.fb.group({
      name: [{value: this.item.name, disabled: !this.item.isNew}],
      description: [{value: this.item.description, disabled: !this.item.editable}], 
      base: [{value: this.item.base, disabled: true}], 
      extendsFrom: [{value: this.getItem('extendsFrom', 'name', 'AGENTS'), disabled: !this.item.editable}], 
      graphName: [{value: this.getItem('graphName', 'name', 'GRAPHS'), disabled: !this.item.editable}],
    });
    this.item.variables.sort(function(a:any, b:any){return a.name < b.name ? -1 : a.name == b.name ? 0 : 1});
    this.graphFilter.init(this.hsArray['GRAPHS'], this.form.get('graphName'));
    this.extendsFromFilter.init(this.hsArray['AGENTS'], this.form.get('extendsFrom'));
    this.propertiesDataSource.data = this.item.variables;
  }

  isFormValid(){
    return this.form.valid
  }
  save(){
    let value:any = this.item;
    for(var e in this.form.value){
      value[e] = this.form.value[e];
    }
    value['additionalCode'] = JSON.stringify(this.item.functions);
    value['graphName'] = value['graphName'] ? value['graphName']['name'] : null;
    value['extendsFrom'] = value['extendsFrom'] ? value['extendsFrom']['name'] : null;
    if (!this.validate(value)){
      return;
    }
    value.isNew = false
    this.dialogRef.close(value);
    
  }

}
