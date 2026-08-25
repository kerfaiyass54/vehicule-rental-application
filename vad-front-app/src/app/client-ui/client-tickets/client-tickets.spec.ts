import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientTickets } from './client-tickets';

describe('ClientTickets', () => {
  let component: ClientTickets;
  let fixture: ComponentFixture<ClientTickets>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientTickets]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientTickets);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
