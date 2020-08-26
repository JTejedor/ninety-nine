import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CountView } from 'src/app/data';

export enum DataSize{
  SMALL=1,
  MEDIUM,
  LARGE,
  CUSTOM
}

export enum ScreenSize{
  SMALL=0,
  MEDIUM,
  LARGE,
}

export interface GenericChartData{
  title: string;
  description: string;
  dataSizeLabel: string[];
  view: number [];
  data: CountView[];
  // options
  showXAxis:boolean;
  showYAxis:boolean;
  gradient:boolean;
  showXAxisLabel:boolean;
  xAxisLabel:string;
  showYAxisLabel:boolean;
  yAxisLabel:string;
  colorScheme: Object;
}

@Component({
  selector: 'app-generic-chart',
  templateUrl: './generic-chart.component.html',
  styleUrls: ['./generic-chart.component.sass']
})
export class GenericChartComponent implements OnInit {
  
  @Input()
  chart: GenericChartData;

  @Output()
  sizeChanged: EventEmitter<DataSize> = new EventEmitter()

  constructor() { }

  ngOnInit(): void {
  }

  dataSizeChanged(event){
    this.sizeChanged.emit(DataSize[event.value as string])
  }

  onSelect(event){

  }
}
