import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultDemandComponent } from './consult-demand.component';

describe('ConsultDemandComponent', () => {
  let component: ConsultDemandComponent;
  let fixture: ComponentFixture<ConsultDemandComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultDemandComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsultDemandComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
