import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateAdressComponent } from './update-adress.component';

describe('UpdateAdressComponent', () => {
  let component: UpdateAdressComponent;
  let fixture: ComponentFixture<UpdateAdressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateAdressComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateAdressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
