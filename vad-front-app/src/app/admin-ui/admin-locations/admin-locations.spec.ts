import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminLocations } from './admin-locations';

describe('AdminLocations', () => {
  let component: AdminLocations;
  let fixture: ComponentFixture<AdminLocations>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminLocations]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminLocations);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
