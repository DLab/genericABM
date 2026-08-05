import { BaseEditComponent } from "../../util/base-edit-component";

export class ActionBase extends BaseEditComponent {

    actions:any[];
    contextMenuPosition = { x: '0px', y: '0px' };  
    actionContainer:any;
    additionalCodeHeight:string = '60px';


    constructor(public data: any) { 
        super(data);
        if (this.item.code){
            this.actions = JSON.parse(this.item.code)
          }
          else{
            this.actions = []
            this._addItem(null, 'action');
          }
      
    }
  
    codeChange(){
        if (this.actionContainer){
            this.additionalCodeHeight = (this.actionContainer.offsetHeight + 40) + 'px'
          }
    }

    indent(){
        let n:number = 0;
        for (let index = 0; index < this.actions.length; index++) {
          const item:any = this.actions[index];
          item.indent = n;
          if (item.type == 'If'){
            n++;
          }
          else if (item.type == 'Else'){
            item.indent = n - 1;
          }
          else if (item.type == 'EndIf'){
            n--;
            item.indent = n;
          }
          
        }
      }
      getMaxIndex(){
        let max:number = 0;
        this.actions.forEach(action => {
          
          let n:number = parseInt(action.fieldName.substring(16))
          if (n > max){
            max = n
          }
        });
        return max;
      }
      _addItem(item:any, type:string){
        let index:number = item ? this.actions.indexOf(item) + 1: 0
        this.actions.splice(index, 0, {code:'', fieldName: 'math-field-name-' + (this.getMaxIndex() + 1), type:type, indent:0})
        this.indent();
      }
      addItem(event:any){
        this.openMenu(event.event, event.item);
      }
      removeItem(item:any){
        let index:number = this.actions.indexOf(item)
        this.actions.splice(index, 1)
        this.indent();
        let ctr:any = this;
        setTimeout(() => {
            ctr.codeChange();    
        }, 100);
      }
      openMenu(event:any, item:any){
        event.preventDefault(); 
        this.contextMenuPosition.x = event.clientX + 'px';
        this.contextMenuPosition.y = event.clientY + 'px';
        this['contextMenu'].menuData = { 'item': item };
        this['contextMenu'].menu.focusFirstItem('mouse');
        this['contextMenu'].openMenu();
      }  
      isValidActions(){
        for (let index = 0; index < this.actions.length; index++) {
          if (this.actions[index].code && this.actions[index].code.length > 0){
            return true;
          }      
        }
        return false;
      }    
}
