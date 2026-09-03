import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepairDashboard } from './repair-dashboard';

describe('RepairDashboard', () => {
  let component: RepairDashboard;
  let fixture: ComponentFixture<RepairDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepairDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RepairDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
