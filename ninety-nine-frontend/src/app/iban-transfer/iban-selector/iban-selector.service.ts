import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface IdData{
  id:string
}

@Injectable({
  providedIn: 'root'
})
export class IbanSelectorService {

  private readonly url = "http://localhost:8080/iban"

  constructor(private http: HttpClient) { }

  getIBANs(): Observable<IdData[]>{
    return this.http.get<IdData[]>(this.url)
  }
}
