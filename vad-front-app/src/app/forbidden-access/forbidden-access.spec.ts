import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForbiddenAccess } from './forbidden-access';

describe('ForbiddenAccess', () => {
  let component: ForbiddenAccess;
  let fixture: ComponentFixture<ForbiddenAccess>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ForbiddenAccess]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ForbiddenAccess);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
