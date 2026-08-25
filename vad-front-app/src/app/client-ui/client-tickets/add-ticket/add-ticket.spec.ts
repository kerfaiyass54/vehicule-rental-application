import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTicket } from './add-ticket';

describe('AddTicket', () => {
  let component: AddTicket;
  let fixture: ComponentFixture<AddTicket>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddTicket]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddTicket);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
