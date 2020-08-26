import { TestBed } from '@angular/core/testing';

import { YearTransferViewerService } from './year-transfer-viewer.service';

describe('YearTransferViewerService', () => {
  let service: YearTransferViewerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(YearTransferViewerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
