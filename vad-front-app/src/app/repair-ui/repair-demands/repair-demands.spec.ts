import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepairDemands } from './repair-demands';

describe('RepairDemands', () => {
  let component: RepairDemands;
  let fixture: ComponentFixture<RepairDemands>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepairDemands]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RepairDemands);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
