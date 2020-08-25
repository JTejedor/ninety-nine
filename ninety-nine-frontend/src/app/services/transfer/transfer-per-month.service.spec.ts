import { TestBed } from '@angular/core/testing';

import { TransferPerMonthService } from './transfer-per-month.service';

describe('TransferPerMonthService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TransferPerMonthService = TestBed.get(TransferPerMonthService);
    expect(service).toBeTruthy();
  });
});
