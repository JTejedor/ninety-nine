import { TestBed } from '@angular/core/testing';

import { IbanLastTransfersViewService } from './iban-last-transfers-view.service';

describe('IbanLastTransfersViewService', () => {
  let service: IbanLastTransfersViewService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IbanLastTransfersViewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
