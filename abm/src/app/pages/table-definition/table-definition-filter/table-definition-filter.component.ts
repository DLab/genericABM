import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { FilterList } from 'src/app/utils/filter-list';
import { global } from 'src/globals/global';
import { BaseEditComponent } from '../../util/base-edit-component';
import { TableDefinitionComponent } from '../table-definition.component';

@Component({
  selector: 'app-table-definition-filter',
  templateUrl: './table-definition-filter.component.html',
  styleUrls: ['./table-definition-filter.component.scss']
})
export class TableDefinitionFilterComponent extends BaseEditComponent implements OnInit {
  app:any = global;

  form: FormGroup;
  filterTables:FilterList = new FilterList('description');
  constructor(private fb: FormBuilder) { 
    super({data:{}, parent:null});
  }

  ngOnInit() {
    this.form = this.fb.group({
      parentId: [{value: null, disabled: false}],
    });
  }
  setParent(parent: TableDefinitionComponent): void {
    this.parent = parent;
  }
  getParameters():any {
    this.parent.parentId = this.form.get('parentId').value['id'];
    return { parentId: this.parent.parentId}
  }
  parentChange(){
    if (this.form.get('parentId').value){
      this['search']()
    }
    else{
      this.parent.parentId = null;
    }
    
  }
  isValid(){
    return this.form.valid;
  }
  setArrays(hsArrays: any): void {
    this.item.hsArray = hsArrays;
    this.filterTables.init(this.item.hsArray['TABLE_TYPES'], this.form.get('parentId'));    
  }
  visibleSearch(){
    return false;
  }

}
