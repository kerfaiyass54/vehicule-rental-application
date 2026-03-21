import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavBarSupplierComponent } from './nav-bar-supplier.component';

describe('NavBarSupplierComponent', () => {
  let component: NavBarSupplierComponent;
  let fixture: ComponentFixture<NavBarSupplierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavBarSupplierComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NavBarSupplierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
