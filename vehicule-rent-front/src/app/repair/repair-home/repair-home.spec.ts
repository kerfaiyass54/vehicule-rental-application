import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepairHome } from './repair-home';

describe('RepairHome', () => {
  let component: RepairHome;
  let fixture: ComponentFixture<RepairHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepairHome]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RepairHome);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
