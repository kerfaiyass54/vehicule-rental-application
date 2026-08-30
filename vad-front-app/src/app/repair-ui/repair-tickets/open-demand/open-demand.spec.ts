import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenDemand } from './open-demand';

describe('OpenDemand', () => {
  let component: OpenDemand;
  let fixture: ComponentFixture<OpenDemand>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OpenDemand]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OpenDemand);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
