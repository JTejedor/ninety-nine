import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment'
import { Observable } from 'rxjs';
import { CountData, DayId } from "src/app/data"

@Injectable({
  providedIn: 'root'
})
export class TransferPerDayService {

  url: string = environment.apiUrl +"/count/day"

  constructor(private http: HttpClient) { }

  getTransferPerDay(start: number, end?: number): Observable<CountData<DayId>[]>{
    let params = new HttpParams().set("start", start.toString());
    if(end){
      params = params.set("end", end.toString())
    }
    return this.http.get<CountData<DayId>[]>(this.url, {
      params: params
    })
  }
}
