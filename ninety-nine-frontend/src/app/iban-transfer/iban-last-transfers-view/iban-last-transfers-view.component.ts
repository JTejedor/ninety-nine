import { Component, OnInit } from '@angular/core';
import { Transfer, IbanLastTransfersViewService } from './iban-last-transfers-view.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-iban-last-transfers-view',
  templateUrl: './iban-last-transfers-view.component.html',
  styleUrls: ['./iban-last-transfers-view.component.sass']
})
export class IbanLastTransfersViewComponent implements OnInit {

  displayedColumns: string[] = ['IBAN', 'NIF', 'TIME', 'AMOUNT','CURRENCY','EUR'];
  data: Transfer[] = [];


  constructor(private ibanLastTransfers: IbanLastTransfersViewService) { }

  ngOnInit(): void {
  }

  onIbanChange(iban: string){
    this.ibanLastTransfers.getTransfers(iban).pipe(first()).subscribe(
      retrieveData => this.data = retrieveData
    )
  }

}
