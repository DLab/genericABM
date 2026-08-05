import { startWith, map } from 'rxjs/operators';
import { AbstractControl } from '@angular/forms';
import { Observable } from 'rxjs';


export class FilterList{

  public filteredItems: Observable<any[]>;
  private items: any[] = [];
  private control:AbstractControl;
  private property:string;
  constructor(property?:string){
    if (property == null){
      this.property = 'descripcion';
    }
    this.property = property;
  }

  init(items:any[], control:AbstractControl):void{
    this.items = items; 
    this.control = control;
    control['filterList'] = this;
    this.filteredItems = this.control.valueChanges.pipe(
      startWith<string | any>(''),
      map(value=> typeof value === 'string' ? value : value == null ? null : value[this.property]),
      map(item => item ? this.filterItem(item) : this.items.slice())
    );
  }

  public displayItem(item:any):string{    
    return item ? (item.description ? item.description : item.name ? item.name : '') : '';
  }


  private filterItem(text: string):any[] {
    text = text.toLowerCase();
    return this.items.filter(item =>
      (typeof item === 'string' ? item : item[this.property]).toLowerCase().indexOf(text) === 0);
  }
  public getItems():any[]{
    return this.items;
  }

}