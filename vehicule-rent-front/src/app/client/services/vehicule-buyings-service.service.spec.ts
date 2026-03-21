import { TestBed } from '@angular/core/testing';

import { VehiculeBuyingsServiceService } from './vehicule-buyings-service.service';

describe('VehiculeBuyingsServiceService', () => {
  let service: VehiculeBuyingsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehiculeBuyingsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
