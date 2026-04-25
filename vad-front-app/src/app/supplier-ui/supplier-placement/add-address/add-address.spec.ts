import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAddress } from './add-address';

describe('AddAddress', () => {
  let component: AddAddress;
  let fixture: ComponentFixture<AddAddress>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddAddress]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddAddress);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
