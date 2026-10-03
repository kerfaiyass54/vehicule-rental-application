export interface LocationAnalysisRecord {
  id: string;
  requested_by: string | null;
  period_start: string;
  period_end: string;
  analysis: Record<string, unknown>;
  created_at: string;
}
