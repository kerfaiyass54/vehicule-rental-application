import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchSupplier } from './search-supplier';

describe('SearchSupplier', () => {
  let component: SearchSupplier;
  let fixture: ComponentFixture<SearchSupplier>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchSupplier]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchSupplier);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
