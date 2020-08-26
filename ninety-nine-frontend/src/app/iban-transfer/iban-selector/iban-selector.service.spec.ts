import { TestBed } from '@angular/core/testing';

import { IbanSelectorService } from './iban-selector.service';

describe('IbanSelectorService', () => {
  let service: IbanSelectorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IbanSelectorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
