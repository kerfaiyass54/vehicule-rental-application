import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandManagementComponent } from './demand-management.component';

describe('DemandManagementComponent', () => {
  let component: DemandManagementComponent;
  let fixture: ComponentFixture<DemandManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DemandManagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DemandManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
