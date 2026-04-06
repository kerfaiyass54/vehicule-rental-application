import { TestBed } from '@angular/core/testing';

import { SupplierDetailsService } from './supplier-details-service';

describe('SupplierDetailsService', () => {
  let service: SupplierDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierDetailsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
