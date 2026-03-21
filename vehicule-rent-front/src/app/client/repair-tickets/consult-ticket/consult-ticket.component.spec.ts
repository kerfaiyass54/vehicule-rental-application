import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultTicketComponent } from './consult-ticket.component';

describe('ConsultTicketComponent', () => {
  let component: ConsultTicketComponent;
  let fixture: ComponentFixture<ConsultTicketComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultTicketComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsultTicketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
