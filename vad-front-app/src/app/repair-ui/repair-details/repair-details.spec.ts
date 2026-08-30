import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepairDetails } from './repair-details';

describe('RepairDetails', () => {
  let component: RepairDetails;
  let fixture: ComponentFixture<RepairDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepairDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RepairDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
