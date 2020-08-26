import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment'
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IdData } from '../data';
import { map } from 'rxjs/operators';

export interface TransfersByCurrency{
  id: string,
  count: number
}

export interface CurrencyCount{
  currencies: number
}

@Injectable({
  providedIn: 'root'
})
export class CurrencyProviderService {

  private readonly url = environment.apiUrl + "/currencies"
  constructor(private http: HttpClient) { }

  getReceivedCurrencies(): Observable<string[]>{
    return this.http.get<IdData[]>(this.url).pipe(
      map(
        idDataContainer => idDataContainer.map(idData => idData.id)
      )
    )
  }

  getTransferCurrencies(): Observable<TransfersByCurrency[]>{
    const url = this.url+"/transfers"
    return this.http.get<TransfersByCurrency[]>(url)
  }

  getTotalCurrencies(): Observable<CurrencyCount>{
    const url = this.url+"/count"
    return this.http.get<CurrencyCount>(url)
  }
}
