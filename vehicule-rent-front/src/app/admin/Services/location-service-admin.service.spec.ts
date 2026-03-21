import { TestBed } from '@angular/core/testing';

import { LocationServiceAdminService } from './location-service-admin.service';

describe('LocationServiceAdminService', () => {
  let service: LocationServiceAdminService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocationServiceAdminService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
