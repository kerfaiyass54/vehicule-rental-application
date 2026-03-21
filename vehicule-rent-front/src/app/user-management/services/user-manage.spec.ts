import { TestBed } from '@angular/core/testing';

import { UserManage } from './user-manage';

describe('UserManage', () => {
  let service: UserManage;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserManage);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
