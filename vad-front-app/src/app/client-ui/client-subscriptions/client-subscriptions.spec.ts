import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientSubscriptions } from './client-subscriptions';

describe('ClientSubscriptions', () => {
  let component: ClientSubscriptions;
  let fixture: ComponentFixture<ClientSubscriptions>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientSubscriptions]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientSubscriptions);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
