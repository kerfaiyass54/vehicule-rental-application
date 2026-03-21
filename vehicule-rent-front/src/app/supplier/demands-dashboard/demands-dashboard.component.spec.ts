import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandsDashboardComponent } from './demands-dashboard.component';

describe('DemandsDashboardComponent', () => {
  let component: DemandsDashboardComponent;
  let fixture: ComponentFixture<DemandsDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DemandsDashboardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DemandsDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
