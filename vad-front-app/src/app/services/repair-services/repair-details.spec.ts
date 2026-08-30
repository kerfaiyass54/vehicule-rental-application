import { TestBed } from '@angular/core/testing';

import { RepairDetails } from './repair-details';

describe('RepairDetails', () => {
  let service: RepairDetails;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RepairDetails);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
