import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { global } from 'src/globals/global';
import { BaseEditComponent } from '../../../util/base-edit-component';
import { FilterList } from 'src/app/utils/filter-list';
import { MessageBox } from 'src/app/pages/message-box/message.box';

@Component({
  selector: 'app-graph-edit',
  templateUrl: './graph-edit.component.html',
  styleUrls: ['./graph-edit.component.scss']
})
export class GraphEditComponent  extends BaseEditComponent implements OnInit {
  app:any = global;

  form: FormGroup;
  graphTypeFilter:FilterList = new FilterList('description');
  graphTopologyFilter:FilterList = new FilterList('description');
  graphAlgorithmFilter:FilterList = new FilterList('description');

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
            private dialogRef: MatDialogRef<GraphEditComponent>,
            public messageBox: MessageBox,
            private fb: FormBuilder) { 
              super(data);
  }

  ngOnInit() {
    this.form = this.fb.group({
      name: [{value: this.item.name, disabled: !this.item.isNew}],
      graphTypeId: [{value: this.getItem('graphTypeId', 'id', 'GRAPH_TYPES'), disabled: !this.item.editable}], 
      graphTopologyId: [{value: this.getItem('graphTopologyId', 'id', 'GRAPH_TOPOLOGYS'), disabled: !this.item.editable}], 
      graphAlgorithmId: [{value: this.getItem('graphAlgorithmId', 'id', 'GRAPH_ALGORITHMS'), disabled: !this.item.editable}], 
    });
    this.graphTypeFilter.init(this.hsArray['GRAPH_TYPES'], this.form.get('graphTypeId'));
    this.graphTopologyFilter.init(this.hsArray['GRAPH_TOPOLOGYS'], this.form.get('graphTopologyId'));
    this.graphAlgorithmFilter.init(this.hsArray['GRAPH_ALGORITHMS'], this.form.get('graphAlgorithmId'));
  }

  isFormValid(){
    return this.form.valid
  }
  save(){
    let value:any = this.item;
    for(var e in this.form.value){
      value[e] = this.form.value[e];
    }
    value['graphTypeId'] = value['graphTypeId']['id'];
    value['graphTopologyId'] = value['graphTopologyId']['id'];
    value['graphAlgorithmId'] = value['graphAlgorithmId']['id'];
    if (!this.validate(value)){
      return;
    }
    value.isNew = false
    this.dialogRef.close(value);
    
  }

}
