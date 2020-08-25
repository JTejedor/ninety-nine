import { TestBed } from '@angular/core/testing';

import { DataToViewConvertService } from './data-to-view-convert.service';

describe('DataToViewConvertService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DataToViewConvertService = TestBed.get(DataToViewConvertService);
    expect(service).toBeTruthy();
  });
});
