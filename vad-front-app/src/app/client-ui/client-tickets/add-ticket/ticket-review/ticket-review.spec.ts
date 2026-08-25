import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketReview } from './ticket-review';

describe('TicketReview', () => {
  let component: TicketReview;
  let fixture: ComponentFixture<TicketReview>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketReview]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketReview);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
