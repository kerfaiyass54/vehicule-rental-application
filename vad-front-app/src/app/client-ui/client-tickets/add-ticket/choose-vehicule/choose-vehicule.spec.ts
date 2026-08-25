import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseVehicule } from './choose-vehicule';

describe('ChooseVehicule', () => {
  let component: ChooseVehicule;
  let fixture: ComponentFixture<ChooseVehicule>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChooseVehicule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChooseVehicule);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
