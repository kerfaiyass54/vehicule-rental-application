import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehiculesBuyingsComponent } from './vehicules-buyings.component';

describe('VehiculesBuyingsComponent', () => {
  let component: VehiculesBuyingsComponent;
  let fixture: ComponentFixture<VehiculesBuyingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehiculesBuyingsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(VehiculesBuyingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
