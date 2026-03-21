import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierHome } from './supplier-home';

describe('SupplierHome', () => {
  let component: SupplierHome;
  let fixture: ComponentFixture<SupplierHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierHome]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierHome);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
