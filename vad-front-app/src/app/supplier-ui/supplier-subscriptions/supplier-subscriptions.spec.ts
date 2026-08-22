import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierSubscriptions } from './supplier-subscriptions';

describe('SupplierSubscriptions', () => {
  let component: SupplierSubscriptions;
  let fixture: ComponentFixture<SupplierSubscriptions>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierSubscriptions]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierSubscriptions);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
