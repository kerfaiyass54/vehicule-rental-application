import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyVehiculeComponent } from './buy-vehicule.component';

describe('BuyVehiculeComponent', () => {
  let component: BuyVehiculeComponent;
  let fixture: ComponentFixture<BuyVehiculeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BuyVehiculeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BuyVehiculeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
