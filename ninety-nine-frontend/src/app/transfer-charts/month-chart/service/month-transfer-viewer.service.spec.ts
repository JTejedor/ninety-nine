import { TestBed } from '@angular/core/testing';

import { MonthTransferViewerService } from './month-transfer-viewer.service';

describe('MonthTransferViewerService', () => {
  let service: MonthTransferViewerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MonthTransferViewerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
