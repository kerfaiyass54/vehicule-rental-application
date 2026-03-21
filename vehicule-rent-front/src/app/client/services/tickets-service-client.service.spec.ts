import { TestBed } from '@angular/core/testing';

import { TicketsServiceClientService } from './tickets-service-client.service';

describe('TicketsServiceClientService', () => {
  let service: TicketsServiceClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicketsServiceClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
