import { Injectable } from '@angular/core';
import { GenericMonthHitService } from '../generic-month-hit.service';
import { MonthId } from 'src/app/data';
import { Observable } from 'rxjs';

export interface MaxTransferData{
  id: MonthId,
  maxCount: number
}

@Injectable({
  providedIn: 'root'
})
export class MaxTransferHitService {

  private readonly url: string = "http://localhost:8080/month/transfer-count-hit"

  constructor(private genericHitService: GenericMonthHitService) { }

  getLastYearHit(): Observable<MaxTransferData>{
    return this.genericHitService.getHitData(this.url, 11);
  }

  getLastThreeYearHit(): Observable<MaxTransferData>{
    return this.genericHitService.getHitData(this.url, 35);
  }

  getAllTimesHit(): Observable<MaxTransferData>{
    return this.genericHitService.getHitAllTimes(this.url);
  }
}
