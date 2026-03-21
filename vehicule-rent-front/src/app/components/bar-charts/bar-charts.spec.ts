import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarCharts } from './bar-charts';

describe('BarCharts', () => {
  let component: BarCharts;
  let fixture: ComponentFixture<BarCharts>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BarCharts]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BarCharts);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
