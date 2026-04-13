import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationsModal } from './locations-modal';

describe('LocationsModal', () => {
  let component: LocationsModal;
  let fixture: ComponentFixture<LocationsModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LocationsModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LocationsModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
