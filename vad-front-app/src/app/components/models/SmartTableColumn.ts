export interface SmartTableColumn {
  key: string;
  label: string;
  icon?: string;
  type?: 'avatar' | 'progress' | 'badge';
  sortable?: boolean;
}
