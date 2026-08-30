import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepairTickets } from './repair-tickets';

describe('RepairTickets', () => {
  let component: RepairTickets;
  let fixture: ComponentFixture<RepairTickets>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepairTickets]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RepairTickets);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
