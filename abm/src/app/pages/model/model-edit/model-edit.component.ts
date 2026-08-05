import { PropertyBaseEditComponent } from './property-base-edit-component';
import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { BaseService } from 'src/app/base.service';
import { formatDate } from '@angular/common';
import { MatTableDataSource } from '@angular/material/table';
import { ListColumn } from 'src/@fury/shared/list/list-column.model';
import { hasPrivilege } from 'src/app/utils/privileges';
import { AgentEditComponent } from './agent-edit/agent-edit.component';
import { RuleEditComponent } from './rule-edit/rule-edit.component';
import { ActionEditComponent } from './action-edit/action-edit.component';
import { FilterList } from 'src/app/utils/filter-list';
import { global } from 'src/globals/global';
import { GraphEditComponent } from './graph-edit/graph-edit.component';
import { MessageBox, MessageBoxType } from '../../message-box/message.box';
import { clone } from 'lodash-es';

@Component({
  selector: 'app-model-edit',
  templateUrl: './model-edit.component.html',
  styleUrls: ['./model-edit.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ModelEditComponent extends PropertyBaseEditComponent implements OnInit {
  app:any = global;

  downloadFiles:boolean
  form: FormGroup;
  runWithFilter:FilterList = new FilterList('name');
  agentsDataSource: MatTableDataSource<any> | null;
  agentsColumns: ListColumn[] = [
      { name: global.screen['Name'], property: 'name', visible: true, isModelProperty: true }
    , { name: global.screen['Descripcion'], property: 'description', visible: true, isModelProperty: true }
    , { name: global.screen['Base'], property: 'base', visible: true, isModelProperty: true, type: 'CHECKED'}
    , { name: global.screen['Graph'], property: 'graphName', visible: true, isModelProperty: true }
    , { name: global.screen['ExtendsFrom'], property: 'extendsFrom', visible: true, isModelProperty: true }
    , { name: global.screen['Accion'], property: 'accion', visible: true},
  ] as ListColumn[];


  actionsDataSource: MatTableDataSource<any> | null;
  actionsColumns: ListColumn[] = [
    { name: global.screen['Name'], property: 'name', visible: true, isModelProperty: true }
  , { name: global.screen['Descripcion'], property: 'description', visible: true, isModelProperty: true }
  , { name: global.screen['Accion'], property: 'accion', visible: true},
] as ListColumn[];

rulesDataSource: MatTableDataSource<any> | null;
rulesColumns: ListColumn[] = [
  { name: global.screen['Name'], property: 'name', visible: true, isModelProperty: true }
, { name: global.screen['Descripcion'], property: 'description', visible: true, isModelProperty: true }
, { name: global.screen['Initial'], property: 'initial', visible: true, isModelProperty: true,  type: 'CHECKED'}
, { name: global.screen['TrueAction'], property: 'trueActionName', visible: true, isModelProperty: true }
, { name: global.screen['FalseAction'], property: 'falseActionName', visible: true, isModelProperty: true }
, { name: global.screen['BeforeAction'], property: 'beforeActionName', visible: true, isModelProperty: true }
, { name: global.screen['AfterAction'], property: 'afterActionName', visible: true, isModelProperty: true }
, { name: global.screen['Position'], property: 'position', visible: true, isModelProperty: true }
, { name: global.screen['Accion'], property: 'accion', visible: true},
] as ListColumn[];

graphsDataSource: MatTableDataSource<any> | null;
graphsColumns: ListColumn[] = [
  { name: global.screen['Name'], property: 'name', visible: true, isModelProperty: true }
, { name: global.screen['Tipo'], property: 'graphTypeId', visible: true, isModelProperty: true }
, { name: global.screen['Topology'], property: 'graphTopologyId', visible: true, isModelProperty: true }
, { name: global.screen['Algorithm'], property: 'graphAlgorithmId', visible: true, isModelProperty: true }
, { name: global.screen['Accion'], property: 'accion', visible: true},
] as ListColumn[];

SingleAgentList:any[] = [{name: 'AGENTS'}]
constructor(@Inject(MAT_DIALOG_DATA) public data: any,
            private dialogRef: MatDialogRef<ModelEditComponent>,
            private fb: FormBuilder,
            private baseService: BaseService,
            public messageBox: MessageBox,
            public dialog: MatDialog) { 
              super(data, dialog, false);
  }

  ngOnInit() {
    let editable:boolean = this.item.editable;
    if (!this.item.isNew || this.item.isCopy){
      let isCopy:boolean = this.item.isCopy;
      let searchResult:any[] = this.item.searchResult;
      let functions:any[] = this.item.functions;
      this.item = this.item.isCopy ? clone(this.item.details) : this.item.details;
      this.downloadFiles = !this.item.isNew && this.item.activate;
      this.item.searchResult = searchResult;
      this.item.functions = functions;
      if (isCopy){
        this.item.isNew = true;
        this.item.name = null;
        this.item.userId = global.user
      }
      else{
        this.item.isNew = false;
      }
      let hsVariables:any = {}
      this.item.variables.forEach(e => {
        hsVariables[e.name] = e;
      });
      this.item.agents.forEach(agent => {
        agent.variables.forEach(e => {
          if (hsVariables[e.modelNameRef]){
            hsVariables[e.modelNameRef]['isRef'] = e.modelNameRef != undefined
          }
          
        });
      });
    }
    this.item.editable = editable &&hasPrivilege('Agregar') && hasPrivilege('Eliminar')
    this.hsArray['RULES'] = this.item.rules;
    this.form = this.fb.group({
      name: [{value: this.item.name, disabled: !this.item.editable}],
      activate: [{value: this.item.activate, disabled: !this.item.editable}],
      kqmlIntegration: [{value: this.item.kqmlIntegration, disabled: !this.item.editable}],
      private: [{value: this.item.private, disabled: !this.item.editable}],
      synchronized: [{value: this.item.synchronized, disabled: true}],
      singleAgentsList: [{value: this.item.singleAgentsList, disabled: !this.item.editable}],
      description: [{value: this.item.description, disabled: !this.item.editable}], 
      runWith: [{value: this.getItem('runWith', 'name', 'ALL_RESULTS'), disabled: !this.item.editable}], 
    });
    this.runWithFilter.init(this.hsArray['ALL_RESULTS'].filter(e=> e.name != this.item.name), this.form.get('runWith'));
    this.agentsDataSource = new MatTableDataSource();
    this.actionsDataSource = new MatTableDataSource();    
    this.rulesDataSource = new MatTableDataSource();    
    this.graphsDataSource = new MatTableDataSource();    

    this.item.variables.sort(function(a:any, b:any){return a.name < b.name ? -1 : a.name == b.name ? 0 : 1});
    this.item.rules.sort(function(a:any, b:any){
      return a.position - b.position;
    });

    this.agentsDataSource.data = this.item.agents;
    this.actionsDataSource.data = this.item.actions;    
    this.rulesDataSource.data = this.item.rules;
    this.propertiesDataSource.data = this.item.variables;
    this.graphsDataSource.data = this.item.graphs;
    this.validateListAgents()
  }
  validateListAgents(){
    if (this.item.agents.length > 1){
      this.form.get('singleAgentsList').enable()
    } 
    else{
      this.form.get('singleAgentsList').disable()
      this.form.get('singleAgentsList').setValue(false)
    }  
  }
  changeKQMLProperty(event:any){
    this.item.kqmlIntegration = event.checked
    super.modelChangeKqmlProperty()
  }
  downloadSourceFiles(){
    this.baseService.downloadSourceFile({name: this.item.name}).subscribe((file:any)=>{
      const blob = new Blob([file], { type: 'application/zip' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      const fileName = this.item.name + '-src-' + formatDate(new Date(), 'yyyy/MM/dd/HH:mm:ss', global.currentLocale)+ '.jar';
      a.href = url;
      a.download =  fileName;
      a.click();
      window.URL.revokeObjectURL(url);
      this.messageBox.showMessageBox(MessageBoxType.Success, global.screen['Se_ha_generado_el_archivo_con_exito']);
    });

  }
  getAgentDescription(row:any, column:ListColumn){
    let value:any = row[column.property]
    if (value && column.property == 'extendsFrom'){
      return this.item.agents.find(e=>e.name == value)['name']
    }
    return value;
  }
  addAgent(){
    this.editAgent({isNew:true, variables:[]}, true);
  }
  editAgent(agent:any, editable:boolean){
    agent.hsArray = this.hsArray
    agent.editable = editable
    agent.all = this.item.agents
    agent.model = this.item;
    this.hsArray['AGENTS'] = this.item.agents.filter(e=> e != agent)
    this.hsArray['GRAPHS'] = this.item.graphs;
    this.dialog.open(AgentEditComponent, {
      data: {data: agent, parent: this},
      height: '800px', width: '1500px'
    }).afterClosed().subscribe(result => {
      if (result != undefined && result != true && this.item.agents.indexOf(result) == -1){
        this.item.agents.push(result)
        this.agentsDataSource.data = this.item.agents;
      }
      let bases:string[] = [];
      this.item.agents.forEach(agent => {
        if (agent.extendsFrom){
          bases.push(agent.extendsFrom)
        }
        agent['base'] = false
      });
      bases.forEach(name=>{
        this.item.agents.find(e=>e.name == name)['base'] = true;
      })
      this.validateListAgents()
      delete agent.hsArray
      delete agent.all
      delete agent.model;
    });
  }
  deleteAgent(row:any){
    this.agentsDataSource.data = this.deleteItem(row, this.item.agents)
    this.validateListAgents()
  }
  get agentsVisibleColumns() {
    return this.agentsColumns.filter(column => column.visible).map(column => column.property);
  }

  getRuleDescription(row:any, column:ListColumn){
    let value:any = row[column.property]
    if (value && (column.property == 'trueActionName' || column.property == 'falseActionName')){
      return this.item.actions.find(e=>e.name == value)['name']
    }
    return value;
  }
  addRule(){
    this.editRule({isNew:true, position: this.item.rules.length + 1, initial: false}, true);
  }

  editRule(rule:any, editable:boolean){
    rule.hsArray = this.hsArray
    rule.editable = this.item.editable
    rule.all = this.item.rules
    rule.model = this.item;
    this.hsArray['RULES'] = this.item.rules.filter(e=> e != rule)
    this.hsArray['ACTIONS'] = this.item.actions;
    
    this.hsArray['AGENTS'] = this.form.get('singleAgentsList').value ? this.SingleAgentList : this.item.agents;
    this.dialog.open(RuleEditComponent, {
      data: {data: rule, parent: this},
      height: '500px', width: '1000px'
    }).afterClosed().subscribe(result => {
      if (result != undefined && result != true && this.item.rules.indexOf(result) == -1){
        this.item.rules.push(result)
        this.item.rules.sort(function(a:any, b:any){
          return a.position - b.position;
        });
        this.rulesDataSource.data = this.item.rules;
        this.actionsDataSource.data = this.item.actions;
      }
      delete rule.model
      delete rule.hsArray
      delete rule.all

    });
  }
  changeName(){
    this.item.name = this.form.get("name").value
  }
  deleteRule(row:any){
    this.rulesDataSource.data = this.deleteItem(row, this.item.rules)
    this.deleteAction(this.item.actions.find(e=>e.name == row.trueActionName));
    this.deleteAction(this.item.actions.find(e=>e.name == row.falseActionName));
    this.deleteAction(this.item.actions.find(e=>e.name == row.beforeActionName));
    this.deleteAction(this.item.actions.find(e=>e.name == row.afterActionName));
  }

  get rulesVisibleColumns() {
    return this.rulesColumns.filter(column => column.visible).map(column => column.property);
  }

  getActionDescription(row:any, column:ListColumn){
    return row[column.property]
  }
  addAction(){
    this.editAction({isNew:true}, true);
  }
  editAction(action:any, editable:boolean){
    action.hsArray = this.hsArray
    action.editable = editable
    action.all = this.item.actions
    action.model = this.item;
    this.hsArray['RULES'] = this.item.rules
    this.dialog.open(ActionEditComponent, {
      data: {data: action, parent: this},
      height: '800px', width: '1500px'
    }).afterClosed().subscribe(result => {
      if (result != undefined && result != true && this.item.actions.indexOf(result) == -1){
        this.item.actions.push(result)
        this.actionsDataSource.data = this.item.actions;
      }
      delete action.hsArray
      delete action.all
      delete action.model
    });
  }
  deleteAction(row:any){
    this.actionsDataSource.data = this.deleteItem(row, this.item.actions)
  }

  get actionsVisibleColumns() {
    return this.actionsColumns.filter(column => column.visible).map(column => column.property);
  }

  getGraphDescription(row:any, column:ListColumn){
    let value:any = row[column.property]
    if (value && column.property == 'graphTypeId'){
      return this.hsArray['GRAPH_TYPES'].find(e=>e.id == value)['description']
    }
    if (value && column.property == 'graphAlgorithmId'){
      return this.hsArray['GRAPH_ALGORITHMS'].find(e=>e.id == value)['description']
    }
    if (value && column.property == 'graphTopologyId'){
      return this.hsArray['GRAPH_TOPOLOGYS'].find(e=>e.id == value)['description']
    }
    return value;
  }
  addGraph(){
    this.editGraph({isNew:true}, true);
  }
  editGraph(graph:any, editable:boolean){
    graph.hsArray = this.hsArray
    graph.editable = editable
    graph.all = this.item.graphs
    this.dialog.open(GraphEditComponent, {
      data: {data: graph, parent: this},
      height: '240px', width: '1200px'
    }).afterClosed().subscribe(result => {
      if (result != undefined && result != true && this.item.graphs.indexOf(result) == -1){
        this.item.graphs.push(result)
        this.graphsDataSource.data = this.item.graphs;
      }
      delete graph.hsArray
      delete graph.all

    });
  }
  deleteGraph(row:any){
    this.graphsDataSource.data = this.deleteItem(row, this.item.graphs)
  }

  get graphsVisibleColumns() {
    return this.graphsColumns.filter(column => column.visible).map(column => column.property);
  }


  isFormValid(){
    return this.form.valid
  }
  onFilterChange(value:string) {

    if (!this.agentsDataSource) {
      return;
    }
    value = value.trim();
    value = value.toLowerCase();
    this.agentsDataSource.filter = value;
  }
  variablesValidate(model:any){
    let x:number = 0;
    let y:number = 0;
    let z:number = 0;
    model.variables.forEach(e => {
      if (e.generalAnalysisId == 'X'){
        x++;
      }
      else if (e.generalAnalysisId == 'Y'){
        y++;
      }
      else if (e.generalAnalysisId == 'Z'){
        z++;
      }
    });
    return x == 1 && y == 1 && z == 1;
  }
  save(){
    let value:any = {};
    for(var e in this.form.value){
      value[e] = this.form.value[e];
    }
    value.isNew = this.item.isNew
    value.agents = this.item.agents;
    value.rules = this.item.rules;
    value.actions = this.item.actions;
    value.variables = this.item.variables;
    value.graphs = this.item.graphs;
    if (!value.name){
      value.name = this.item.name
    }
    value['additionalCode'] = JSON.stringify(this.item.functions);
    value.userId = this.item.userId;
    value['runWith'] = value['runWith'] ? value['runWith']['name'] : null;
    
    if (value['activated'] && !this.variablesValidate(value)){
      this.messageBox.showMessageBox(MessageBoxType.Ok, this.app.screen.You_must_enter_a_variable_on_theX_Y_Z_axis)
      return;
    }
    if (!this.validate(value)){
      return;
    }
    delete value.hsArray
    this.baseService.modelSave(value).subscribe(result=>{
      result['timestamp'] = formatDate(result['timestamp'], 'yyyy/MM/dd HH:mm:ss', global.currentLocale);
      this.dialogRef.close(result);
    });    
  }

}
