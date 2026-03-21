import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateVehiculeComponent } from './update-vehicule.component';

describe('UpdateVehiculeComponent', () => {
  let component: UpdateVehiculeComponent;
  let fixture: ComponentFixture<UpdateVehiculeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateVehiculeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateVehiculeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
