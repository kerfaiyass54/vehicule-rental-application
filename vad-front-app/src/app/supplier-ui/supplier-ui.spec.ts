import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierUi } from './supplier-ui';

describe('SupplierUi', () => {
  let component: SupplierUi;
  let fixture: ComponentFixture<SupplierUi>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierUi]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierUi);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
