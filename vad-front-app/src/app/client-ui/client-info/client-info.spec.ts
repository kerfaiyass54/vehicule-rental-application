import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientInfo } from './client-info';

describe('ClientInfo', () => {
  let component: ClientInfo;
  let fixture: ComponentFixture<ClientInfo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientInfo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientInfo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
