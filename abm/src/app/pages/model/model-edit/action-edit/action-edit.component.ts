import { ActionBase } from './../action-base';
import { AfterViewInit, Component, Inject, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { global } from 'src/globals/global';
import { BaseEditComponent } from '../../../util/base-edit-component';
import { FilterList } from 'src/app/utils/filter-list';
import { MatMenuTrigger } from '@angular/material/menu';
import { MessageBox, MessageBoxType } from 'src/app/pages/message-box/message.box';

@Component({
  selector: 'app-action-edit',
  templateUrl: './action-edit.component.html',
  styleUrls: ['./action-edit.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ActionEditComponent extends ActionBase implements OnInit, AfterViewInit {
  app:any = global;

  form: FormGroup;
  @ViewChild(MatMenuTrigger) contextMenu: MatMenuTrigger;
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
            private dialogRef: MatDialogRef<ActionEditComponent>,
            private messageBox: MessageBox,
            private fb: FormBuilder) { 
              super(data);
  }

  ngOnInit() {
    this.form = this.fb.group({
      name: [{value: this.item.name, disabled: !this.item.isNew}],
      description: [{value: this.item.description, disabled: !this.item.editable}], 
    });
  }
  ngAfterViewInit(): void {
    this.actionContainer = document.getElementById('action-container');
  }
  isFormValid(){
    return this.form.valid;// && this.isValidActions()
  }
  save(){
    if (this.actions[this.actions.length - 1].indent > 0){
      this.messageBox.showMessageBox(MessageBoxType.Error, this.app.screen['Missing_a_closing_condition']);
      return;
    }
    let value:any = this.item;
    for(var e in this.form.value){
      value[e] = this.form.value[e];
    }
    value.code = JSON.stringify(this.actions)
    delete value.hsArray;
    value.isNew = false
    this.dialogRef.close(value);
    
  }

}
