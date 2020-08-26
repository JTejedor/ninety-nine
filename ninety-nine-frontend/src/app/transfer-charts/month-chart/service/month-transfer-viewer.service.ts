import { Injectable } from '@angular/core';
import { Converter, ChartHttpProviderService } from '../../service/chart-http-provider.service';
import { MonthId, CountData, CountView } from 'src/app/data';
import { Observable } from 'rxjs';

class MonthConverter implements Converter<MonthId>{
  convertArray(countDataArray: CountData<MonthId>[]): CountView[] {
    return countDataArray.map(
      countData => this.convert(countData)
    )
  }

  private convert(countData: CountData<MonthId>): CountView {
    return {
      name: `${countData.id.year}-${countData.id.month}`,
      value: countData.count
    }
  }
}


@Injectable({
  providedIn: 'root'
})
export class MonthTransferViewerService {

  private readonly url: string = "http://localhost:8080/count/month"
  private readonly converter: Converter<MonthId> = new MonthConverter();

  constructor(private chartHttpService: ChartHttpProviderService) { }


  getMonthDataLastYear(): Observable<CountView[]> {
    return this.chartHttpService.getCountViewData(this.url, 11, this.converter)
  }

  getMonthDataLast3Year(): Observable<CountView[]> {
    return this.chartHttpService.getCountViewData(this.url, 23, this.converter)
  }

  getMonthDataLast5Year(): Observable<CountView[]> {
    return this.chartHttpService.getCountViewData(this.url, 35, this.converter)
  }
}
