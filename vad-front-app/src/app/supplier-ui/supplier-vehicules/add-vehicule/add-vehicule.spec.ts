import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddVehicule } from './add-vehicule';

describe('AddVehicule', () => {
  let component: AddVehicule;
  let fixture: ComponentFixture<AddVehicule>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddVehicule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddVehicule);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
