import { TestBed } from '@angular/core/testing';

import { SupplierDemand } from './supplier-demand';

describe('SupplierDemand', () => {
  let service: SupplierDemand;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierDemand);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
