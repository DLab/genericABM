import { Options } from "@angular-slider/ngx-slider";
import { getFormatNumber } from "src/app/utils/number-format/number-format.directive";
import { global } from "src/globals/global";

export class ValueField {
    
    field:string;
    minValue:number;
    maxValue:number;
    inGraph:boolean;
    sliderValue:number;
    options:Options;
    ticks:number[];
    minZValue:any;
    maxZValue:any;
    step:number;
    highValue:number;
    showTicks:boolean;
    label:string;

    constructor(field:string, inGraph:boolean){
        this.ticks = [];
        this.field = this.getPropertyName(field);
        this.minValue = NaN;
        this.maxValue = NaN;
        this.inGraph = inGraph;
        this.sliderValue = 0;
        this.step = 1;
        this.showTicks = true;
        this.minZValue = null;
        this.maxZValue = null;
        this.label = field.charAt(0).toUpperCase() + field.slice(1)
        this.setOptionInit('')
    }
    getPropertyName(name:string):string{
        let t:string[] = name.split(/[ ]+/);
        let s = ''
        t.forEach(e=>{
            s += e.charAt(0).toUpperCase() + e.substring(1)
        })
        return s.charAt(0).toLowerCase() + s.substring(1)
    }
    addZValue(value:number, e:any){
        if (!this.minZValue || this.minZValue.minValue > value){
            this.minZValue = e;
            e.minValue = value;
        }
        if (!this.maxZValue || this.maxZValue.maxValue < value){
            this.maxZValue = e;
            e.maxValue = value;
        }
    }
    clearZValues(){
        this.minZValue = NaN;
        this.maxZValue = NaN;
    }
    setLabel(label:string){
        this.label = label;
    }
    isInTicks()
    {
        return true;
    }
    addValue(value:number){
        if (isNaN(this.minValue) || this.minValue > value){
            this.minValue = value;
        }
        if (isNaN(this.maxValue) || this.maxValue < value){
            this.maxValue = value;
        }
        let index:number = this.ticks.indexOf(value);
        if (index == -1){
            this.ticks.push(value)
        }
    }
    init(){
        this.ticks = [];
        this.minValue = NaN
        this.maxValue = NaN;
    
    }
    meet(value:number, typePozo:string){
        if (typeof value != 'number'){
            value = value[this.field]
        }
        return value == this.sliderValue
    }
    getValue(e:any, typePozo:string){
        return e[this.field];
    }
    setOptionInit(pozoType:string){
        this.sliderValue = isNaN(this.minValue) ? 0 : this.minValue;
        this.options = this.getOptionInit()
    }
    getStickArray(){
        return this.ticks;
    }
    getOptionInit()
    {
      return {
        floor: isNaN(this.minValue) ? 0 : this.minValue,
        ceil: isNaN(this.maxValue) ? 1 : this.maxValue,
        step: this.step,
        showTicks: this.showTicks,
        ticksArray: this.getStickArray(),
        translate: (value: number): string => {
          return getFormatNumber(value, 2);
        }      
      };
    }
    getMinValue(pozoType:string){
        return this.minValue;
    }
    getMaxValue(pozoType:string){
        return this.maxValue;
    }
    getLabel(pozoType:string){
        return this.label;
    }
}
