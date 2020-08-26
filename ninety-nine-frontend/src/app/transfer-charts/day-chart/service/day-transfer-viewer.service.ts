import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CountView, CountData, DayId } from "src/app/data"
import { ChartHttpProviderService, Converter } from '../../service/chart-http-provider.service';
import { environment } from 'src/environments/environment';

class DayConverter implements Converter<DayId>{
  convertArray(countDataArray: CountData<DayId>[]): CountView[] {
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
}

@Injectable({
  providedIn: 'root'
})
export class DayTransferViewerService {

  private readonly url: string = environment.apiUrl+"/count/day"
  private readonly converter: Converter<DayId> = new DayConverter();

  constructor(private chartHttpService: ChartHttpProviderService) { }


  getDayDataLastMonth(): Observable<CountView[]> {
    return this.chartHttpService.getCountViewData(this.url, 0, this.converter)
  }

  getDayDataLastThreeMonth(): Observable<CountView[]> {
    return this.chartHttpService.getCountViewData(this.url, 2, this.converter)
  }

  getDayDataLastSixMonth(): Observable<CountView[]> {
    return this.chartHttpService.getCountViewData(this.url, 5, this.converter)
  }
}
