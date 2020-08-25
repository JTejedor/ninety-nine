import { TestBed } from '@angular/core/testing';

import { TransferPerDayService } from './transfer-per-day.service';

describe('TransferPerDayService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TransferPerDayService = TestBed.get(TransferPerDayService);
    expect(service).toBeTruthy();
  });
});
