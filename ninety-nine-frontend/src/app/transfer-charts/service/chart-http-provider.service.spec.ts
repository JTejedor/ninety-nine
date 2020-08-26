import { TestBed } from '@angular/core/testing';

import { ChartHttpProviderService } from './chart-http-provider.service';

describe('ChartHttpProviderService', () => {
  let service: ChartHttpProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChartHttpProviderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
