import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Transfer{
  iban: string;
  nif: string;
  timestamp: string;
  amount: number;
  currency: string;
  eurConversion: number;
}

@Injectable({
  providedIn: 'root'
})
export class IbanLastTransfersViewService {

  private readonly url = "http://localhost:8080/iban/transfers"

  constructor(private http: HttpClient) { 

  }

  getTransfers(iban: string): Observable<Transfer[]>{
    const url = this.url +"/"+iban
    let params = new HttpParams().set("start", (0).toString());
    params = params.set("limit", (20).toString())
    return this.http.get<Transfer[]>(url, {
      params: params
    })
  }
}
