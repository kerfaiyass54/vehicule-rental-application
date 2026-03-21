import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LaunchRepairComponent } from './launch-repair.component';

describe('LaunchRepairComponent', () => {
  let component: LaunchRepairComponent;
  let fixture: ComponentFixture<LaunchRepairComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LaunchRepairComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LaunchRepairComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
