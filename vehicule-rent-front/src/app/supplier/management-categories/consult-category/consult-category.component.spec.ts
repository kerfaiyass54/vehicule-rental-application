import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultCategoryComponent } from './consult-category.component';

describe('ConsultCategoryComponent', () => {
  let component: ConsultCategoryComponent;
  let fixture: ComponentFixture<ConsultCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultCategoryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsultCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
