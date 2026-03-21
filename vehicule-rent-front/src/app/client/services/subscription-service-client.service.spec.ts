import { TestBed } from '@angular/core/testing';

import { SubscriptionServiceClientService } from './subscription-service-client.service';

describe('SubscriptionServiceClientService', () => {
  let service: SubscriptionServiceClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubscriptionServiceClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
