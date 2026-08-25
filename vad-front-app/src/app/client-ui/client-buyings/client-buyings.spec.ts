import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientBuyings } from './client-buyings';

describe('ClientBuyings', () => {
  let component: ClientBuyings;
  let fixture: ComponentFixture<ClientBuyings>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientBuyings]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientBuyings);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
