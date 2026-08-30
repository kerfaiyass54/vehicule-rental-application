import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepairInfo } from './repair-info';

describe('RepairInfo', () => {
  let component: RepairInfo;
  let fixture: ComponentFixture<RepairInfo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepairInfo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RepairInfo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
