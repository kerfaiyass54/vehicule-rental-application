import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscriptionForm } from './subscription-form';

describe('SubscriptionForm', () => {
  let component: SubscriptionForm;
  let fixture: ComponentFixture<SubscriptionForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubscriptionForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubscriptionForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
