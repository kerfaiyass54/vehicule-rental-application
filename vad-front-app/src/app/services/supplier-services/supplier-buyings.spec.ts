import { TestBed } from '@angular/core/testing';

import { SupplierBuyings } from './supplier-buyings';

describe('SupplierBuyings', () => {
  let service: SupplierBuyings;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierBuyings);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
