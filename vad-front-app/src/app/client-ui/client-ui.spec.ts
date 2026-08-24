import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientUi } from './client-ui';

describe('ClientUi', () => {
  let component: ClientUi;
  let fixture: ComponentFixture<ClientUi>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientUi]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientUi);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
