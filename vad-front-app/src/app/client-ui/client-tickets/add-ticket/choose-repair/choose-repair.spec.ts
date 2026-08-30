import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseRepair } from './choose-repair';

describe('ChooseRepair', () => {
  let component: ChooseRepair;
  let fixture: ComponentFixture<ChooseRepair>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChooseRepair]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChooseRepair);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
