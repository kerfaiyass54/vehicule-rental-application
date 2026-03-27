import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuppleirDetails } from './suppleir-details';

describe('SuppleirDetails', () => {
  let component: SuppleirDetails;
  let fixture: ComponentFixture<SuppleirDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SuppleirDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SuppleirDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
