import { AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { SelectedPropertiesComponent } from '../selected-properties/selected-properties.component';
import { MatDialog } from '@angular/material/dialog';
import { global } from 'src/globals/global';

@Component({
  selector: 'app-code-editor',
  templateUrl: './code-editor.component.html',
  styleUrls: ['./code-editor.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CodeEditorComponent implements OnInit, AfterViewInit {
  app:any = global;
  MQ:any = null;
  mathFieldSpan:any;
  mathField:any;
  mathFieldHeight:number = 50;
  @Input() mathFieldName:string = 'math-field';
  @Input() source:any;
  @Input() model:any;
  @Input() editable:boolean = true;
  @Input() addButtons:boolean = false;
  @Input() initializeTime:number = 0;
  @Output() codeChange:EventEmitter<any> = new EventEmitter<any>();
  @Output() addItem:EventEmitter<any> = new EventEmitter<any>();
  @Output() removeItem:EventEmitter<any> = new EventEmitter<any>();

  constructor(public dialog: MatDialog) { }

  ngOnInit() {
  }
  ngAfterViewInit(): void {
    if (this.source.type == 'Else' || this.source.type == 'EndIf'){
      return;
    }
    if (this.source.type == undefined){
      this.source.type = 'action'
    }
    let ctr:any = this;
    setTimeout(function(){
      ctr.initializeMathField();
    }, this.initializeTime);  
  }
  initializeMathField(){
    this.mathFieldSpan = document.getElementById(this.mathFieldName);
    this.MQ = (window as any).MathQuill.getInterface(2);
    let ctr:any = this;
    let isOk:boolean = false;
    //console.log('creoq:', this.mathFieldSpan)
    this.mathField = this.MQ.MathField(this.mathFieldSpan, {
      spaceBehavesLikeTab: false, // configurable
      supSubsRequireOperand: true,
      maxDepth: 1,
      handlers: {
        edit: (mathField) => {
          // useful event handlers
          ctr.source.code = mathField.latex()
          console.log('codechange:', mathField.latex())
          this.codeChange.emit(ctr.source)
          if (isOk){
            let height:number = ctr.mathField.__controller.container[0].children[1].offsetHeight;
            ctr.mathFieldHeight = height + 13;
          }
          if (!this.editable){
            ctr.mathField.__controller.container[0].children[0].children[0].setAttribute("disabled", "true")
          }
          
          /*if (this.removeCharPressed) {
            this.removeCharPressed = false;
            this.tempFunc();
          }*/
          // let offset = this.getCursorOffset(mathField)
          // console.log(offset)
        },
      },
    });
    let textArea = ctr.mathField.__controller.container[0].children[0].children[0]
    textArea.addEventListener("keyup", (event) => {
      if (event.key == 'Enter'){
        this.mathField.select()    
        document.execCommand('copy');
        navigator.clipboard.readText().then(text => {
          this.mathField.latex(text)
          console.log('Pasted content down: ', text);
        })
        .catch(err => {
          console.error('Failed to read clipboard contents: ', err);
        });        
}
    });
    this.mathField.latex(this.source.code)
    isOk = true;
  }
  showProperties(){
    this.dialog.open(SelectedPropertiesComponent, {
      data: {data: this.model, parent: this},
      panelClass: 'selected-properties',
      height: '600px' 
    });
  }
  addEditor(event:any){
    this.addItem.emit({event:event, item:this.source})
  }
  removeEditor(){
    this.removeItem.emit(this.source)
  }
  selectedProperty(type:number, item:any){
    if (type == 0){
      this.mathField.cmd(item.aliasName);
    }
    else{
      this.mathField.write(item.aliasName + "_i");
      this.mathField.keystroke('Enter');
      this.mathField.focus()
    }
  }
}
