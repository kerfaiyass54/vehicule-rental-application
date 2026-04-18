import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FreeLocation } from './free-location';

describe('FreeLocation', () => {
  let component: FreeLocation;
  let fixture: ComponentFixture<FreeLocation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FreeLocation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FreeLocation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
