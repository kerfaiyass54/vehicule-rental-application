import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepairTicketsComponent } from './repair-tickets.component';

describe('RepairTicketsComponent', () => {
  let component: RepairTicketsComponent;
  let fixture: ComponentFixture<RepairTicketsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepairTicketsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RepairTicketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
