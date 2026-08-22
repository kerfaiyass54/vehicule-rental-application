import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierBuyings } from './supplier-buyings';

describe('SupplierBuyings', () => {
  let component: SupplierBuyings;
  let fixture: ComponentFixture<SupplierBuyings>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierBuyings]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierBuyings);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
