import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateUser } from './update-user';

describe('UpdateUser', () => {
  let component: UpdateUser;
  let fixture: ComponentFixture<UpdateUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateUser);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
