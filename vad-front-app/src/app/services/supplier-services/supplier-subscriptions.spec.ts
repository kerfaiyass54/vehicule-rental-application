import { TestBed } from '@angular/core/testing';

import { SupplierSubscriptions } from './supplier-subscriptions';

describe('SupplierSubscriptions', () => {
  let service: SupplierSubscriptions;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierSubscriptions);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
