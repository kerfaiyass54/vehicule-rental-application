import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateProfileModal } from './update-profile-modal';

describe('UpdateProfileModal', () => {
  let component: UpdateProfileModal;
  let fixture: ComponentFixture<UpdateProfileModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateProfileModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateProfileModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
