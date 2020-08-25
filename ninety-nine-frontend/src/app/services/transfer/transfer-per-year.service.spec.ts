import { TestBed } from '@angular/core/testing';

import { TransferPerYearService } from './transfer-per-year.service';

describe('TransferPerYearService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TransferPerYearService = TestBed.get(TransferPerYearService);
    expect(service).toBeTruthy();
  });
});
