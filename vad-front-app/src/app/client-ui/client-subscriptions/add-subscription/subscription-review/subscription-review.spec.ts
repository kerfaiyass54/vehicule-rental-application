import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscriptionReview } from './subscription-review';

describe('SubscriptionReview', () => {
  let component: SubscriptionReview;
  let fixture: ComponentFixture<SubscriptionReview>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubscriptionReview]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubscriptionReview);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
