import { Injectable } from '@angular/core';
import { TransferPerDayService } from './transfer-per-day.service';
import { DataToViewConvertService } from '../../data-to-view-convert.service';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { map } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class DayTransferViewerService {

  constructor(private dayService: TransferPerDayService,
    private convertService: DataToViewConvertService) { }

  getDayDataMonth(month?: number): Observable<CountView[]> {
    const m: moment.Moment = moment()
    const monthIncrement = month ? month : 0;
    const start = m.startOf("month").subtract(monthIncrement,"month").valueOf()
    const end = m.endOf("month").subtract(monthIncrement,"month").valueOf()
    return this.dayService.getTransferPerDay(start, end).pipe(
      map(
        countDataArray => this.convertService.convertArray(countDataArray)
      )
    )
  }

}
