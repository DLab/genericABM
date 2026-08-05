import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Constantes, global } from 'src/globals/global';
import { BaseEditComponent } from '../../../util/base-edit-component';
import { FilterList } from 'src/app/utils/filter-list';
import { MessageBox } from 'src/app/pages/message-box/message.box';

@Component({
  selector: 'app-selected-properties',
  templateUrl: './selected-properties.component.html',
  styleUrls: ['./selected-properties.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SelectedPropertiesComponent extends BaseEditComponent implements OnInit {
  app:any = global;

  type:any;
  types:any[];
  properties:any[];
  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { 
              super(data);
  }

  ngOnInit() {
    this.type = {};
    this.types = [];
    this.properties = [];
    let arr:any[] = [];
    arr.push(...this.item.variables);
    arr.push(...this.item.functions);
    console.log(this.item.name)
    this.types.push({id: 0, code: this.item.name, properties: arr});
    let id:number = 0;
    this.item.agents.forEach(e => {
      if (!e.functions){
        e.functions = e.additionaCode ? JSON.parse(e.additionaCode) : [];
      }
      arr = [];
      arr.push(...e.variables);
      arr.push(...e.functions);
      this.types.push({id:++id, code: e.name, properties: arr});
    });
  }
  changeType(){
    if (!this.type && this.type != 0){
      this.properties = [];
    }
    else{
      this.properties = this.types[this.type].properties
    }
  }
  selectedItem(item:any){
    this.parent.selectedProperty(this.type, item);
  }


}