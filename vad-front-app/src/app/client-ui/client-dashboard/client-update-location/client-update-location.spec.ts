import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientUpdateLocation } from './client-update-location';

describe('ClientUpdateLocation', () => {
  let component: ClientUpdateLocation;
  let fixture: ComponentFixture<ClientUpdateLocation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientUpdateLocation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientUpdateLocation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
