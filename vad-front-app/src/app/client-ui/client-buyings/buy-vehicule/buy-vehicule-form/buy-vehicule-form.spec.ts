import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyVehiculeForm } from './buy-vehicule-form';

describe('BuyVehiculeForm', () => {
  let component: BuyVehiculeForm;
  let fixture: ComponentFixture<BuyVehiculeForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BuyVehiculeForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BuyVehiculeForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
