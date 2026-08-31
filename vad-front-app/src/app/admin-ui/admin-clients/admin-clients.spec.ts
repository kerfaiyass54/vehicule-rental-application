import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminClients } from './admin-clients';

describe('AdminClients', () => {
  let component: AdminClients;
  let fixture: ComponentFixture<AdminClients>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminClients]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminClients);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
