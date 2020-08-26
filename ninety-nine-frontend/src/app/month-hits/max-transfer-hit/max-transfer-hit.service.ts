import { Injectable } from '@angular/core';
import { GenericMonthHitService } from '../generic-month-hit.service';
import { MonthId } from 'src/app/data';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

export interface MaxTransferData{
  id: MonthId,
  maxCount: number
}

@Injectable({
  providedIn: 'root'
})
export class MaxTransferHitService {

  private readonly url: string = environment.apiUrl+"/month/transfer-count-hit"

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
