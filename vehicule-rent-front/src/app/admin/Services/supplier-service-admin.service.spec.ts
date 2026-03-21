import { TestBed } from '@angular/core/testing';

import { SupplierServiceAdminService } from './supplier-service-admin.service';

describe('SupplierServiceAdminService', () => {
  let service: SupplierServiceAdminService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierServiceAdminService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
