import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRepairs } from './admin-repairs';

describe('AdminRepairs', () => {
  let component: AdminRepairs;
  let fixture: ComponentFixture<AdminRepairs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminRepairs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminRepairs);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
