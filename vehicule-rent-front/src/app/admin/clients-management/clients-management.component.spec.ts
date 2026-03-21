import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientsManagementComponent } from './clients-management.component';

describe('ClientsManagementComponent', () => {
  let component: ClientsManagementComponent;
  let fixture: ComponentFixture<ClientsManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientsManagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ClientsManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
