import { Injectable } from '@angular/core';
import { CountData, CountView, DayId } from "src/app/data"

@Injectable({
  providedIn: 'root'
})
export class DataToViewConvertService {

  constructor() { }

  convertArray<T>( countDataArray: CountData<DayId>[]): CountView[]{
    return countDataArray.map(
      countData => this.convert(countData)
    )
  }

  convert( countData: CountData<DayId>): CountView{
    return {
      name: `${countData.id.year}-${countData.id.month}-${countData.id.day}`,
      value: countData.count
    }
  }
}
