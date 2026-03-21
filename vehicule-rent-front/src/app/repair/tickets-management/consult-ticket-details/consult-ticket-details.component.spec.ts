import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultTicketDetailsComponent } from './consult-ticket-details.component';

describe('ConsultTicketDetailsComponent', () => {
  let component: ConsultTicketDetailsComponent;
  let fixture: ComponentFixture<ConsultTicketDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultTicketDetailsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsultTicketDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
