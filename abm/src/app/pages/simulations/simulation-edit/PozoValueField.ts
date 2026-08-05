import { global } from "src/globals/global";
import { ValueField } from "./ValueField";
import { getFormatNumber } from "src/app/utils/number-format/number-format.directive";

export class PozoValueField extends ValueField{
    mean:ValueField;
    median:ValueField;
    constructor(field:string, inGraph:boolean){
        super(field, inGraph)
        this.mean = new ValueField(field, true)
        this.median = new ValueField(field, false)
        this.mean.setLabel(this['mean'].label + "_mean")
        this.median.setLabel(this['median'].label + "_median")
        this.step = 0.1;
        this.mean.showTicks = false
        this.median.showTicks = false
        this.setOptionInit('mean')
    }
    init(){
        this.mean.init()
        this.median.init()
    }
    meet(obj:any, typePozo:string){
        let value:number = obj.pozo[typePozo];
        return value >= this.sliderValue && value <= this.highValue;
    }
    getValue(e:any, typePozo:string){
        //console.log(e, this.field)
        return e[this.field][typePozo];
    }
    getStickArray(){
        return []
    }
    setOptionInit(pozoType:string){
        if (this.mean){
            if (pozoType == 'mean'){
                this.maxValue = isNaN(this.mean.maxValue) ? 0 : this.mean.maxValue;
                this.minValue = isNaN(this.mean.minValue) ? 0 : this.mean.minValue; 
            }
            else{
                this.maxValue = isNaN(this.median.maxValue) ? 0 : this.median.maxValue;
                this.minValue = isNaN(this.median.minValue) ? 0 : this.median.minValue; 
            }
    
        }
        this.highValue = this.maxValue -1
        super.setOptionInit(pozoType)
    }
    getLabel(pozoType:string){
        return this[pozoType].label;
    }
    getMinValue(pozoType:string){
        if (pozoType == 'mean'){
            return this.mean.minValue; 
        }
        else{
            return this.median.minValue; 
        }    
    }
    getMaxValue(pozoType:string){
        if (pozoType == 'mean'){
            return this.mean.maxValue; 
        }
        else{
            return this.median.maxValue; 
        }    
    }


}
