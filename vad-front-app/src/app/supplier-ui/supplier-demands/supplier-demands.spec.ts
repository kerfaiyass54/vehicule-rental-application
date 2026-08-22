import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierDemands } from './supplier-demands';

describe('SupplierDemands', () => {
  let component: SupplierDemands;
  let fixture: ComponentFixture<SupplierDemands>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierDemands]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierDemands);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
