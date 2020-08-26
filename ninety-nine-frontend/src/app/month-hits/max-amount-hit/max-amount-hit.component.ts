import { Component, OnInit } from '@angular/core';
import { GenericHitData, SelectedTime } from '../generic-month-hit/generic-month-hit.component';
import { MaxAmountHitService, MaxAmountData } from './max-amount-hit.service';
import * as moment from 'moment';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-max-amount-hit',
  templateUrl: './max-amount-hit.component.html',
  styleUrls: ['./max-amount-hit.component.sass']
})
export class MaxAmountHitComponent implements OnInit {

  hit: GenericHitData;

  constructor(private maxAmountHitService : MaxAmountHitService) { 
    this.hit = {
      title: "Month with the most aggregated amount of transfers ever",
      timePeriodLabel: ["Last year", "Last three year", "all times"],
      year: "2020",
      month: "September",
      value: 0,
      unit: "€ EUR",
    }
  }

  ngOnInit(): void {
    this.changePeriod(SelectedTime.ONE_YEAR)
  }

  private setHit(maxAmountData: MaxAmountData){
    this.hit.year = maxAmountData.id.year.toString()
          this.hit.month = moment(maxAmountData.id.month, 'MM').format('MMMM')
          this.hit.value = maxAmountData.maxAmount
  }

  changePeriod(time: SelectedTime){
    if(time === SelectedTime.ONE_YEAR){
      this.maxAmountHitService.getLastYearHit().pipe(first()).subscribe(
        maxAmountData => {
          this.setHit(maxAmountData)
        }
      )
    }
    else if(time === SelectedTime.THREE_YEARS){
      this.maxAmountHitService.getLastThreeYearHit().pipe(first()).subscribe(
        maxAmountData => {
          this.setHit(maxAmountData)
        }
      )
    }
    else if(time === SelectedTime.ALL_TIMES){
      this.maxAmountHitService.getAllTimesHit().pipe(first()).subscribe(
        maxAmountData => {
          this.setHit(maxAmountData)
        }
      )
    }
  }

  onTimeChange(event: SelectedTime) {
    this.changePeriod(event)
  }

}
