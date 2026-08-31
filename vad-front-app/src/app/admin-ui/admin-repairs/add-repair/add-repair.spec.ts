import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRepair } from './add-repair';

describe('AddRepair', () => {
  let component: AddRepair;
  let fixture: ComponentFixture<AddRepair>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddRepair]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddRepair);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
