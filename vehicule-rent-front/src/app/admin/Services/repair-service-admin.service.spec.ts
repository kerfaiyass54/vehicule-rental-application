import { TestBed } from '@angular/core/testing';

import { RepairServiceAdminService } from './repair-service-admin.service';

describe('RepairServiceAdminService', () => {
  let service: RepairServiceAdminService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RepairServiceAdminService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
