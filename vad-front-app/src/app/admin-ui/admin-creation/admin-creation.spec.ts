import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCreation } from './admin-creation';

describe('AdminCreation', () => {
  let component: AdminCreation;
  let fixture: ComponentFixture<AdminCreation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminCreation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminCreation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
