import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyReview } from './buy-review';

describe('BuyReview', () => {
  let component: BuyReview;
  let fixture: ComponentFixture<BuyReview>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BuyReview]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BuyReview);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
