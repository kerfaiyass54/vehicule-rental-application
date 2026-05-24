import { TestBed } from '@angular/core/testing';

import { RecommandService } from './recommand-service';

describe('RecommandService', () => {
  let service: RecommandService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecommandService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
