import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultSubscriptionComponent } from './consult-subscription.component';

describe('ConsultSubscriptionComponent', () => {
  let component: ConsultSubscriptionComponent;
  let fixture: ComponentFixture<ConsultSubscriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultSubscriptionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsultSubscriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
