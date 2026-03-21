import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketsManagementComponent } from './tickets-management.component';

describe('TicketsManagementComponent', () => {
  let component: TicketsManagementComponent;
  let fixture: ComponentFixture<TicketsManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketsManagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TicketsManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
