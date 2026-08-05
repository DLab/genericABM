import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { global } from 'src/globals/global';
import * as echarts from 'echarts';
import 'echarts-gl';
import { getAnaliticsOptions } from 'src/app/pages/dashboard/dashboard-util';
import { clone } from 'lodash-es';

@Component({
  selector: 'app-graph-component',
  templateUrl: './graph-component.component.html',
  styleUrls: ['./graph-component.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class GraphComponentComponent implements OnInit, AfterViewInit {
  app:any = global;

  chartClassName:string;
  graphEchart:any;

  logScale:boolean = false;
  _graph:any;
  _analisysProperties:any[];

  allOptions:any[];
  firstRow:any[];
  secondRow:any[];
  initialize:boolean = false
  @Input() pozo:string = 'mean'
  @Input() item:any;
  @Input() title:string
  @Input() numSteps:number;
  @Input() caption:string;
  @Input() parent:any;
  @Input() indexGraph:number;
  @Input() itemStyleColor:any;
  @Input() set analisysProperties(value:any[]){
    this._analisysProperties = value
    let index:number = 0;
    this.allOptions = []
    this._analisysProperties.forEach(e=>{
      let label:string = e.name.charAt(0).toUpperCase() + e.name.substring(1)
      this.allOptions.push({name:e.name, label:label, selected: true, std: false, row: 1, visible: true, index: index});
      this.allOptions.push({name:e.name, label:label + '(Std)', selected: false, std: true, row: 2, visible: true, index: index++});
    });
    if (this._graph){
      this.initGraph()  
    }
  }
  @Input() set graph(value:any){
    this._graph = value;
    value.ctr = this
    if (this.allOptions){
      this.initGraph()
    }
    
  }

  @ViewChild('graph', { static: true }) graphRef: ElementRef;

  constructor() { 

  }

  ngOnInit() {
    this.optionInit();
    this.chartClassName = 'long-demo-chart';
  }
  initGraph(){
    for (let n = 0; n < this.allOptions.length; n++) {
      this.allOptions[n].visible = this._graph.items.indexOf(this.allOptions[n].index) != -1;
      this.allOptions[n].selected = this.allOptions[n].visible && !this.allOptions[n].std;
      this.allOptions[n].ctr = this
    }
    if (this.initialize){
      this.optionInit()
      this.graphEchart.setOption(this.getOptionGraph());
    }
    
  }
  optionInit(){
    this.firstRow = this.allOptions.filter(e=> e.row == 1 && e.visible)
    this.secondRow = this.allOptions.filter(e=> e.row == 2 && e.visible)
    for (let n = 0; n < this.firstRow.length; n++) {
      this.firstRow[n].ref = this.secondRow[n];
      this.secondRow[n].ref = this.firstRow[n];
    }
  }
  ngAfterViewInit(): void {
    this.initialize = true;
    this.graphEchart = echarts.init(this.graphRef.nativeElement); 
    this.graphEchart.setOption(this.getOptionGraph());
  }
  selectedSeriesChange(){
    this.graphEchart.setOption(this.getOptionGraph());
  }
  changePozo(){
    this.graphEchart.setOption(this.getOptionGraph());
  }
  openMenu(event:any, item:any){
    this.parent.openMenu(event, this.indexGraph, item);
  }
  changeLogScale(){
    this.graphEchart.setOption(this.getOptionGraph());
  }

  getOptionGraph(){
    let series:any[] = [];
    let xaxis:any[] = [];
    this.graphEchart.clear()
    for (let index = 0; index < this.numSteps; index++) {
      xaxis.push(index)      
    }
    this.allOptions.forEach(e=>{
      if (e.selected && e.visible){
        let serie:any;
        if (e.std){
          let data:number[] = this.item[this.pozo][e.name]
          let std:number[] = this.item['stdDev'][e.name]
          let up:number[] = [];
          let down:number[] = [];
          for (let index = 0; index < this.numSteps; index++) {
            let d:number = data[index];
            up.push(d + std[index])
            down.push(d - std[index])  
          }
          let label:string = e.label
          series.push({type: "line",  symbol: 'none'
                  , lineStyle: {type: "dotted"}, name: '+' + label
                  , itemStyle:{color: this.itemStyleColor[e.name + '_STD']}, data: up})
          series.push({type: "line", symbol: 'none'
                  , lineStyle: {type: "dotted"}, name: '-' + label
                  , itemStyle:{color: this.itemStyleColor[e.name + '_STD']}, data: down})
        }
        serie = {type: "line", itemStyle:{}};
        serie['name'] = e.label
        serie['itemStyle']['color'] = this.itemStyleColor[e.name]
        serie['data'] = this.item[this.pozo][e.name]
        series.push(serie)  
        
      }
    });
    let option:any = getAnaliticsOptions(xaxis, series, this.title, this.caption, /*this.app.screen.Steps*/ 'Time Steps', 'Common'/*this.app.screen.Value*/);
    if (this.logScale){
      option['yAxis']['type'] = 'log';
    }
    return option;

  }  
}
