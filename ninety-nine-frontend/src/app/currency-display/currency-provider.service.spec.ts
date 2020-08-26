import { TestBed } from '@angular/core/testing';

import { CurrencyProviderService } from './currency-provider.service';

describe('CurrencyProviderService', () => {
  let service: CurrencyProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CurrencyProviderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
