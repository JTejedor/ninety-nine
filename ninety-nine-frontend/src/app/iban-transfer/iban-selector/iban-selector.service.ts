import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IdData } from 'src/app/data';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class IbanSelectorService {

  private readonly url = environment.apiUrl+"/iban"

  constructor(private http: HttpClient) { }

  getIBANs(): Observable<IdData[]>{
    return this.http.get<IdData[]>(this.url)
  }
}
