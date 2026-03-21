import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultRepairComponent } from './consult-repair.component';

describe('ConsultRepairComponent', () => {
  let component: ConsultRepairComponent;
  let fixture: ComponentFixture<ConsultRepairComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultRepairComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsultRepairComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
