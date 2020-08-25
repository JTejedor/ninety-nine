import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class DataToViewConvertService {

  constructor() { }

  convertArray<T>( countDataArray: CountData<T>[]): CountView[]{
    return countDataArray.map(
      countData => this.convert<T>(countData)
    )
  }

  convert<T>( countData: CountData<T>): CountView{
    return {
      name: countData.id.toString(),
      value: countData.count
    }
  }
}
