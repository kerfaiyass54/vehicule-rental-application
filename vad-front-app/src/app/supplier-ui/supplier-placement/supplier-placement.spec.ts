import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierPlacement } from './supplier-placement';

describe('SupplierPlacement', () => {
  let component: SupplierPlacement;
  let fixture: ComponentFixture<SupplierPlacement>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierPlacement]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierPlacement);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
