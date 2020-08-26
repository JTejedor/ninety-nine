import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CountData, CountView } from 'src/app/data';
import { HttpParams, HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import * as moment from 'moment';


export interface Converter<T>{
  convertArray(countDataArray: CountData<T>[]): CountView[]
}

@Injectable({
  providedIn: 'root'
})
export class ChartHttpProviderService {

  constructor(private http: HttpClient) { }

  getCountViewData<T>(url: string, month: number, converter: Converter<T> ): Observable<CountView[]> {
    const m: moment.Moment = moment()
    const end = m.endOf("month").valueOf()
    const start = m.startOf("month").subtract(month, "month").valueOf()
    return this.getTransfers<T>(url, start, end).pipe(
      map(
        countDataArray => {
          return converter.convertArray(countDataArray)
        }
      )
    )
  }

  private getTransfers<T>(url: string, start: number, end?: number): Observable<CountData<T>[]>{
    let params = new HttpParams().set("start", start.toString());
    if(end){
      params = params.set("end", end.toString())
    }
    return this.http.get<CountData<T>[]>(url, {
      params: params
    })
  }
}
