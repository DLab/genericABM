import { ValueField } from "./ValueField";

export class LimitedValuesField extends ValueField{

    constructor(field:string, inGraph:boolean){
        super(field, inGraph)
        this.step = 0.1;
    }
    meet(obj:any, typePozo:string){
        return super.meet(obj[this.field].params ? obj[this.field].params[0] : obj[this.field], typePozo)
    }
    getValue(obj:any, typePozo:string){
        return obj[this.field].params ? obj[this.field].params[0] : obj[this.field]
    }
    isInTicks(){
        return this.ticks.indexOf(this.sliderValue) != -1;
    }

}
