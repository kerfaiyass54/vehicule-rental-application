import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultCategoriesComponent } from './consult-categories.component';

describe('ConsultCategoriesComponent', () => {
  let component: ConsultCategoriesComponent;
  let fixture: ComponentFixture<ConsultCategoriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultCategoriesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsultCategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
