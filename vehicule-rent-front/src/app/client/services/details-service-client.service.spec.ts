import { TestBed } from '@angular/core/testing';

import { DetailsServiceClientService } from './details-service-client.service';

describe('DetailsServiceClientService', () => {
  let service: DetailsServiceClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DetailsServiceClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
