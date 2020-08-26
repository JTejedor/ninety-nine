//{
//  "id": {
//    "year": 2020,
//    "month": 8,
//    "day": 17
//  },
//  "count": 14699
//}

export interface IdData{
  id:string
}

export interface DayId {
  year: number;
  month: number;
  day: number;
}

export interface MonthId {
  year: number;
  month: number;
}

export interface YearId {
  year: number;
}

export interface CountData<T> {
  id: T;
  count: number;
}

export interface CountView {
  name: string,
  value: number,
}