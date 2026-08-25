import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchVehicule } from './search-vehicule';

describe('SearchVehicule', () => {
  let component: SearchVehicule;
  let fixture: ComponentFixture<SearchVehicule>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchVehicule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchVehicule);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
