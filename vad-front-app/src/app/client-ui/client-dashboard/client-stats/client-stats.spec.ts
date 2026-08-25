import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientStats } from './client-stats';

describe('ClientStats', () => {
  let component: ClientStats;
  let fixture: ComponentFixture<ClientStats>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientStats]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientStats);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
