import { Injectable } from '@angular/core';
import { YearId, CountView, CountData } from 'src/app/data';
import { Converter, ChartHttpProviderService } from '../../service/chart-http-provider.service';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

class YearConverter implements Converter<YearId>{
  convertArray(countDataArray: CountData<YearId>[]): CountView[] {
    return countDataArray.map(
      countData => this.convert(countData)
    )
  }

  private convert(countData: CountData<YearId>): CountView {
    return {
      name: `${countData.id.year}`,
      value: countData.count
    }
  }
}

@Injectable({
  providedIn: 'root'
})
export class YearTransferViewerService {

  private readonly url: string = environment.apiUrl+"/count/year"
  private readonly converter: Converter<YearId> = new YearConverter();

  constructor(private chartHttpService: ChartHttpProviderService) { }


  getYearDataLast2Year(): Observable<CountView[]> {
    return this.chartHttpService.getCountViewData(this.url, 11, this.converter)
  }

  getYearDataLast3Year(): Observable<CountView[]> {
    return this.chartHttpService.getCountViewData(this.url, 23, this.converter)
  }

  getYearDataLast5Year(): Observable<CountView[]> {
    return this.chartHttpService.getCountViewData(this.url, 47, this.converter)
  }
}

