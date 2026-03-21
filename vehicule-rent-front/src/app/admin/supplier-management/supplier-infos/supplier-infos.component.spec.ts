import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierInfosComponent } from './supplier-infos.component';

describe('SupplierInfosComponent', () => {
  let component: SupplierInfosComponent;
  let fixture: ComponentFixture<SupplierInfosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierInfosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SupplierInfosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
