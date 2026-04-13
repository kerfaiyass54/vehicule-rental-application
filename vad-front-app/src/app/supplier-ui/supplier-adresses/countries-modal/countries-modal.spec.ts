import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CountriesModal } from './countries-modal';

describe('CountriesModal', () => {
  let component: CountriesModal;
  let fixture: ComponentFixture<CountriesModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CountriesModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CountriesModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
