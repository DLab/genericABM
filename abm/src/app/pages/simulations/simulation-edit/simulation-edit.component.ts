import { PozoValueField } from './PozoValueField';
import { LimitedValuesField } from './LimitedValuesField';
import { ValueField } from './ValueField';
import { clone } from 'lodash-es';
import { AfterViewInit, Component, ElementRef, EventEmitter, Inject, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { BaseService } from 'src/app/base.service';
import { BaseEditComponent } from '../../util/base-edit-component';
import { getTime, global } from 'src/globals/global';
import { MatTableDataSource } from '@angular/material/table';
import { ListColumn } from 'src/@fury/shared/list/list-column.model';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { getFormatNumber, getParseNumber } from 'src/app/utils/number-format/number-format.directive';
import { MessageBox, MessageBoxType } from '../../message-box/message.box';
import { formatDate } from '@angular/common';
import * as echarts from 'echarts';
import 'echarts-gl';
import { MatMenuTrigger } from '@angular/material/menu';
import { DetailSimulationEditComponent } from './detail-simulation-edit/detail-simulation-edit.component';
import { FilterList } from 'src/app/utils/filter-list';
import { Analysis2DComponent } from './analysis2D/analysis2D.component';

@Component({
  selector: 'app-simulation-edit',
  templateUrl: './simulation-edit.component.html',
  styleUrls: ['./simulation-edit.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SimulationEditComponent extends BaseEditComponent implements AfterViewInit {

  suffixFieldsName:any[] = [{name: 'Start', type: 'number', value: 0}
                          , {name: 'Step', type: 'number', value: 0.1}
                          , {name: 'End', type: 'number', value: 1}
                          , {name: 'X', type: 'checked', label: 'InXAxis'}
                          , {name: 'Y', type: 'checked', label: 'InYAxis'}
                          , {name: 'Z', type: 'checked', label: 'InZAxis'}]

  app:any = global;
  
  form: FormGroup;
  filterForm: FormGroup;
  isSimulated:boolean;
  simulationResults:any[];
  fileNameResult:string;
  chartClassName:string;
  graph:any;
  zValue:string;
  filterOpenState:boolean = true;
  initialized:boolean;
  filterFields:any[][];
  globalScale:boolean = true;
  running:boolean = false;
  zAxisProperty:any;
  functionProperties:any[];
  rangeProperties:any[];
  resultSimulation:any;
  graph2DProperties:any = {fields:[], zAxisField: null};
  
  xAxisField:ValueField;
  yAxisField:ValueField;
  zAxisField:ValueField;

  sliderFields:ValueField[];

  fields:any;
  contextMenuPosition = { x: '0px', y: '0px' };  

  meanStepTime:number;
  modelSelectedFilter:FilterList = new FilterList('name');
  numAgents:any[];
  descriptionGenerated:string;

  
  dataSource: MatTableDataSource<any> = new MatTableDataSource();
  @Output() columns: ListColumn[] = [];

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild("paginator", { static: true }) paginator: MatPaginator;
  @ViewChild('graph', { static: true }) graphRef: ElementRef;
  @ViewChild('analysis2D', { static: true }) analysis2D: Analysis2DComponent;
  @ViewChild(MatMenuTrigger) contextMenu: MatMenuTrigger;
  
  private graphEchart:any;


  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
      private dialogRef: MatDialogRef<SimulationEditComponent>,
      private fb: FormBuilder,
      private messageBox:MessageBox,
      private baseService: BaseService,
      private dialog: MatDialog) { 
    super(data);
    this.chartClassName = 'long-demo-chart';

    this.meanStepTime = Number(this.app.properties['SweepSimulation.meanStepTime']);
    if (this.hsArray['ACTIVE_MODELS'].length == 0){
      messageBox.showMessageBox(MessageBoxType.Ok, this.app.screen.There_are_no_activated_models_with_which_you_can_simulate)
    }

  }

  ngOnInit() {
    this.initialized = false;
    this.zValue = 'mean';
    this.isSimulated = !this.item.isNew;
    this.filterFields = []
    console.log(this.item)

    if (!this.item.isNew){
      let editable:boolean = this.item.editable;
      
      this.item = this.item.details;
      this.item.editable = editable;
      
      this.item['timestamp'] = formatDate(this.item['timestamp'], 'yyyy/MM/dd HH:mm:ss', global.currentLocale);      
      if (!this.item.details){
        this.item.details = [];
      }
      this.simulationResults = clone(this.item.details);
      this.dataSource.data = this.item.details
      this.analysis2D.isSimulationOk = true
    }
    else{
      this.simulationResults = []
    }
    this.hsArray['CLUSTERING_METHODS'] = this.hsArray['ANALYSIS_METHODS'].filter(e=>e.parentName == 'Clustering')
    this.hsArray['NORMALITY_ANALYSIS_METHODS'] = this.hsArray['ANALYSIS_METHODS'].filter(e=>e.parentName == 'Normality analysis')
    this.hsArray['LINEARITY_ANALYSIS_METHODS'] = this.hsArray['ANALYSIS_METHODS'].filter(e=>e.parentName == 'Linearity analysis')
    this.hsArray['DIMENSIONALITY_REDUCTION_METHODS'] = this.hsArray['ANALYSIS_METHODS'].filter(e=>e.parentName == 'Dimensionality reduction')
    this.form = this.fb.group({
      description: [{value: this.item.description, disabled: !this.item.editable}],
      numSteps: [{value: this.item.numSteps, disabled: !this.item.editable}], 
      numSim: [{value: this.item.numSim, disabled: !this.item.editable}], 
      modelSelected: [{value: this.item.modelSelected, disabled: !this.item.isNew}], 
      probMeet: [{value: this.item.probMeet, disabled: !this.item.editable}],  
      clusteringMethod: [{value: this.getItem('clusteringMethod', 'name', 'CLUSTERING_METHODS'), disabled: !this.item.editable}], 
      normalityAnalysisMethod: [{value: this.getItem('normalityAnalysisMethod', 'name', 'NORMALITY_ANALYSIS_METHODS'), disabled: !this.item.editable}], 
      linearityAnalysisMethod: [{value: this.getItem('linearityAnalysisMethod', 'name', 'LINEARITY_ANALYSIS_METHODS'), disabled: !this.item.editable}], 
      dimensionalityReductionMethod: [{value: this.getItem('dimensionalityReductionMethod', 'name', 'DIMENSIONALITY_REDUCTION_METHODS'), disabled: !this.item.editable}], 

      userId: [{value: this.item.userId, disabled: true}],  
      timestamp: [{value: this.item.timestamp, disabled: true}],  
    });

    //console.log(this.item)
    this.modelSelectedFilter.init(this.hsArray['ACTIVE_MODELS'], this.form.get('modelSelected'));
    
    this.sliderFields = [];
  }
  onChangeText(){
    let model:any = this.form.get("modelSelected").value
    let description:any = this.form.get("description").value
    if (this.item.isNew && model && (this.descriptionGenerated == undefined || this.descriptionGenerated == description)){

      this.descriptionGenerated = model.name.replace(" (", "-").replace("(", "-").replace(")", "") + " (" 
                                + this.form.get("numSim").value 
                                + "-" + this.form.get("numSteps").value 
                                + "-" + this.form.get("probMeet").value 
      //console.log(this.numAgents, this.form.value)                                
      this.numAgents.forEach(e=>{
        this.descriptionGenerated += "-" + e.ctr.value
      })
      this.descriptionGenerated += ")"
      this.form.get("description").setValue(this.descriptionGenerated)
    }
  }
  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.graphEchart = echarts.init(this.graphRef.nativeElement); 
    
    let ctr:any = this;
    this.graphEchart.on('click', function (params) {
      ctr.openMenu(params)
    });
    if (!this.item.isNew){
      setTimeout(() => {
        this.modelSelectedChange()  
        this.initialized = true;
      }, 50);      
    }
    else{
      this.initialized = true;
      this.setGraphOption()
    }
  }
  onTabChanged(event:any){
    if (event.index == 1){
      this.analysis2D.initialize(this);
    }
  }
  changeGlobalScale(){
    this.setGraphOption()    
  }

  modelSelectedChange(){
    let model:any = this.form.get("modelSelected").value

    this.filterFields = []
    this.fields = {};
    this.graph2DProperties = {fields:[], zAxisField: null};

    if (model){
      let fieldControls:any = {};
      let isNew:boolean = this.item.isNew
      this.meanStepTime = model.meanSimulationTime ? model.meanSimulationTime : this.meanStepTime;
      this.columns = [];
      this.functionProperties = [];
      this.rangeProperties = []
      this.numAgents = [];

      model.agents.forEach(e => {
        if (!e.base){
          let name:string = e.name.charAt(0).toUpperCase() + e.name.substring(1);
          if (this.item.isNew){
            this.numAgents.push({name:name, ctr: new FormControl(this.item.numAgents)})
          }
          else{
            this.numAgents.push({name:name, ctr: new FormControl(this.item.numAgents[name])})
          }
  
        }        
      });
      //console.log(model)
      model.variables.forEach(e => {
        let name:string = e.name.charAt(0).toUpperCase() + e.name.substring(1);
        
        if (e.rangeValues){
          let row:any[];
          
          if (e.generalAnalysisId && e.generalAnalysisId != ''){
            this.fields[e.name] = new LimitedValuesField(e.name, e.generalAnalysisId == 'X' || e.generalAnalysisId == 'Y' || e.generalAnalysisId == 'Z')
            if (this.fields[e.name].inGraph){
              this[e.generalAnalysisId.toLowerCase() + 'AxisField'] = this.fields[e.name]
            }
            else{
              this.sliderFields.push(this.fields[e.name])
            }
          }
          
          this.filterFields.push(row = []);
          this.suffixFieldsName.forEach(suffix=>{
            let fieldName:string = e.name + suffix.name
            fieldControls[fieldName] = [{value: isNew ? (suffix.type == 'number' ? (suffix.name == 'End' && e.initialValue ? e.initialValue.split(';')[0] : suffix.name == 'Step' && e.variableTypeId == 1 ? 1 : suffix.value) 
                                : suffix.name == e.generalAnalysisId) : this.item[fieldName], disabled: !this.item.editable}]
            row.push({name: fieldName, type: suffix.type
              , label: suffix.label
              , decimals: e.variableTypeId == 2 || e.variableTypeId == 5 ? 2 : 0
              , axisName: e.name
              , axis: suffix.type == 'number' ? '' : suffix.name})
          })
          if (e.variableTypeId == 5){
            this.functionProperties.push(e)
            this.columns.push({ name: name, property: '_' + e.name, visible: true, isModelProperty: true } as ListColumn)
          }
          else{
            this.rangeProperties.push(e)
            this.columns.push({ name: name, property: e.name, visible: true, isModelProperty: true, numeric: true, numDecimales: 2 } as ListColumn)
          }
          this.graph2DProperties.fields.push({name: name, property: e.name, typeId: e.variableTypeId});
        }
        else if (e.generalAnalysisId == 'X' || e.generalAnalysisId == 'Y' || e.generalAnalysisId == 'Z'){
          this.fields[e.name] = new PozoValueField(e.name, true)
          //console.log(e.name)
          this[e.generalAnalysisId.toLowerCase() + 'AxisField'] = this.fields[e.name]
          if (e.generalAnalysisId == 'Z'){
            this.zAxisProperty = e;
            this.columns.push({ name: name + '(Median)', property: e.name + '_median', visible: true, isModelProperty: true, numeric: true, numDecimales: 2 } as ListColumn)
            this.columns.push({ name: name + '(Mean)', property: e.name + '_mean', visible: true, isModelProperty: true, numeric: true, numDecimales: 2 } as ListColumn)
            this.columns.push({ name: name + '(Std)', property: e.name + '_std', visible: true, isModelProperty: true, numeric: true, numDecimales: 2 } as ListColumn)

            this.graph2DProperties.zAxisField = {name: name, property: e.name, typeId: e.variableTypeId};
          }
          else{
            this.graph2DProperties.fields.push({name: name, property: e.name, typeId: e.variableTypeId});
          }
          
        }
      });

      //console.log(this.graph2DProperties)
      this.filterForm = this.fb.group(fieldControls);
      this.initResults(this.simulationResults)
      this.sliderFields.forEach(e=>{
        e.setOptionInit(this.zValue)
      });
      this.setGraphOption()
      if (this.item.isNew){
        this.onChangeText()
      }

    }

  }
  openMenu(params:any){
    this.contextMenuPosition.x = params.event.event.clientX + 'px';
    this.contextMenuPosition.y = params.event.event.clientY + 'px';
    this.contextMenu.menuData = { 'item': params.data };
    this.contextMenu.menu.focusFirstItem('mouse');
    this.contextMenu.openMenu();
  }
  getMinZValue(){
    return getFormatNumber(this.zAxisField.minZValue.minValue, 2)
  }
  getMaxZValue(){
    return getFormatNumber(this.zAxisField.maxZValue.maxValue, 2)
  }
  getZValue(item:any){
    return getFormatNumber(this.zAxisField.getValue(item[3], this.zValue), 2)
  }
  minDetail(item:any){
    this.details([0, 0, 0, this.zAxisField.minZValue])
  }
  maxDetail(item:any){
    this.details([0, 0, 0, this.zAxisField.maxZValue])
  }

  details(item:any){
    let input:any = {}
    input.idSimulation = this.item.id;
    input.id =  item[3].index;
    input.isNew = this.item.isNew;
    input.fileName = this.fileNameResult;
    this.baseService.simulationDetailQuery(input).subscribe((result:any)=>{
      this.dialog.open(DetailSimulationEditComponent, {
        data: {item: result, model: this.form.get("modelSelected").value, isNormal: item[3].isNormal},
        height: '800px', width: '1500px', maxWidth: '1500px'
      })
    });
  }
  setGraphOption(){
    this.graphEchart.clear();
    this.graphEchart.setOption(this.getGraph());
  }

  assignVariableFields(){
    this.sliderFields = [];
    for(let key in this.fields){
      let field:ValueField = this.fields[key];
      if (!field.inGraph){
        this.sliderFields.push(field)
        field.setOptionInit(this.zValue);
      }
    };
    this.setGraphOption()
  }
  changeAxis(value:string, axis:string){
    if (axis != 'Z'){
      let axisField:ValueField = this[axis.toLowerCase() + 'AxisField']
      if (value == axisField.field){
        this.filterForm.get(value + axis).setValue(true)
      }
      else if (value == this.xAxisField.field || value == this.yAxisField.field || value == this.zAxisField.field){
        this.filterForm.get(value + axis).setValue(false)
      }
      else{
        this.filterForm.get(axisField.field + axis).setValue(false)      
        axisField.inGraph = false
        this[axis.toLowerCase() + 'AxisField'] = this.fields[value]
        this[axis.toLowerCase() + 'AxisField'].inGraph = true
        this.assignVariableFields()
      }  
    }
    else{
      let pozoField:PozoValueField = null;
      for (let key in this.fields) {
        const field:any = this.fields[key];
        if (field.constructor.name == 'PozoValueField'){
          pozoField = field;
          break;
        }
      }

      if (value == this.zAxisField.field){
        this.zAxisField.inGraph = false
        this.zAxisField = pozoField
        this.zAxisField.inGraph = true
        this.assignVariableFields()
      }
      else if (value == this.xAxisField.field || value == this.yAxisField.field){
        this.filterForm.get(value + 'Z').setValue(false)
      }
      else{
        if (this.zAxisField.field != pozoField.field){
          this.filterForm.get(this.zAxisField + 'Z').setValue(false)
        }
        this.zAxisField.inGraph = false
        this.zAxisField = this.fields[value]
        this.zAxisField.inGraph = true
        this.assignVariableFields()
      }
  
    }
  }

  changeMeanOption(){
    this.setGraphOption()
  }
  fieldFilterChange(index:number){

    //esto es porque hay un bug en los slider, cuando llega a cero, deja 1.4456e-16
    for(let i in this.sliderFields){
      this.sliderFields[i].sliderValue = Math.floor(this.sliderFields[i].sliderValue * 1000) / 1000
    }
    if (this.sliderFields[index].isInTicks()){
      this.setGraphOption()
    }
  }
  initResults(result:any[]){
    for(let key in this.fields){
      this.fields[key].init();
    };
    //console.log(result, this)
    result.forEach(e => {
      e[this.zAxisProperty.name + '_median'] = e[this.zAxisProperty.name].median
      e[this.zAxisProperty.name + '_mean'] = e[this.zAxisProperty.name].mean
      e[this.zAxisProperty.name + '_std'] = e[this.zAxisProperty.name].std
  
      //console.log(this.functionProperties, e)
      this.functionProperties.forEach(fn=>{    
        e['_' + fn.name] = this.getFunctionStr(e[fn.name])
        this.fields[fn.name].addValue(e[fn.name].params[0])
      })
      //console.log("veamos::", e, this.zAxisProperty.name, e[this.zAxisProperty.name].mean)
      this.fields[this.zAxisProperty.name]['mean'].addValue(e[this.zAxisProperty.name].mean)
      this.fields[this.zAxisProperty.name]['median'].addValue(e[this.zAxisProperty.name].median)
      //this.fields['numAgents'].addValue(e.numAgents)
      this.rangeProperties.forEach(prop=>{     
        if (this.fields[prop.name])   {
          this.fields[prop.name].addValue(Math.floor(e[prop.name] * 1000) / 1000)
        }
      })

    });
  }
  onFilterChange(dataSource:MatTableDataSource<any>, value:string) {
    if (!dataSource) {
      return;
    }
    value = value.trim();
    value = value.toLowerCase();
    dataSource.filter = value;
  }

  get visibleColumns() {
    return this.columns.filter(column => column.visible).map(column => column.property);
  }

  isFormValid(){
    return (!this.initialized && !this.item.isNew) ||  (this.form.valid && this.filterForm.valid);
  }
  getFunctionStr(f:any){
    let params:number[] = f.params;
    let s:string = '';
    let comma:string = '';
    params.forEach(n=>{
      s += comma + n;
      comma = ', ';
    })
    return f.name + " [" + s + "]";
  }
  
  formatNumber(n:number, numDecimales:number){
    return getFormatNumber(n, numDecimales);
  }
  downloadChart(){
    let img:any[] = [this.graphEchart.getDataURL({type:'png', pixelRatio: 1, backgroundColor: '#fff'})];
    let title:string = this.form.value.description
    console.log(this.form.get("modelSelected").value)
    this.baseService.downloadSimulation({id:this.item.id, data: this.simulationResults, img:img, title: title, detail: false, model: this.form.get("modelSelected").value}).subscribe((file:any)=>{
      var blob = new Blob([file], { type: 'application/zip' });
      var url = window.URL.createObjectURL(blob);
      var a = document.createElement('a');
      var fileName = title + '_' + formatDate(new Date(), 'yyyy/MM/dd/HH:mm:ss', global.currentLocale)+ '.zip';
      a.href = url;
      a.download =  fileName;
      a.click();
      window.URL.revokeObjectURL(url);

      url = 'sim/sim_' + this.item.id + '.csv'
      console.log(url)
      a = document.createElement('a');
      fileName = title + '_all_' + formatDate(new Date(), 'yyyy/MM/dd/HH:mm:ss', global.currentLocale)+ '.csv';
      a.href = url;
      a.download =  fileName;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      this.messageBox.showMessageBox(MessageBoxType.Success, global.screen['Se_ha_generado_el_archivo_con_exito']);
    });

  }

  save(){
    let value:any = this.form.value;
    let filterValues:any = this.filterForm.value

    for (let key in filterValues){
      value[key] = filterValues[key]
    } 
    value.isNew = this.item.isNew
    value.id = this.item.id
    value.details = this.simulationResults;
    value.fileName = this.fileNameResult;
    value.idSimulationState = this.item.idSimulationState
    value.userId = this.item.userId
    value.modelName = value.modelSelected.name;
    if (this.resultSimulation && this.item.isNew){
      value.numAgents = this.resultSimulation.numAgents;
    }
    
    this.baseService.simulationSave(value).subscribe((result:any)=>{
      if (value.isNew)  {
        this.messageBox.showMessageBox(MessageBoxType.Success, global.screen['Registro_ingresado_con_exito']);
      }            
      else{
        this.messageBox.showMessageBox(MessageBoxType.Success, global.screen['Registro_modificado_con_exito']);
      }

      result['timestamp'] = formatDate(result['timestamp'], 'yyyy/MM/dd HH:mm:ss', global.currentLocale);
      this.dialogRef.close(result);
      });
  }
  getNumber(o:any, trunc:boolean){
    let value:number;
    if (typeof o == 'string'){
      value = getParseNumber(o)
    }
    else{
      value = o;
    }
    return trunc ? Math.trunc(value) : value;
  }
  
  getCombinations(model:any, key:string, trunc:boolean){
    let value:number = this.getNumber(model[key + 'Start'], trunc);
    let step:number = this.getNumber(model[key + 'Step'], trunc);
    let end:number = this.getNumber(model[key + 'End'], trunc);
    if (value < end && step == 0){
      throw new Error(global.getScreen('You_must_enter_a_value_greater_than_zero_for_the_variable') + ' ' + key + 'Step');
    }

    let n:number = 0;
    while(value <= end){
      value += step;
      n++;
    }
    return n;
  }

  getSimValues(value:any){
    let numStep:number = this.getNumber(value['numSteps'], true);
    let numSim:number = this.getNumber(value['numSim'], true);
    let numAgents:number = 0;
    for(var e in value.numAgents){
      numAgents += value.numAgents[e]
    }
    if (numAgents == 0){
      numAgents = 1
    }
    let model:any = this.form.get("modelSelected").value
    let numCombinaciones:number = 1;
    model.variables.forEach(e => {
      if (e.rangeValues){
        //console.log(e)
        numCombinaciones = numCombinaciones * this.getCombinations(value, e.name, e.variableTypeId == 1)
      }      
    });
    let steps:number = numCombinaciones * numSim * numStep;
    let time:number = (steps * numAgents * this.meanStepTime) / 1000;
    //console.log(steps, time, steps * numAgents * this.meanStepTime, this.meanStepTime)
    //console.log('combinaciones', time, meanNumAgents, steps)
    return [getFormatNumber(numCombinaciones, 0), getFormatNumber(steps, 0), getTime(time), time];
  }
  simulate(){
    let value:any = this.form.value;    
    let filterValues:any = this.filterForm.value
    for (let key in filterValues){
      value[key] = filterValues[key]
    } 
    value.numAgents = {};
    this.numAgents.forEach(e=>{
      value.numAgents[e.name] = typeof(e.ctr.value) === 'string' ? Number(e.ctr.value.replace('.', '')) : e.ctr.value
    });
    value.modelSelected = this.form.get("modelSelected").value
    //console.log(this.numAgents, value.numAgents, this.form.get("modelSelected").value, value)
    
    let arr:any[];
    try{
     arr = this.getSimValues(value);
    }catch(e:any){
      this.messageBox.showMessageBox(MessageBoxType.Error, e.message);
      return;
    } 
    value.modelName = value.modelSelected.name;
    value.background = arr[3] >= 300;
    //console.log(arr, value)
    if (value.background && !this.isFormValid()){
      this.messageBox.showMessageBox(MessageBoxType.Error, global.getScreen('Por_favor_ingrese_los_campos_minimos_necesarios'));
      return;
    }
    this.messageBox.showMessageBox(MessageBoxType.Question, global.getScreen('The_question_for_conducting_a_simulation', arr)).subscribe(result=>{
      if (result === 'SI' || result === 'YES'){
        this._simulate(value);
      }
    });
  }
  _simulate(value:any){
    value.idSimulationState = this.item.idSimulationState
    value.userId = this.item.userId
    value.isNew = this.item.isNew
    value.id = this.item.id
    this.analysis2D.isSimulationOk = false
    this.baseService.simulate(value).subscribe((result:any)=>{
      if (value.background){
        this.messageBox.showMessageBox(MessageBoxType.Information, global.getScreen('The_process_was_running_in_the_background'));
        this.dialogRef.close(result);
        return;  
      }
      this.fileNameResult = result.fileName;
      this.simulationResults = clone(result.results)
      this.initResults(result.results)
      this.isSimulated = true;
      this.resultSimulation = result;
      this.dataSource.data = result.results;
      this.analysis2D.isSimulationOk = true
      this.item.id = result.idProcess
      this.item.isNew = false
      this.assignVariableFields();
    });

  }

  getTooltip(params:any){
    //xxx</b> : ${getFormatNumber(params['value'][0], 2)}<br><b>${label2}</b> : ${getFormatNumber(params['value'][1], 2)}<br><b>Pozo</b> : ${getFormatNumber(params['value'][2], 2)}
    let s:string = '<b>' + this.xAxisField.getLabel(this.zValue) + ':</b>' + getFormatNumber(params['value'][0], 2) + '<br>'
                 + '<b>' + this.yAxisField.getLabel(this.zValue) + ':</b>' + getFormatNumber(params['value'][1], 2) + '<br>'
                 + '<b>' + this.zAxisField.getLabel(this.zValue) + ':</b>' + getFormatNumber(params['value'][2], 2) + '<br>'
              ;
              
    this.sliderFields.forEach((f:ValueField)=>{
      //console.log(params, params['value'][3], f.getValue(params['value'][3], this.zValue))
      s += '<b>' + f.getLabel(this.zValue) + ':</b>' + getFormatNumber(f.getValue(params['value'][3], this.zValue), 2) + '<br>';
    })
    return s;
  }

  getGraph(){
    if (this.running){
      return;
    }
    this.running = true;
    let data:any = [];
    if (this.zAxisField){
      this.zAxisField.clearZValues();
    }
    this.simulationResults.sort(function(a1:any, a2:any){
      let dif:number = a1.initConf - a2.initConf;
      if (dif == 0){
        return a1.initRepu - a2.initRepu;
      }
      return dif;
    });
    //console.log(this.simulationResults)
    this.simulationResults.forEach(e => {
      let meet:boolean = true;
      for(let i in this.sliderFields){
        let f:ValueField = this.sliderFields[i]
        //console.log(e, this.zValue, f.meet(e, this.zValue))
        if (!f.meet(e, this.zValue)){
          meet = false;
          break;
        }
      }
      if (meet){
        let pozo:number = this.zAxisField.getValue(e, this.zValue)
        //console.log(pozo, e)
        this.zAxisField.addZValue(pozo, e)
        data.push([this.xAxisField.getValue(e, this.zValue), this.yAxisField.getValue(e, this.zValue),pozo, e]);
      }
    });
    let ctr:any = this;
    let subtext:string = '';
    let sep:string = '';
    let min:number = this.zAxisField ? (this.globalScale ? this.zAxisField.getMinValue(this.zValue) : this.zAxisField.minZValue.minValue) : 0;
    let max:number = this.zAxisField ? (this.globalScale ? this.zAxisField.getMaxValue(this.zValue) : this.zAxisField.maxZValue.maxValue) : 0;
    this.running = false;
    this.sliderFields.forEach(e => {
      subtext += sep + e.getLabel(this.zValue) + ':' + e.sliderValue
      sep = ' - '
    });
    this.running = false;
    let modelSelected:any = this.form.get("modelSelected").value
    //console.log(data)
    return {
      title: {
        text: modelSelected ? modelSelected.name : this.app.screen.Untitle,
        subtext: subtext
      },
      tooltip: {
        trigger: 'item',
        showDelay: 0,
        transitionDuration: 0.2,
        formatter: function (params) {
          return `${ctr.getTooltip(params)}`;          
        }
      },
      backgroundColor: '#fff',
      visualMap: {
        show: true,
        calculable: true,
        dimension: 2,
        min: isNaN(min) ? 0 : min,
        max: isNaN(max) ? 0 : max,
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
      xAxis3D: {
          type: 'value'
        , name: this.xAxisField ? this.xAxisField.getLabel(this.zValue) : 'X-Axis'
      },
      yAxis3D: {
          type: 'value'
        , name: this.yAxisField ? this.yAxisField.getLabel(this.zValue) : 'Y-Axis'
      },
      zAxis3D: {
          type: 'value'
        , name: this.zAxisField ? this.zAxisField.getLabel(this.zValue) : 'Z-Axis'
      },
      grid3D: {
        viewControl: {
           //projection: 'orthographic'
        }
      },
      series: [
        {
          type: 'surface',
          wireframe: {
             show: true
          },
          data: data
        }
      ]
    };    
  }  
}
