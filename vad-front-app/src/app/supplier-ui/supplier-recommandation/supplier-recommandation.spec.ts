import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierRecommandation } from './supplier-recommandation';

describe('SupplierRecommandation', () => {
  let component: SupplierRecommandation;
  let fixture: ComponentFixture<SupplierRecommandation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierRecommandation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierRecommandation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
