import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavBarClientComponent } from './nav-bar-client.component';

describe('NavBarClientComponent', () => {
  let component: NavBarClientComponent;
  let fixture: ComponentFixture<NavBarClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavBarClientComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NavBarClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
