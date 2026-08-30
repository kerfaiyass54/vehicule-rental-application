import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepairUi } from './repair-ui';

describe('RepairUi', () => {
  let component: RepairUi;
  let fixture: ComponentFixture<RepairUi>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepairUi]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RepairUi);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
