import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyVehicule } from './buy-vehicule';

describe('BuyVehicule', () => {
  let component: BuyVehicule;
  let fixture: ComponentFixture<BuyVehicule>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BuyVehicule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BuyVehicule);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
