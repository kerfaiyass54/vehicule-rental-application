import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSupplierAddress } from './add-supplier-address';

describe('AddSupplierAddress', () => {
  let component: AddSupplierAddress;
  let fixture: ComponentFixture<AddSupplierAddress>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddSupplierAddress]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddSupplierAddress);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
