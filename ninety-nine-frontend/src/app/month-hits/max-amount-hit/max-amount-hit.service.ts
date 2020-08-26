import { Injectable } from '@angular/core';
import { MonthId } from 'src/app/data';
import { GenericMonthHitService } from '../generic-month-hit.service';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

export interface MaxAmountData{
  id: MonthId,
  maxAmount: number
}

@Injectable({
  providedIn: 'root'
})
export class MaxAmountHitService {

  private readonly url: string = environment.apiUrl+"/month/transfer-amount-hit"

  constructor(private genericHitService: GenericMonthHitService) { }

  getLastYearHit(): Observable<MaxAmountData>{
    return this.genericHitService.getHitData(this.url, 11);
  }

  getLastThreeYearHit(): Observable<MaxAmountData>{
    return this.genericHitService.getHitData(this.url, 35);
  }

  getAllTimesHit(): Observable<MaxAmountData>{
    return this.genericHitService.getHitAllTimes(this.url);
  }
}
