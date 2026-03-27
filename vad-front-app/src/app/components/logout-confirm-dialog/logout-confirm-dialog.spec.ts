import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogoutConfirmDialog } from './logout-confirm-dialog';

describe('LogoutConfirmDialog', () => {
  let component: LogoutConfirmDialog;
  let fixture: ComponentFixture<LogoutConfirmDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogoutConfirmDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogoutConfirmDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
