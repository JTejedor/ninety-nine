import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { IbanSelectorService } from './iban-selector.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-iban-selector',
  templateUrl: './iban-selector.component.html',
  styleUrls: ['./iban-selector.component.sass']
})
export class IbanSelectorComponent implements OnInit {

  ibans: string[]

  @Output()
  ibanEmitter: EventEmitter<string> = new EventEmitter()

  constructor(private ibanService: IbanSelectorService) { 
    this.ibans = []
  }

  ngOnInit(): void {
    this.ibanService.getIBANs().pipe(first()).subscribe(
      idDataContainer => {
        idDataContainer.map(
          idData => this.ibans.push(idData.id)
        )        
      }
    )
  }

  onIbanChange(event){
    this.ibanEmitter.emit(event.value)
  }

}
