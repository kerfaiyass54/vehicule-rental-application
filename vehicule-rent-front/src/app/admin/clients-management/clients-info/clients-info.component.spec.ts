import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientsInfoComponent } from './clients-info.component';

describe('ClientsInfoComponent', () => {
  let component: ClientsInfoComponent;
  let fixture: ComponentFixture<ClientsInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientsInfoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ClientsInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
