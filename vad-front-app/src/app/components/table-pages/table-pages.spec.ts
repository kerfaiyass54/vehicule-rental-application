import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TablePages } from './table-pages';

describe('TablePages', () => {
  let component: TablePages;
  let fixture: ComponentFixture<TablePages>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TablePages]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TablePages);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
