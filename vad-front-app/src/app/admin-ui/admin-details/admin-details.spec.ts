import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminDetails } from './admin-details';

describe('AdminDetails', () => {
  let component: AdminDetails;
  let fixture: ComponentFixture<AdminDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
