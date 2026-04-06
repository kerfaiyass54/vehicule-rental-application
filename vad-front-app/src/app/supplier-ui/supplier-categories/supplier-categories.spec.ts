import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierCategories } from './supplier-categories';

describe('SupplierCategories', () => {
  let component: SupplierCategories;
  let fixture: ComponentFixture<SupplierCategories>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierCategories]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierCategories);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
