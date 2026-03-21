import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SessionDetailsDialog } from './session-details-dialog';

describe('SessionDetailsDialog', () => {
  let component: SessionDetailsDialog;
  let fixture: ComponentFixture<SessionDetailsDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SessionDetailsDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SessionDetailsDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
