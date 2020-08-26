import { TestBed } from '@angular/core/testing';

import { MaxTransferHitService } from './max-transfer-hit.service';

describe('MaxTransferHitService', () => {
  let service: MaxTransferHitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaxTransferHitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
