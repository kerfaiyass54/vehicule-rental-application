import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepairOperations } from './repair-operations';

describe('RepairOperations', () => {
  let component: RepairOperations;
  let fixture: ComponentFixture<RepairOperations>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepairOperations]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RepairOperations);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
