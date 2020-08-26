import { TestBed } from '@angular/core/testing';

import { MaxAmountHitService } from './max-amount-hit.service';

describe('MaxAmountHitService', () => {
  let service: MaxAmountHitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaxAmountHitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
