import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { BaseService } from 'src/app/base.service';
import { FilterList } from 'src/app/utils/filter-list';
import { BaseEditComponent } from '../../util/base-edit-component';
import { formatDate } from '@angular/common';
import { global } from 'src/globals/global';

@Component({
  selector: 'app-table-definition-edit',
  templateUrl: './table-definition-edit.component.html',
  styleUrls: ['./table-definition-edit.component.scss']
})
export class TableDefinitionEditComponent extends BaseEditComponent implements OnInit {

  form: FormGroup;
  title:string;
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
            private dialogRef: MatDialogRef<TableDefinitionEditComponent>,
            private fb: FormBuilder,
            private baseService: BaseService) { 
        
      super(data);

  }

  ngOnInit() {
    this.title = this.item.parentId ? global.screen['Record'] : global.screen['Table'];
    this.form = this.fb.group({
      id: [{value: this.item.id, disabled: true}],
      valid: [{value: this.item.valid, disabled: !this.item.editable}],
      description: [{value: this.item.description, disabled: !this.item.editable}], 
    });
  }

  save() {
    let value:any = {};
    for(var e in this.form.value){
      value[e] = this.form.value[e];
    }
    value.id = value.id || this.item.id;
    value.isNew = this.item.isNew;
    value.parentId = this.item.parentId;

    this.baseService.tableSave(value).subscribe(result=>{
      result['timestamp'] = formatDate(result['timestamp'], 'yyyy/MM/dd HH:mm:ss', global.currentLocale);
      this.dialogRef.close(result);
    });
  }

}
