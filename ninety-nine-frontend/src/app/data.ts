//{
//  "id": {
//    "year": 2020,
//    "month": 8,
//    "day": 17
//  },
//  "count": 14699
//}

class DayId {
  year: number;
  month: number;
  day: number;
  public toString = (): string => {
    return `${this.year}-${this.month}-${this.day}`;
  }
}

class MonthId {
  year: number;
  month: number;
  public toString = (): string => {
    return `${this.year}-${this.month}`;
  }
}

class YearId {
  year: number;
  public toString = (): string => {
    return `${this.year}`;
  }
}

interface CountData<T> {
  id: T,
  count: number
}

interface CountView {
  name: string,
  value: number,
}