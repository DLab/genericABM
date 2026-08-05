import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BaseService } from 'src/app/base.service';
import { MessageBox, MessageBoxType } from 'src/app/pages/message-box/message.box';
import { BaseEditComponent } from 'src/app/pages/util/base-edit-component';
import { FilterList } from 'src/app/utils/filter-list';
import { getFormatNumber } from 'src/app/utils/number-format/number-format.directive';
import { global } from 'src/globals/global';
import * as echarts from 'echarts';
import 'echarts-gl';
import { MatDialog } from '@angular/material/dialog';
import { AdditionalDataComponent } from './AdditionalData/AdditionalData.component';
import { generateHexColors } from 'src/globals/colors';
import * as ecStat from 'echarts-stat';

const DataZoom:any = [
  {
    textStyle: {
      color: '#8392A5'
    },
    bottom: "15",
    handleIcon:
      'path://M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
    dataBackground: {
      areaStyle: {
        color: '#8392A5'
      },
      lineStyle: {
        opacity: 0.8,
        color: '#8392A5'
      }
    },
    brushSelect: true
  },
  {
    type: 'inside'
  }
];


@Component({
  selector: 'app-analysis2D',
  templateUrl: './analysis2D.component.html',
  styleUrls: ['./analysis2D.component.scss']
})

export class Analysis2DComponent extends BaseEditComponent implements OnInit {
  app:any = global;

  analysisForm:FormGroup;
  normalityAnalysisMethodFilter:FilterList = new FilterList('name');
  linearityAnalysisMethodFilter:FilterList = new FilterList('name');
  dimensionalityReductionMethodFilter:FilterList = new FilterList('name');
  clusteringMethodFilter:FilterList = new FilterList('name');
  savedAnalysisResultsFilter:FilterList = new FilterList('description');

  selectedGraphType:string;


  @Input() chartClassName:string;
  @Input() fileNameResult:string;
  @Input() _item:any;
  @Input() _hsArray:any;
  @Input() graph2DProperties:any;
  @Input() isSimulationOk:boolean = false;

  @ViewChild('graph2d', { static: true }) graph2dRef: ElementRef;
  private graph2DEchart:any = null;
  private graph2DData:any = {min: 0, max: 0, title: 'Untitle', xAxisName: 'X', yAxisName: 'Y'};

  constructor(private messageBox:MessageBox,
    private baseService: BaseService,
    private fb: FormBuilder,
    private dialog: MatDialog) { 
      super({data:{}});
    }

  ngOnInit() {
    let _ecStat:any = ecStat
    echarts.registerTransform(_ecStat.transform.clustering);
    //console.log(this.graph2DProperties)
    this.item = this._item
    this.hsArray = this._hsArray
    //console.log(this.item)
    this.analysisForm = this.fb.group({
      clusteringMethod: [{value: this.getItem('clusteringMethod', 'name', 'CLUSTERING_METHODS'), disabled: !this.item.editable}], 
      normalityAnalysisMethod: [{value: this.getItem('normalityAnalysisMethod', 'name', 'NORMALITY_ANALYSIS_METHODS'), disabled: !this.item.editable}], 
      linearityAnalysisMethod: [{value: this.getItem('linearityAnalysisMethod', 'name', 'LINEARITY_ANALYSIS_METHODS'), disabled: !this.item.editable}], 
      dimensionalityReductionMethod: [{value: this.getItem('dimensionalityReductionMethod', 'name', 'DIMENSIONALITY_REDUCTION_METHODS'), disabled: !this.item.editable}], 
      savedAnalysisResults: [{value: null, disabled: !this.item.editable}], 
    });

    //this.clusteringMethodFilter.init(this.hsArray['CLUSTERING_METHODS'].filter(e=> e.name != this.item.name), this.analysisForm.get('clusteringMethod'));
    this.normalityAnalysisMethodFilter.init(this.hsArray['NORMALITY_ANALYSIS_METHODS'].filter(e=> e.name != this.item.name), this.analysisForm.get('normalityAnalysisMethod'));
    this.linearityAnalysisMethodFilter.init(this.hsArray['LINEARITY_ANALYSIS_METHODS'].filter(e=> e.name != this.item.name), this.analysisForm.get('linearityAnalysisMethod'));
    //this.dimensionalityReductionMethodFilter.init(this.hsArray['DIMENSIONALITY_REDUCTION_METHODS'].filter(e=> e.name != this.item.name), this.analysisForm.get('dimensionalityReductionMethod'));
    this.savedAnalysisResultsFilter.init(this.item.isNew ? [] : this.item.analysisResults, this.analysisForm.get('savedAnalysisResults'));
    if (!this.item.isNew){
      this.validateAnalysisResult(false);
    }
    
    this.normalityAnalysisChange()
    this.linearityAnalysisChange()
  }
  savedAnalysisResultsChange(){
    let analysisResult:any = this.analysisForm.get('savedAnalysisResults').value;
    if (analysisResult == null){
      return;
    }
    if (analysisResult.idState == 3){
      this.messageBox.showMessageBox(MessageBoxType.Question, global.screen['Desea_eliminar_el_registro_definitivamente']).subscribe(value=>{
        if (value === 'SI'){
          this.baseService.deleteAnalysisResult(analysisResult).subscribe(resp=>{
            this.validateAnalysisResult(true);
          });
        }
      });
      return;
    }
    this.baseService.getAnalysisResultContent(analysisResult).subscribe((result:any)=>{
      this.graph2DData = result;
      console.log(result)
      this.setGraph2DOption(result.result)
    });
  }
  validateAnalysisResult(force:boolean){
    let analyzing:boolean = false;
    if (!force){
      this.item.analysisResults.forEach(e => {
        if (e.idState == 1){
          analyzing = true;
        }
      });  
    }
    else{
      analyzing = true;
    }
    if (analyzing){
      let ctr:any = this;
      setTimeout(() => {
        ctr.baseService.getAnalysisResults({id:ctr.item.id}).subscribe((result:any)=>{
          ctr.item.analysisResults = result;
          ctr.savedAnalysisResultsFilter.init(ctr.item.analysisResults, ctr.analysisForm.get('savedAnalysisResults'));
          ctr.validateAnalysisResult();
        });
    }, 1000);
    }
  }
  isValidForm(){
    return this.isSimulationOk && this.analysisForm.valid && (this.analysisForm.get("clusteringMethod").value || this.analysisForm.get("dimensionalityReductionMethod").value)
  }
  linearityAnalysisChange(){
    let normalityAnalysisMethod:any = this.analysisForm.get('linearityAnalysisMethod').value;
    this.analysisForm.get('dimensionalityReductionMethod').reset()
    if (normalityAnalysisMethod == null){
      this.dimensionalityReductionMethodFilter.init(this.hsArray['DIMENSIONALITY_REDUCTION_METHODS'].filter(e=> e.name == 'U_map' || e.name == 'PCA'), this.analysisForm.get('dimensionalityReductionMethod'));
    }
    else{
      this.dimensionalityReductionMethodFilter.init(this.hsArray['DIMENSIONALITY_REDUCTION_METHODS'].filter(e=> e.name == 'PCA/U_map'), this.analysisForm.get('dimensionalityReductionMethod'));
    }
  }
  normalityAnalysisChange(){
    let normalityAnalysisMethod:any = this.analysisForm.get('normalityAnalysisMethod').value;
    this.analysisForm.get('clusteringMethod').reset()
    if (normalityAnalysisMethod == null){
      this.clusteringMethodFilter.init(this.hsArray['CLUSTERING_METHODS'].filter(e=> e.name == 'k_means' || e.name == 'k_median'), this.analysisForm.get('clusteringMethod'));
    }
    else{
      this.clusteringMethodFilter.init(this.hsArray['CLUSTERING_METHODS'].filter(e=> e.name == 'k_means/k_median'), this.analysisForm.get('clusteringMethod'));
    }
  }
  initialize(parent:any){
    this.parent = parent;
    if (this.graph2DEchart == null){
      let ctr:any = this;
      setTimeout(() => {
        ctr.graph2DEchart = echarts.init(ctr.graph2dRef.nativeElement); 
        ctr.graph2DEchart.on('click', function (params) {
          console.log('click', ctr)
          ctr.parent.details([0, 0, 0, {index: params.dataIndex, isNormal: true}])
        });
    
        setTimeout(() => {
          ctr.setGraph2DOption([])
        }, 50);      
      }, 100);      
    }

  }
  showAdditionalData(data:any, items:any){

    this.dialog.open(AdditionalDataComponent, {
      data: {items: items
          , hsArray: this.hsArray
      }
    }).afterClosed().subscribe((result:any) => {      
      if (result != undefined && result != true){
        data['additionalData'] = result;
        this._runAnalysis2D(data);
      }
    });
  }  
  getData(){
    let data:any = {};
    data.normalityAnalysisMethod = this.analysisForm.get("normalityAnalysisMethod").value;
    data.linearityAnalysisMethod = this.analysisForm.get("linearityAnalysisMethod").value;
    data.dimensionalityReductionMethod = this.analysisForm.get("dimensionalityReductionMethod").value;
    data.clusteringMethod = this.analysisForm.get("clusteringMethod").value;
    data.fileName = this.fileNameResult;
    data.id = this.item.id;
    data.analitica = []
    data.graph2DProperties = this.graph2DProperties;
    if (data.linearityAnalysisMethod){
      data.analitica.push(data.linearityAnalysisMethod)
    }
    if (data.dimensionalityReductionMethod){
      data.analitica.push(data.dimensionalityReductionMethod)
    }
    if (data.normalityAnalysisMethod){
      data.analitica.push(data.normalityAnalysisMethod)
    }
    if (data.clusteringMethod){
      data.analitica.push(data.clusteringMethod)
    }
    return data;
  }
  runAnalysis2D():void{
    let additionalData:any[] = [];
    let show:boolean = false;
    let data:any = this.getData()
    data.analitica.forEach(e => {
      let jsonGui:any;
      if (!e['jsonGui']){
        if (e['parametersGui']){
          jsonGui = JSON.parse(e['parametersGui']);
        }
        else{
          jsonGui = {}
        }
        e['jsonGui'] = jsonGui;  
      }
      else{
        jsonGui = e['jsonGui'];
      }
      
      if (jsonGui['additionalData']){
        show = true;
        additionalData.push({'name': e['name'], additionalData: jsonGui['additionalData']})
      }
      else{
        additionalData.push({'name': e['name'], additionalData: []})
      }
    });

    if (show){
      this.showAdditionalData(data, additionalData);
    }
    else{
      data['additionalData'] = [[]];
      this._runAnalysis2D(data);
    }
}  
  _runAnalysis2D(data:any){
    this.graph2DData = {min: 0, max: 0};
    console.log(data)
    this.baseService.getAnalytics(data).subscribe((result:any)=>{
      if (result.result == 'RUN BACKGROUND' || result.result == 'RECORD ALREADY EXIST'){
        this.messageBox.showMessageBox(MessageBoxType.Ok, result.result == 'RECORD ALREADY EXIST' ? this.app.screen.The_parameter_combination_already_exists 
                                                    : this.app.screen.The_process_has_been_left_running_in_the_background)
        this.validateAnalysisResult(true)
        return;
      }
      //console.log(result)
      this.graph2DData = result;
      this.setGraph2DOption(result.result)
      this.validateAnalysisResult(true)
    });
  }

  setGraph2DOption(data:any[]){
    this.graph2DEchart.clear();
    this.selectedGraphType = this.graph2DData.type;
    if (!this.selectedGraphType){
      this.selectedGraphType = 'U_map'
    }
    this.graph2DEchart.setOption(this.getGraph2D(data));
  }

  getTooltip2D(params:any){
    if (this.graph2DData.data == undefined || this.graph2DData.data[params.dataIndex] == undefined){
      return '';
    }
    let s:string = '';
    let index:number = 0
    this.graph2DProperties.fields.forEach(e => {
      s += '<b>' + e.name + ':</b>' + getFormatNumber(this.graph2DData.data[params.dataIndex][index++], 2) + '<br>'
    });
    s += '<b>' + this.graph2DProperties.zAxisField.name + ':</b>' + getFormatNumber(this.graph2DData.data[params.dataIndex][index], 2) + '<br>'
    if (this.selectedGraphType != 'U_map'){
      s = s + '<b>cluster:</b>' + getFormatNumber(params.data[2], 0) + '<br>'
    }
    
    //xxx</b> : ${getFormatNumber(params['value'][0], 2)}<br><b>${label2}</b> : ${getFormatNumber(params['value'][1], 2)}<br><b>Pozo</b> : ${getFormatNumber(params['value'][2], 2)}
    //let s:string = getFormatNumber(params['value'][2], 2)
    return s;
  }
  getGraph2D(data:any[]){
    let ctr:any = this;
    if (this.selectedGraphType == 'U_map'){
      return {
        title: {
          text: this.graph2DData.title,
          subtext: this.graph2DData.subtitle != '' ? this.graph2DData.subtitle : null,
          left: 'center',
          top: 0
        },
        visualMap: {
          show: true,
          min: this.graph2DData.min,
          max: this.graph2DData.max,
          dimension: 2,
          orient: 'vertical',
          right: 10,
          top: 'center',
          text: ['HIGH', 'LOW'],
          calculable: true,
          inRange: {
            color: [
              '#313695',
              '#4575b4',
              '#74add1',
              '#abd9e9',
              '#e0f3f8',
              '#ffffbf',
              '#fee090',
              '#fdae61',
              '#f46d43',
              '#d73027',
              '#a50026'
            ]
          }
        },
        tooltip: {
          trigger: 'item',
          axisPointer: {
            type: 'cross'
          },
          showDelay: 0,
          transitionDuration: 0.2,
          formatter: function (params) {
            return `${ctr.getTooltip2D(params)}`;          
          }

        },
        xAxis: [
          {
            name: this.graph2DData.xAxisName,
            nameLocation: 'middle',
            nameGap: 30,
            nameTextStyle: {fontSize: 16},        
          }
        ],
        yAxis: [
          {
            name: this.graph2DData.yAxisName,
            nameLocation: 'middle',
            nameGap: 50,
            nameTextStyle: {fontSize: 16},        
            type: 'value',
            axisLabel: {
              formatter: '{value}'
            },
            axisPointer: {
              snap: true
            }          
          }
        ],
        dataZoom: DataZoom, 
        series: [{name:"Pozo"
              , type: "scatter"
              , symbolSize :5
              , data: data
            }]
      };    
    }
    else{
      let colors:string[] = generateHexColors('Viridis', this.graph2DData.clusterCount);
      var pieces = [];
      var symbols = []
      for (var i = 0; i < this.graph2DData.clusterCount; i++) {
        pieces.push({
          value: i,
          label: 'cluster ' + i,
          color: colors[i]
        });
        symbols.push('circle')
      }
      pieces.push({
        value: i,
        label: 'Centroid',
        color: '#ff0000'
      });
      symbols.push('triangle')
      let center = this.graph2DData.clusterCount
      if (!this.graph2DData.clusterData){
        this.graph2DData.clusterData = data
        this.graph2DData.centers.forEach(item => {
          item.push(center);
          this.graph2DData.clusterData.push(item)
        });
        }
      return {
        title: {
          text: this.graph2DData.title,
          subtext: this.graph2DData.subtitle != '' ? this.graph2DData.subtitle : null,
          left: 'center',
          top: 0
        },
        visualMap: {
          type: 'piecewise',
          show: true,
          left: 'right',
          top: 'middle',
          min: 0,
          max: this.graph2DData.clusterCount + 1,
          splitNumber: this.graph2DData.clusterCount + 1,
          dimension: 2,
          pieces: pieces,
          inRange: {
            color: colors,
            symbolSize: [8, 8],
            symbol: symbols
          }      
        },
        tooltip: {
          trigger: 'item',
          axisPointer: {
            type: 'cross'
          },
          showDelay: 0,
          transitionDuration: 0.2,
          formatter: function (params) {
            return `${ctr.getTooltip2D(params)}`;          
          }

        },
        xAxis: [
          {
            name: this.graph2DData.xAxisName,
            nameLocation: 'middle',
            nameGap: 30,
            nameTextStyle: {fontSize: 16},        
          }
        ],
        yAxis: [
          {
            name: this.graph2DData.yAxisName,
            nameLocation: 'middle',
            nameGap: 50,
            nameTextStyle: {fontSize: 16},        
            type: 'value',
            axisLabel: {
              formatter: '{value}'
            },
            axisPointer: {
              snap: true
            }          
          }
        ],
        dataZoom: DataZoom,    
        series: [{
                type: "scatter"
              , data: this.graph2DData.clusterData
              , itemStyle:{
                color: (params)=>{
                  return colors[params.data[2]]
                }
              }
            }
          ]
      };       
    }
  }  

}
