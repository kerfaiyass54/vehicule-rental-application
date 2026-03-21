import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RenewSubscriptionComponent } from './renew-subscription.component';

describe('RenewSubscriptionComponent', () => {
  let component: RenewSubscriptionComponent;
  let fixture: ComponentFixture<RenewSubscriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RenewSubscriptionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RenewSubscriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
