import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierDashboard } from './supplier-dashboard';

describe('SupplierDashboard', () => {
  let component: SupplierDashboard;
  let fixture: ComponentFixture<SupplierDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
