import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscriptionsDetailsComponent } from './subscriptions-details.component';

describe('SubscriptionsDetailsComponent', () => {
  let component: SubscriptionsDetailsComponent;
  let fixture: ComponentFixture<SubscriptionsDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubscriptionsDetailsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SubscriptionsDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
