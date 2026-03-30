import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoRole } from './no-role';

describe('NoRole', () => {
  let component: NoRole;
  let fixture: ComponentFixture<NoRole>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NoRole]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NoRole);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
