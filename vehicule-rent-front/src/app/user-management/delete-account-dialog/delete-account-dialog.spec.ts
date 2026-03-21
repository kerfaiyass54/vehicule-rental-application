import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteAccountDialog } from './delete-account-dialog';

describe('DeleteAccountDialog', () => {
  let component: DeleteAccountDialog;
  let fixture: ComponentFixture<DeleteAccountDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteAccountDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteAccountDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
