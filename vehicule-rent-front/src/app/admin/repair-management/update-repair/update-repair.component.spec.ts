import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateRepairComponent } from './update-repair.component';

describe('UpdateRepairComponent', () => {
  let component: UpdateRepairComponent;
  let fixture: ComponentFixture<UpdateRepairComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateRepairComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateRepairComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
