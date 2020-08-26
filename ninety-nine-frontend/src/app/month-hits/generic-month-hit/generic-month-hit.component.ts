import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

export enum SelectedTime{
  ONE_YEAR,
  THREE_YEARS,
  ALL_TIMES
}

export interface GenericHitData{
  title: string;
  timePeriodLabel: string[];
  year: string;
  month: string;
  value: number;
  unit: string;
}

@Component({
  selector: 'app-generic-month-hit',
  templateUrl: './generic-month-hit.component.html',
  styleUrls: ['./generic-month-hit.component.sass']
})
export class GenericMonthHitComponent implements OnInit {

  @Input()
  hit: GenericHitData

  @Output()
  timeChange: EventEmitter<SelectedTime> = new EventEmitter()

  constructor() { }

  ngOnInit(): void {
  }

  timeChanged(event){
    this.timeChange.emit(SelectedTime[event.value as string])
  }
}
