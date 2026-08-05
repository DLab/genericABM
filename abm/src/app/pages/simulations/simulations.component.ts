import { ChangeDetectorRef, Component, OnDestroy, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { ListColumn } from 'src/@fury/shared/list/list-column.model';
import { MantenedorBaseComponent } from '../mantenedor-base/mantenedor-base.component';
import { getTime, global } from 'src/globals/global';
import { ComponentType } from 'ngx-toastr';
import { formatDate } from '@angular/common';
import { SimulationEditComponent } from './simulation-edit/simulation-edit.component';
import { BaseService } from 'src/app/base.service';
import { MessageBox } from '../message-box/message.box';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-simulations',
  templateUrl: './simulations.component.html',
  styleUrls: ['./simulations.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SimulationsComponent implements OnInit, OnDestroy {
  app:any = global;
  pendingQuerys:any;
  states:any;
  destroy:boolean;

  @Output() multiplesParametros:any = {codigoPadre: ['ACTIVE_MODELS', 'SIMULATION_STATE', 'ANALYSIS_METHODS']};
  @Output() editDialog:ComponentType<any> = SimulationEditComponent;
  @Output() columns: ListColumn[] = [
    { name: this.app.screen.Id, property: 'id', visible: false, isModelProperty: true },
    { name: this.app.screen.Descripcion, property: 'description', visible: true, isModelProperty: true },
    { name: this.app.screen.User, property: 'userId', visible: true, isModelProperty: true },
    { name: this.app.screen.Model, property: 'modelName', visible: true, isModelProperty: true },
    { name: this.app.screen.Privated, property: 'privated', visible: true, isModelProperty: true, type: 'CHECKED' },
    { name: this.app.screen.Estado, property: 'simulationState', visible: true, isModelProperty: true },
    { name: this.app.screen.Remaining, property: 'remainingTime', visible: true, isModelProperty: true, renderer:  this.remainingTimeRender, parentRenderer: this},
    { name: this.app.screen.Timestamp, property: 'timestamp', visible: true, isModelProperty: true },
    { name: this.app.screen.Accion, property: 'accion', visible: true},
  ] as ListColumn[];

  @ViewChild(MantenedorBaseComponent, { static: true }) mantenedor: MantenedorBaseComponent;

  constructor(private messageBox:MessageBox,
    private baseService: BaseService,
    private sanitizer: DomSanitizer,
    private cdr: ChangeDetectorRef) {
    global.currentComponent = this;
    this.pendingQuerys = {}
    this.destroy = false;
  }

  ngOnInit() {
  }
  ngOnDestroy(): void {
    this.destroy = true;
  }
  getTime(time:number, idSimulationState:number){
    if (time == 0){
      if (idSimulationState == 5){
        return 'Save Files..';  
      }
      return 'Analyzing results..';
    }
    return getTime(time)
  }
  remainingTimeRender(row:any){
    if (row['progress'] == undefined || this['parentRenderer'].destroy){
      return '';
    }
    let ctr:any = this;    
    if (ctr['parentRenderer'].pendingQuerys[row['id']] == undefined){
      ctr['parentRenderer'].pendingQuerys[row['id']] = 'X';
      setTimeout(() => {
        if (this['parentRenderer'].destroy){
          return;
        }
        ctr['parentRenderer'].baseService.getProgressSimulation({id:row['id']}).subscribe((result:any)=>{
          //console.log(result)
          row['progress'] = result['progress']
          row['remainingTime'] = result['remainingTime']
          row['idSimulationState'] = result['idSimulationState']
          row['simulationState'] = ctr['parentRenderer'].states[result['idSimulationState']].description
          ctr['parentRenderer'].pendingQuerys[row['id']] = undefined;
          ctr['parentRenderer'].cdr.detectChanges()
        });
        
      }, 2000);  
    }
    return this['parentRenderer'].sanitizer.bypassSecurityTrustHtml('<div class="container-progress"><div class="progress" style="width: ' 
        + (row['progress'] * 100)+ '%"><div class="inner-progress">' 
        + this['parentRenderer'].getTime(row['remainingTime'] / 1000, row['idSimulationState'])+ '</div></div></div>')
  }

  @Output() newItem():any{
    return {isNew:true
      , editable: true
      , idSimulationState:2
      , memory: false
      , numSim: this.app.properties['SweepSimulation.numSim']
      , numAgents: this.app.properties['SweepSimulation.numAgents']
      , probMeet: this.app.properties['model.default.probMeet'].replace('.', ',')
      , numSteps: this.app.properties['SweepSimulation.numSteps']
      , userId: global.user
     };
  }
  @Output() processRow(row:any, hsArray:any):void{
    row['timestamp'] = formatDate(row['timestamp'], 'yyyy/MM/dd HH:mm:ss', global.currentLocale);
    if (!this['parent'].states){
      this['parent'].states = {};
      hsArray['SIMULATION_STATE'].forEach(e => {
        this['parent'].states[e.id] = e
      });
    }
    row.simulationState = this['parent'].states[row.idSimulationState].description
  }
  getEditDialogSize()
  {
    return {height: '800px', width: '1500px'}
  }
  hasCommand(action:string, row:any):boolean {
    return action == 'Agregar' ? false: action == 'Cancel' ? row.idSimulationState == 1 && (row.userId === global.user || global.user.toLowerCase() === 'admin') : true
  }
}
