import { BaseComponent } from './base-component';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Inject } from '@angular/core';
import { global } from "src/globals/global";
import { MessageBoxType } from '../message-box/message.box';

export class BaseEditComponent extends BaseComponent {

    gl:any = global;
    item:any;
    parent:any;
    hsArray:any;
    constructor(@Inject(MAT_DIALOG_DATA) public data: any) { 
      super();
      this.item = data.data;
      this.parent = data.parent;
      this.hsArray = this.item.hsArray;
    }
  
    getItem(property:string, codeProperty:string, listName:string, item?: any):any{
      return this._getItem(property, codeProperty, this.hsArray[listName], item);
    }
    _getItem(property:string, codeProperty:string, arr:any[], item?:any):any{
      item = item ? item : this.item;
      if (item[property] && item[property] != null){
        const value:any = item[property];
        return arr.find(o=>o[codeProperty] == value);
      }
      return null;
    }
    deleteItem(item:any, list:any[]){
      let index:number = list.indexOf(item);
      if (index != -1){
        list.splice(index, 1)
      }
      return list;
    }

    validate(newItem:any, property:string = 'name'){
      if (newItem.isNew){
        let list:any[] = this.item.all ? this.item.all : this.item.searchResult;
        let find:any = list.find(e=>e[property].toUpperCase() == newItem[property].toUpperCase());
        if (find){
          this['messageBox'].showMessageBox(MessageBoxType.Ok, global.getScreen('Record_already_exists_in_the_list', property, newItem[property]));
          return false;
        }
      }
      return true;
    }
  }
  