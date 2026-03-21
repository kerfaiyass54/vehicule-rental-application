import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDemandComponent } from './create-demand.component';

describe('CreateDemandComponent', () => {
  let component: CreateDemandComponent;
  let fixture: ComponentFixture<CreateDemandComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateDemandComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateDemandComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
