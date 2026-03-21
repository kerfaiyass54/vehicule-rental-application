import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepairManagementComponent } from './repair-management.component';

describe('RepairManagementComponent', () => {
  let component: RepairManagementComponent;
  let fixture: ComponentFixture<RepairManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepairManagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RepairManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
