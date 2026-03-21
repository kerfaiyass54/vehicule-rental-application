import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientHome } from './client-home';

describe('ClientHome', () => {
  let component: ClientHome;
  let fixture: ComponentFixture<ClientHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientHome]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientHome);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
