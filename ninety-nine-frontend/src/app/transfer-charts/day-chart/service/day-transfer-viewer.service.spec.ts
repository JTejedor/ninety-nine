import { TestBed } from '@angular/core/testing';

import { DayTransferViewerService } from './day-transfer-viewer.service';

describe('DayTransferViewerService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DayTransferViewerService = TestBed.get(DayTransferViewerService);
    expect(service).toBeTruthy();
  });
});
