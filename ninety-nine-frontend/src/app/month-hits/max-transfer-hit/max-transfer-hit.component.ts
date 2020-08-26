import { Component, OnInit } from '@angular/core';
import { SelectedTime, GenericHitData } from '../generic-month-hit/generic-month-hit.component';
import { MaxTransferHitService, MaxTransferData } from './max-transfer-hit.service';
import { first, max } from 'rxjs/operators';
import * as moment from 'moment';

@Component({
  selector: 'app-max-transfer-hit',
  templateUrl: './max-transfer-hit.component.html',
  styleUrls: ['./max-transfer-hit.component.sass']
})
export class MaxTransferHitComponent implements OnInit {

  hit: GenericHitData;

  constructor(private maxTransferHitService : MaxTransferHitService) {
    this.hit = {
      title: "Month with the most transfers",
      timePeriodLabel: ["Last year", "Last three year", "all times"],
      year: "2020",
      month: "September",
      value: 0,
      unit: "transfers",
    }
  }

  ngOnInit(): void {
    this.changePeriod(SelectedTime.ONE_YEAR)
  }

  private setHit(maxTransferData: MaxTransferData){
    this.hit.year = maxTransferData.id.year.toString()
          this.hit.month = moment(maxTransferData.id.month, 'MM').format('MMMM')
          this.hit.value = maxTransferData.maxCount
  }

  changePeriod(time: SelectedTime){
    if(time === SelectedTime.ONE_YEAR){
      this.maxTransferHitService.getLastYearHit().pipe(first()).subscribe(
        maxTransferData => {
          this.setHit(maxTransferData)
        }
      )
    }
    else if(time === SelectedTime.THREE_YEARS){
      this.maxTransferHitService.getLastThreeYearHit().pipe(first()).subscribe(
        maxTransferData => {
          this.setHit(maxTransferData)
        }
      )
    }
    else if(time === SelectedTime.ALL_TIMES){
      this.maxTransferHitService.getAllTimesHit().pipe(first()).subscribe(
        maxTransferData => {
          this.setHit(maxTransferData)
        }
      )
    }
  }

  onTimeChange(event: SelectedTime) {
    this.changePeriod(event)
  }

}
