import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandInfo } from './demand-info';

describe('DemandInfo', () => {
  let component: DemandInfo;
  let fixture: ComponentFixture<DemandInfo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DemandInfo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DemandInfo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
