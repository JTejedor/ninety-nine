import { Injectable } from '@angular/core';
import { TransferPerDayService } from './transfer-per-day.service';
import { DataToViewConvertService } from '../../data-to-view-convert.service';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { map } from "rxjs/operators";
import { CountView, CountData, DayId } from "src/app/data"

@Injectable({
  providedIn: 'root'
})
export class DayTransferViewerService {

  constructor(private dayService: TransferPerDayService) { }

  private convertArray(countDataArray: CountData<DayId>[]): CountView[] {
    return countDataArray.map(
      countData => this.convert(countData)
    )
  }

  private convert(countData: CountData<DayId>): CountView {
    return {
      name: `${countData.id.year}-${countData.id.month}-${countData.id.day}`,
      value: countData.count
    }
  }

  private getDayData(month: number): Observable<CountView[]> {
    const m: moment.Moment = moment()
    const end = m.endOf("month").valueOf()
    const start = m.startOf("month").subtract(month, "month").valueOf()
    return this.dayService.getTransferPerDay(start, end).pipe(
      map(
        countDataArray => {
          return this.convertArray(countDataArray)
        }
      )
    )
  }

  getDayDataLastMonth(): Observable<CountView[]> {
    return this.getDayData(0)
  }

  getDayDataLastThreeMonth(): Observable<CountView[]> {
    return this.getDayData(2)
  }

  getDayDataLastSixMonth(): Observable<CountView[]> {
    return this.getDayData(5)
  }
}
