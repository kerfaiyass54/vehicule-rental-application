import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountInfo } from './account-info';

describe('AccountInfo', () => {
  let component: AccountInfo;
  let fixture: ComponentFixture<AccountInfo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountInfo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountInfo);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
