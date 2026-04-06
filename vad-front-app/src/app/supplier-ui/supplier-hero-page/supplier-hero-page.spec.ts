import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierHeroPage } from './supplier-hero-page';

describe('SupplierHeroPage', () => {
  let component: SupplierHeroPage;
  let fixture: ComponentFixture<SupplierHeroPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierHeroPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierHeroPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
