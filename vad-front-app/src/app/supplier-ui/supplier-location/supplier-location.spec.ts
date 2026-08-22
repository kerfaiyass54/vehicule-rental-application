import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierLocation } from './supplier-location';

describe('SupplierLocation', () => {
  let component: SupplierLocation;
  let fixture: ComponentFixture<SupplierLocation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierLocation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierLocation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
