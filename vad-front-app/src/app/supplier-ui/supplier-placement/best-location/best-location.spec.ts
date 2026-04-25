import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BestLocation } from './best-location';

describe('BestLocation', () => {
  let component: BestLocation;
  let fixture: ComponentFixture<BestLocation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BestLocation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BestLocation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
