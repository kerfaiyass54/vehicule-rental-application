import { TestBed } from '@angular/core/testing';

import { SupplierDetails } from './supplier-details';

describe('SupplierDetails', () => {
  let service: SupplierDetails;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierDetails);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
