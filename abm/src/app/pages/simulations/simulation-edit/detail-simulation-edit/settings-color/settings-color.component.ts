import { Color } from '@angular-material-components/color-picker';
import { Component, Inject, OnInit, Optional, ViewEncapsulation } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { clone } from 'lodash-es';
import { BaseService } from 'src/app/base.service';
import { MessageBox, MessageBoxType } from 'src/app/pages/message-box/message.box';
import { PALETTES_COLORS_NAMES, generateHexColors } from 'src/globals/colors';
import { global } from 'src/globals/global';

@Component({
  selector: 'app-settings-color',
  templateUrl: './settings-color.component.html',
  styleUrls: ['./settings-color.component.scss'],
  encapsulation: ViewEncapsulation.None

})
export class SettingsColorComponent implements OnInit {
  app:any = global;
  palettes:string[] = PALETTES_COLORS_NAMES;
  palette:string = PALETTES_COLORS_NAMES[PALETTES_COLORS_NAMES.length - 1];
  
  form: FormGroup;
  touchUi = false;
  color: ThemePalette = 'primary';
  firstRow:any[];
  secondRow:any[];
  colorCtrs:any
  model:any
  analisysProperties:any[];

  allOptions:any[];
  constructor(@Optional() @Inject(MAT_DIALOG_DATA) public data: any
            , private dialogRef: MatDialogRef<SettingsColorComponent>
            , private fb: FormBuilder
            , private messageBox:MessageBox
            , private baseService: BaseService) { 
      this.model = data.model;
      this.analisysProperties = data.analisysProperties
  }

  ngOnInit() {
    this.allOptions = [];
    this.analisysProperties.forEach(e=>{
      let name:string = e.name.charAt(0) + e.name.substring(1)
      this.allOptions.push({name:e.name, label: name, row: 1})
      this.allOptions.push({name:e.name + '_STD', label: name + '(Std)', row: 2})
    })
    this.firstRow = this.allOptions.filter(e=> e.row == 1)
    this.secondRow = this.allOptions.filter(e=> e.row == 2)
    this.colorCtrs = {};
    this.allOptions.forEach(e=>{
      let rgba:number[] = this.hexToRgbA(global.configurations[this.model.name].itemStyleColor[e.name])
      this.colorCtrs[e.name] = new FormControl(new Color(rgba[0], rgba[1], rgba[2]), [Validators.required]);
    })
  }
  changePalette(){
    let colors:any[] = generateHexColors(this.palette, this.allOptions.length)
    let i = 0
    this.allOptions.forEach(e=>{
      let rgba:number[] = this.hexToRgbA(colors[i++]);
      (this.colorCtrs[e.name] as FormControl).setValue(new Color(rgba[0], rgba[1], rgba[2]));
    })

  }
  hexToRgbA(hex:string){
    var c;
    if(/^#([A-Fa-f0-9]{3}){1,2}$/.test(hex)){
        c = hex.substring(1).split('');
        if(c.length== 3){
            c= [c[0], c[0], c[1], c[1], c[2], c[2]];
        }
        c = '0x'+c.join('');
        return [(c>>16)&255, (c>>8)&255, c&255];
    }
    throw new Error('Bad Hex');
}  
  accept(){
    let configurations:any = clone(global.configurations)

    this.allOptions.forEach(e=>{
      let ctr:any = this.colorCtrs[e.name];
      configurations[this.model.name].itemStyleColor[e.name] = '#' + ctr.value.hex;  
    })
    this.baseService.saveUserConfiguration({user: global.user, configurations: configurations}).subscribe((result:any)=>{
      this.messageBox.showMessageBox(MessageBoxType.Success, this.app.screen['Registro_ingresado_con_exito']);
      global.configurations = configurations;
      this.dialogRef.close({});
    });

    
  }
}
