import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientVehicules } from './client-vehicules';

describe('ClientVehicules', () => {
  let component: ClientVehicules;
  let fixture: ComponentFixture<ClientVehicules>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientVehicules]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientVehicules);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
