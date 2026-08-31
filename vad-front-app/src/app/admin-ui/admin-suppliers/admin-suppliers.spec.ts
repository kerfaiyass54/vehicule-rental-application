import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSuppliers } from './admin-suppliers';

describe('AdminSuppliers', () => {
  let component: AdminSuppliers;
  let fixture: ComponentFixture<AdminSuppliers>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSuppliers]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSuppliers);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
