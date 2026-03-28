import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableData } from './table-data';

describe('TableData', () => {
  let component: TableData;
  let fixture: ComponentFixture<TableData>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TableData]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TableData);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
