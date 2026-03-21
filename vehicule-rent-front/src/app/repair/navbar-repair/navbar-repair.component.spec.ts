import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarRepairComponent } from './navbar-repair.component';

describe('NavbarRepairComponent', () => {
  let component: NavbarRepairComponent;
  let fixture: ComponentFixture<NavbarRepairComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarRepairComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NavbarRepairComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
