import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddressesModal } from './addresses-modal';

describe('AddressesModal', () => {
  let component: AddressesModal;
  let fixture: ComponentFixture<AddressesModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddressesModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddressesModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
