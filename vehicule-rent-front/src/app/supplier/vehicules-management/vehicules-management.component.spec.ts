import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehiculesManagementComponent } from './vehicules-management.component';

describe('VehiculesManagementComponent', () => {
  let component: VehiculesManagementComponent;
  let fixture: ComponentFixture<VehiculesManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehiculesManagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(VehiculesManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
