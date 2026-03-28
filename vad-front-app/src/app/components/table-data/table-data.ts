import {
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import {TableFetchParams} from '../models/TableFetchParams';
import {SmartTableColumn} from '../models/SmartTableColumn';


@Component({
  selector: 'app-table-data',
  standalone: true,
  imports: [CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,],
  templateUrl: './table-data.html',
  styleUrl: './table-data.css',
})
export class TableData implements OnInit, AfterViewInit, OnChanges {

  // ── Presentation inputs ──────────────────────────────────────────────────

  /** Main heading shown above the table. */
  @Input() title = 'Data Table';

  /** Optional subtitle / description. */
  @Input() subtitle = '';

  /** Optional emoji / icon shown beside the title. */
  @Input() icon = '';

  /** Placeholder text for the filter input. */
  @Input() filterPlaceholder = 'Search…';

  /** Show the stats bar above the table. */
  @Input() showStats = true;

  // ── Data inputs ──────────────────────────────────────────────────────────

  /**
   * Column definitions. Order determines display order.
   * Example:
   * ```ts
   * columns = [
   *   { key: 'name',     label: 'Name',     icon: '👤', type: 'avatar'   },
   *   { key: 'progress', label: 'Progress', icon: '📊', type: 'progress' },
   *   { key: 'status',   label: 'Status',   icon: '🏷',  type: 'badge'    },
   *   { key: 'joined',   label: 'Joined',   icon: '📅'                    },
   * ];
   * ```
   */
  @Input() columns: SmartTableColumn[] = [];

  /**
   * The current page of data to display.
   * Replace the array whenever you receive a response from your API.
   */
  @Input() data: Record<string, unknown>[] = [];

  /**
   * Total number of records on the server (used by the paginator).
   * Set this to `response.totalElements` or equivalent.
   */
  @Input() totalItems = 0;

  /** Show the loading overlay. */
  @Input() loading = false;

  // ── Pagination / sort inputs ─────────────────────────────────────────────

  /** Initial page size. */
  @Input() pageSize = 10;

  /** Available page size options. */
  @Input() pageSizeOptions: number[] = [5, 10, 25, 50];

  // ── Outputs ──────────────────────────────────────────────────────────────

  /**
   * Emitted whenever the user changes page, page size, sort, or filter.
   * Consume this to fire your API call.
   *
   * Example:
   * ```ts
   * onFetch(params: TableFetchParams) {
   *   this.myService.getUsers(params).subscribe(res => {
   *     this.tableData  = res.content;
   *     this.totalItems = res.totalElements;
   *   });
   * }
   * ```
   */
  @Output() fetchData = new EventEmitter<TableFetchParams>();

  /** Emitted when the user clicks a row. */
  @Output() rowClick = new EventEmitter<Record<string, unknown>>();

  // ── Internal state ───────────────────────────────────────────────────────

  displayedColumns: string[] = [];
  dataSource = new MatTableDataSource<Record<string, unknown>>([]);
  pageIndex = 0;

  private currentSort: Sort = { active: '', direction: '' };
  private currentFilter = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  // ── Lifecycle ────────────────────────────────────────────────────────────

  ngOnInit(): void {
    this.displayedColumns = this.columns.map(c => c.key);
  }

  ngAfterViewInit(): void {
    // We handle all pagination/sort server-side, so we intentionally
    // do NOT assign paginator/sort to dataSource.
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['columns']) {
      this.displayedColumns = this.columns.map(c => c.key);
    }
    if (changes['data']) {
      this.dataSource.data = this.data;
    }
  }

  // ── Event handlers ───────────────────────────────────────────────────────

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize  = event.pageSize;
    this.emit();
  }

  onSortChange(sort: Sort): void {
    this.currentSort = sort;
    this.pageIndex   = 0;   // reset to first page on sort change
    this.emit();
  }

  onFilterChange(event: Event | null): void {
    this.currentFilter = event
      ? (event.target as HTMLInputElement).value.trim()
      : '';
    this.pageIndex = 0;
    this.emit();
  }

  onRowClick(row: Record<string, unknown>): void {
    this.rowClick.emit(row);
  }

  // ── Helpers ──────────────────────────────────────────────────────────────

  /** Deterministic pastel colour based on the first character of a string. */
  getAvatarColor(value: string): string {
    const palette = [
      '#6366f1', '#8b5cf6', '#ec4899', '#f43f5e',
      '#f97316', '#eab308', '#22c55e', '#14b8a6',
      '#3b82f6', '#06b6d4',
    ];
    const idx = (value?.charCodeAt(0) ?? 0) % palette.length;
    return palette[idx];
  }

  // ── Private ──────────────────────────────────────────────────────────────

  private emit(): void {
    this.fetchData.emit({
      page:      this.pageIndex,
      size:      this.pageSize,
      sortField: this.currentSort.active ?? '',
      sortDir:   (this.currentSort.direction as 'asc' | 'desc' | '') || '',
      filter:    this.currentFilter,
    });
  }
}

