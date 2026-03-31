import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatePasswordModal } from './update-password-modal';

describe('UpdatePasswordModal', () => {
  let component: UpdatePasswordModal;
  let fixture: ComponentFixture<UpdatePasswordModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdatePasswordModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdatePasswordModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
