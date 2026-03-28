export interface TableFetchParams {
  page: number;          // 0-based page index
  size: number;          // rows per page
  sortField: string;     // active sort column key ('' if none)
  sortDir: 'asc' | 'desc' | '';
  filter: string;        // current filter string
}
