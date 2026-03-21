import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultVehiculeComponent } from './consult-vehicule.component';

describe('ConsultVehiculeComponent', () => {
  let component: ConsultVehiculeComponent;
  let fixture: ComponentFixture<ConsultVehiculeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultVehiculeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsultVehiculeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
