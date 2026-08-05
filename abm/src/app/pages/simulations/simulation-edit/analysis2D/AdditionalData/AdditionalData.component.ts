import { Component, Inject, OnInit, Optional, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { BaseService } from 'src/app/base.service';
import { FilterList } from 'src/app/utils/filter-list';
import { global } from 'src/globals/global';

@Component({
  selector: 'app-AdditionalData',
  templateUrl: './AdditionalData.component.html',
  styleUrls: ['./AdditionalData.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AdditionalDataComponent implements OnInit {
  app:any = global;
  form: FormGroup;
  items:any[];
  filters:any = {};

  constructor(@Optional() @Inject(MAT_DIALOG_DATA) public data: any
          , private dialogRef: MatDialogRef<AdditionalDataComponent>
          , private fb: FormBuilder
          , private baseService: BaseService) { 
    this.items = data.items;
  }

  ngOnInit() {
    let fields: any = {};
    this.items.forEach(item=>{
      item.additionalData.forEach(e => {
        let name:string = item.name + '_' + e.name;
        fields[name] = [{value: e.type == 'list' ? e.options.find(ee=> ee.name == e.defaultValue) : e.defaultValue, disabled: false}];
      });
    });
    this.form = this.fb.group(fields);   
    this.items.forEach(item=>{
      item.additionalData.forEach(e => {
        if (item.type = 'list'){
          let name:string = item.name + '_' + e.name;
          this.filters[name] = new FilterList('name');
          this.filters[name].init(e.options, this.form.get(name));
        }
      });
    });
  }
  isFormValid()
  {
    return this.form.valid;
  }
  getValue(type:string, decimals:number, value:any){
    if (type == 'number' && typeof(value) == 'string'){
      if (decimals > 0){
        value = value.replace(',', '.')
      }
      value = Number(value)
    }
    else if (type == 'list'){
      if (value['name'])
        value = value['name'];
    }
    return value;
  }
  accept()
  {
    let data:any = this.form.value;
    let result:any[] = [];
    this.items.forEach(item=>{
      let ritem:any[] = [];
      result.push(ritem);
      item.additionalData.forEach(e => {
        let value:any = this.getValue(e.type, e.decimals, data[item.name + '_' + e.name])
        e.defaultValue = value;
        ritem.push(value)
      });
    });
    this.dialogRef.close(result);
  }
}
