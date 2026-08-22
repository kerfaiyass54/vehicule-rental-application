import { TestBed } from '@angular/core/testing';

import { SupplierAddresses } from './supplier-addresses';

describe('SupplierAddresses', () => {
  let service: SupplierAddresses;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierAddresses);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
