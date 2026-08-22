import { TestBed } from '@angular/core/testing';

import { SupplierLocation } from './supplier-location';

describe('SupplierLocation', () => {
  let service: SupplierLocation;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierLocation);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
