import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import Keycloak from 'keycloak-js';
import { Subject, takeUntil } from 'rxjs';
import { LocationAnalysisRecord, LocationEdaManagementService } from '../../../services/admin-services/location-eda-management.service';

@Component({
  selector: 'app-location-analysis-list',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './analysis-list.html',
  styleUrl: './analysis-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LocationAnalysisList implements OnInit, OnDestroy {
  private readonly service = inject(LocationEdaManagementService);
  private readonly router = inject(Router);
  private readonly keycloak = inject(Keycloak);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly destroy$ = new Subject<void>();

  email = '';
  loading = false;
  error = '';
  records: LocationAnalysisRecord[] = [];
  pageIndex = 0;
  readonly pageSize = 8;
  selectedRecord: LocationAnalysisRecord | null = null;

  ngOnInit(): void {
    const email = this.keycloak.tokenParsed?.['email'] as string | undefined;
    if (!email) {
      this.error = 'Your email could not be retrieved from the Keycloak token.';
      return;
    }
    this.email = email;
    this.loadAnalyses();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadAnalyses(): void {
    const email = this.email.trim();
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      this.error = 'Enter a valid email address to find saved reports.';
      this.records = [];
      this.cdr.markForCheck();
      return;
    }

    this.loading = true;
    this.error = '';
    this.pageIndex = 0;
    this.cdr.markForCheck();
    this.service.getAnalyses(email, 100).pipe(takeUntil(this.destroy$)).subscribe({
      next: records => {
        this.records = records;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: err => {
        console.error('Unable to load saved location analyses:', err);
        this.records = [];
        this.loading = false;
        this.error = 'Saved analyses could not be loaded. Check that the EDA service is available and try again.';
        this.cdr.markForCheck();
      },
    });
  }

  goBack(): void {
    void this.router.navigate(['/admin/location-eda']);
  }

  openDetails(record: LocationAnalysisRecord): void {
    this.selectedRecord = record;
  }

  closeDetails(): void {
    this.selectedRecord = null;
  }

  get pageRecords(): LocationAnalysisRecord[] {
    const start = this.pageIndex * this.pageSize;
    return this.records.slice(start, start + this.pageSize);
  }

  get pageCount(): number {
    return Math.max(1, Math.ceil(this.records.length / this.pageSize));
  }

  get firstVisible(): number {
    return this.records.length ? this.pageIndex * this.pageSize + 1 : 0;
  }

  get lastVisible(): number {
    return Math.min((this.pageIndex + 1) * this.pageSize, this.records.length);
  }

  setPage(page: number): void {
    this.pageIndex = Math.min(Math.max(page, 0), this.pageCount - 1);
  }

  count(record: LocationAnalysisRecord, key: string): number {
    const value = record.analysis[key];
    return typeof value === 'number' ? value : 0;
  }

  forecast(record: LocationAnalysisRecord): number | null {
    const value = record.analysis['forecast'];
    if (!value || typeof value !== 'object') return null;
    const expected = (value as { expected_events?: unknown }).expected_events;
    return typeof expected === 'number' ? expected : null;
  }
}
