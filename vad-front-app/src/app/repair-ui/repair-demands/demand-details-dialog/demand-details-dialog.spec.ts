import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandDetailsDialog } from './demand-details-dialog';

describe('DemandDetailsDialog', () => {
  let component: DemandDetailsDialog;
  let fixture: ComponentFixture<DemandDetailsDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DemandDetailsDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DemandDetailsDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
