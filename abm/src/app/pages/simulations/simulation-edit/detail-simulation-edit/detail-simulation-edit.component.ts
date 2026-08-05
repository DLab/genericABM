import { global } from 'src/globals/global';
import { generateHexColors } from 'src/globals/colors';
import { Component, ElementRef, Inject, OnInit, Optional, ViewChild, ViewEncapsulation } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { MessageBox, MessageBoxType } from 'src/app/pages/message-box/message.box';
import { BaseService } from 'src/app/base.service';
import { formatDate } from '@angular/common';
import { SettingsColorComponent } from './settings-color/settings-color.component';
import { MatMenuTrigger } from '@angular/material/menu';



@Component({
  selector: 'app-detail-simulation-edit',
  templateUrl: './detail-simulation-edit.component.html',
  styleUrls: ['./detail-simulation-edit.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DetailSimulationEditComponent implements OnInit {

  app:any = global;

  ctr:any;
  item:any;
  title:string
  numSteps:number;
  caption:string;
  model:any;
  isNormal:boolean;
  itemsColors:any = {itemStyleColor : {}}
  optionsGraph:any[] = [];
  moveToGraphs:any[];
  contextMenuPosition = { x: '0px', y: '0px' };  
  analisysProperties:any[];

  @ViewChild(MatMenuTrigger) contextMenu: MatMenuTrigger;

  constructor(@Optional() @Inject(MAT_DIALOG_DATA) public data: any,
            private messageBox:MessageBox,
            private baseService: BaseService,
            private dialog: MatDialog) { 
    this.ctr = this;
    this.item = data.item
    this.model = data.model
    this.isNormal = data.isNormal == undefined ? true : data.isNormal;
    //console.log('yaaa', data)
    this.title = this.model.name + ' (' + this.app.screen.Detalle + ')';
    this.numSteps = this.item.model.numSteps

    this.caption = this.app.screen['NumAgents'] + ': ' + this.item['model']['numAgents']
                  + ", " + this.app.screen['NumSteps'] + ': ' + this.item['model']['numSteps'];

    this.analisysProperties = [];
    this.model.variables.forEach(e => {
      if (e.rangeValues){
        this.caption +=  ", " + e.name.charAt(0).toUpperCase() + e.name.substring(1) 
          + ': ' + (e.variableTypeId == 5 ? this.item['model'][e.name].params[0] : this.item['model'][e.name] )
      }
      if (e.detailedAnalysis){
        this.analisysProperties.push(e);
      }
    });
    this.model.agents.forEach(agent => {
      agent.variables.forEach(e => {
        if (e.detailedAnalysis){
          this.analisysProperties.push(e);
        }
      })        
    });
    console.log(this.model.agents, this.analisysProperties)
    //console.log('yaaaaa', data)
    if (!global.configurations[this.model.name]){
      let n:number = 0;
      let defaultColors:string[] = generateHexColors('Viridis', this.analisysProperties.length)
      //console.log(defaultColors)
      this.analisysProperties.forEach(e=>{
        this.itemsColors.itemStyleColor[e.name] = defaultColors[n]
        this.itemsColors.itemStyleColor[e.name + '_STD'] = defaultColors[n++]
      })
      
      global.configurations[this.model.name] = this.itemsColors;
    }
    else{
      this.itemsColors = global.configurations[this.model.name]
    }


  }

  ngOnInit() {
    this.optionsGraph = [{items:[0, 1, 2, 3, 4, 5, 6, 7]}]
  }

  getGraphImages(){
    let imgs:any[] = [];
    this.optionsGraph.forEach(e=>{
      imgs.push(e.ctr.graphEchart.getDataURL({type:'png', pixelRatio: 1, backgroundColor: '#fff'}));
    })
    return imgs
  }

  downloadChart(){
    let imgs:any[] = this.getGraphImages();
    let title:string = this.title
    this.baseService.downloadSimulation({data: this.item, img:imgs, title: title, caption: this.caption, detail: true, model: this.model, analisysProperties: this.analisysProperties}).subscribe((file:any)=>{
      const blob = new Blob([file], { type: 'application/zip' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      const fileName = title + this.item.index + '_' + formatDate(new Date(), 'yyyy/MM/dd/HH:mm:ss', global.currentLocale)+ '.zip';
      a.href = url;
      a.download =  fileName;
      a.click();
      window.URL.revokeObjectURL(url);
      this.messageBox.showMessageBox(MessageBoxType.Success, global.screen['Se_ha_generado_el_archivo_con_exito']);
    });

  }
  colorSettings(){
    this.dialog.open(SettingsColorComponent, {
      data: {model: this.model, analisysProperties: this.analisysProperties},
      width: '1400px'
    }).afterClosed().subscribe(result => {
      if (result && result != true){
        this.optionsGraph.forEach(e=>{
          e.ctr.selectedSeriesChange();
        })
      }
    });
  }
  openNewGraph(item:any){
    item.selected = false;
    item.ref.selected = false;
    item.ctr.selectedSeriesChange()
    this.optionsGraph.push({items:[item.index]});
    this.optionsGraph = this.optionsGraph;
  }

  moveToGraph(item:any, graph:any){
    item.selected = false;
    item.ref.selected = false;
    item.ctr.selectedSeriesChange()
    graph.items.push(item.index);
    graph.ctr.initGraph()
  }
  removeFromGraph(item:any){
    this.optionsGraph.splice(item.indexGraph, 1);
    this.optionsGraph = this.optionsGraph;
  }

  openMenu(event:any, index:number, item:any){
    item['indexGraph'] = index;
    event.preventDefault(); 
    this.moveToGraphs = [];
    if (index == 0){
      let n:number = 0;
      this.optionsGraph.forEach(e=>{
        if (n > 0){
          this.moveToGraphs.push(e);
        }
        n++;
      });      
    }
    this.contextMenuPosition.x = event.clientX + 'px';
    this.contextMenuPosition.y = event.clientY + 'px';
    this.contextMenu.menuData = { 'item': item };
    this.contextMenu.menu.focusFirstItem('mouse');
    this.contextMenu.openMenu();
  }

}