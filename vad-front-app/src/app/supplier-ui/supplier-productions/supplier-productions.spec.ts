import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierProductions } from './supplier-productions';

describe('SupplierProductions', () => {
  let component: SupplierProductions;
  let fixture: ComponentFixture<SupplierProductions>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierProductions]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierProductions);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
