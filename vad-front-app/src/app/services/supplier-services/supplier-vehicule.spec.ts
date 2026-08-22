import { TestBed } from '@angular/core/testing';

import { SupplierVehicule } from './supplier-vehicule';

describe('SupplierVehicule', () => {
  let service: SupplierVehicule;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierVehicule);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
