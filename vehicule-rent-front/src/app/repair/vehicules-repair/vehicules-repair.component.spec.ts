import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehiculesRepairComponent } from './vehicules-repair.component';

describe('VehiculesRepairComponent', () => {
  let component: VehiculesRepairComponent;
  let fixture: ComponentFixture<VehiculesRepairComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehiculesRepairComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(VehiculesRepairComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
