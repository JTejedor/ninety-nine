import { Component, OnInit } from '@angular/core';
import { CurrencyProviderService } from '../currency-provider.service';
import { concatMap, first, map } from 'rxjs/operators';

export interface CurrencyTotal {
  name: string,
  value: number
}

@Component({
  selector: 'app-currency-display',
  templateUrl: './currency-display.component.html',
  styleUrls: ['./currency-display.component.sass']
})
export class CurrencyDisplayComponent implements OnInit {
  private readonly letters = '0123456789ABCDEF';
  currencies: CurrencyTotal[] = [];
  view: any[] = [800, 400];

  // options
  gradient: boolean = true;
  showLegend: boolean = true;
  showLabels: boolean = true;
  isDoughnut: boolean = false;
  legendPosition: string = 'right';

  colorScheme: object;

  receivedData: boolean = false;

  constructor(private currencyService: CurrencyProviderService) { }

  ngOnInit(): void {
    this.currencyService.getTotalCurrencies().pipe(
      concatMap(
        currencyTotal =>{
          this.colorScheme = this.fillScheme(currencyTotal.currencies)
          return this.currencyService.getTransferCurrencies()
        }
      ),
      map(
        currencyTransfers => currencyTransfers.map(
          currencyTransfer =>{
            let currencyTotal:CurrencyTotal ={
                name: currencyTransfer.id,
                value: currencyTransfer.count
            }
            return currencyTotal;
          }
        )
      ),
      first()
    ).subscribe(
      currencyTransfers => {
        this.currencies = currencyTransfers
        this.receivedData = true;
      }
    )
  }

  fillScheme(totalCurrencies: number){
    const domain = [];
    for (let i = 0; i < totalCurrencies; i++) {
      domain.push(this.getRandomColor())
    }
    return {
      domain: domain
    }
  }

  private getRandomColor(): string {
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += this.letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

}
