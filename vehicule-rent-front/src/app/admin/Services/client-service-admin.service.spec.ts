import { TestBed } from '@angular/core/testing';

import { ClientServiceAdminService } from './client-service-admin.service';

describe('ClientServiceAdminService', () => {
  let service: ClientServiceAdminService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientServiceAdminService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
