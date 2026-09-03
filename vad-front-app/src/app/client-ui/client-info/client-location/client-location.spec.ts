import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientLocation } from './client-location';

describe('ClientLocation', () => {
  let component: ClientLocation;
  let fixture: ComponentFixture<ClientLocation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientLocation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientLocation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
