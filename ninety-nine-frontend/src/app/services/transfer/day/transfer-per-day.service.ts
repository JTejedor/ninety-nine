import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment'
import { Observable } from 'rxjs';
import { timeout } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TransferPerDayService {

  url: string = environment.apiUrl +"/count/day"

  constructor(private http: HttpClient) { }

  getTransferPerDay(start: number, end?: number): Observable<CountData<DayId>[]>{
    const params = new HttpParams().set("start", start.toString());
    if(end){
      params.set("end", end.toString())
    }
    return this.http.get<CountData<DayId>[]>(this.url, {
      params: params
    })
    .pipe(
      timeout(60000)
    )
  }
}
