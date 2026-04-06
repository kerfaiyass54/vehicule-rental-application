import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierAdresses } from './supplier-adresses';

describe('SupplierAdresses', () => {
  let component: SupplierAdresses;
  let fixture: ComponentFixture<SupplierAdresses>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierAdresses]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierAdresses);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
