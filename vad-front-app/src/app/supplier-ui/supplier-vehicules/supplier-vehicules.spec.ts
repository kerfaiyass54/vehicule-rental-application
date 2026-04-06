import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierVehicules } from './supplier-vehicules';

describe('SupplierVehicules', () => {
  let component: SupplierVehicules;
  let fixture: ComponentFixture<SupplierVehicules>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierVehicules]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierVehicules);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
