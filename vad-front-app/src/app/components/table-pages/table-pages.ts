import { AfterViewInit, Component, Input, ViewChild, OnChanges, SimpleChanges } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import {
  MatCell,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderRow,
  MatRow,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';

@Component({
  selector: 'table-pages',
  standalone: true,
  templateUrl: './table-pages.html',
  imports: [
    MatPaginator,
    MatHeaderRow,
    MatRow,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell
  ],
  styleUrls: ['./table-pages.css']
})
export class TablePages implements AfterViewInit, OnChanges {

  /** dynamic column names */
  @Input() displayedColumns: string[] = [];

  /** column labels override */
  @Input() columnLabels: Record<string, string> = {};

  /** API data */
  @Input() data: any[] = [];

  /** paginator size options */
  @Input() pageSizeOptions: number[] = [5, 10, 20];

  dataSource = new MatTableDataSource<any>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngAfterViewInit() {

    this.dataSource.paginator = this.paginator;
  }

  ngOnChanges(changes: SimpleChanges) {

    if (changes['data']) {

      this.dataSource.data = this.data;
    }
  }

  getLabel(column: string): string {

    return this.columnLabels[column] ?? column;
  }
}
