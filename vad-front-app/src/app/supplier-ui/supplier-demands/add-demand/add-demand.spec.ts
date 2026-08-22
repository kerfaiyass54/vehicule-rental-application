import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddDemand } from './add-demand';

describe('AddDemand', () => {
  let component: AddDemand;
  let fixture: ComponentFixture<AddDemand>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddDemand]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddDemand);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
