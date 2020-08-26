import { Injectable, ɵCompiler_compileModuleSync__POST_R3__ } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpParams, HttpClient } from '@angular/common/http';
import * as moment from 'moment';



@Injectable({
  providedIn: 'root'
})
export class GenericMonthHitService {

  constructor(private http: HttpClient) { }

  getHitData<T>(url: string, month?: number): Observable<T> {
    const m: moment.Moment = moment()
    const end = m.endOf("month").valueOf()
    const start = m.startOf("month").subtract(month, "month").valueOf()
    return this.getHit<T>(url, start, end)
  }

  getHitAllTimes<T>(url: string): Observable<T>{
    const m: moment.Moment = moment()
    const end = m.endOf("month").valueOf()
    let params = new HttpParams().set("end", end.toString());
    params = params.set("start", "0000000000")
    return this.http.get<T>(url, {
      params: params
    })
  }

  private getHit<T>(url: string, start: number, end?: number): Observable<T>{
    let params = new HttpParams().set("start", start.toString());
    if(end){
      params = params.set("end", end.toString())
    }
    return this.http.get<T>(url, {
      params: params
    })
  }
}

