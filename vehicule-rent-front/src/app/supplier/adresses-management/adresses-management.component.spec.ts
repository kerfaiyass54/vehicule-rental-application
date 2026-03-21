import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdressesManagementComponent } from './adresses-management.component';

describe('AdressesManagementComponent', () => {
  let component: AdressesManagementComponent;
  let fixture: ComponentFixture<AdressesManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdressesManagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdressesManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
