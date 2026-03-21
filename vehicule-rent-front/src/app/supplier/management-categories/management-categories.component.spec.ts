import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagementCategoriesComponent } from './management-categories.component';

describe('ManagementCategoriesComponent', () => {
  let component: ManagementCategoriesComponent;
  let fixture: ComponentFixture<ManagementCategoriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManagementCategoriesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManagementCategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
