import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import Keycloak from 'keycloak-js';
import { Chart, registerables } from 'chart.js';
import { Subject, takeUntil } from 'rxjs';
import { LocationAnalysisRecord, LocationEdaManagementService } from '../../../services/admin-services/location-eda-management.service';

Chart.register(...registerables);

interface CountByName { [key: string]: number; }
interface DailyActivity { date: string; event_count: number; }
interface AnalysisMetrics {
  event_count?: number;
  movement_count?: number;
  users_per_location?: CountByName;
  users_per_country?: CountByName;
  suppliers_per_location?: CountByName;
  suppliers_per_country?: CountByName;
  repairs_per_location?: CountByName;
  buyings_per_location?: CountByName;
  buyings_per_country?: CountByName;
  tickets_per_location?: CountByName;
  ticket_count?: number;
  location_stats?: Array<{
    location_id: string;
    location_name: string;
    country?: string | null;
    users: number;
    clients: number;
    suppliers: number;
    repairs: number;
    buyings: number;
    buyings_in_period: number;
    buyings_in: number;
    buyings_out: number;
    tickets: number;
    tickets_in_period: number;
    tickets_in: number;
    tickets_out: number;
    movements_in: number;
    movements_out: number;
  }>;
  movement_counts_by_entity?: CountByName;
  daily_activity?: DailyActivity[];
  forecast?: { expected_events?: number | null; status?: string; horizon_days?: number };
}

@Component({
  selector: 'app-location-analysis',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './location-analysis.html',
  styleUrl: './location-analysis.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LocationAnalysis implements OnInit, OnDestroy {
  @ViewChild('usersLocationChart') usersLocationChart?: ElementRef<HTMLCanvasElement>;
  @ViewChild('usersCountryChart') usersCountryChart?: ElementRef<HTMLCanvasElement>;
  @ViewChild('suppliersChart') suppliersChart?: ElementRef<HTMLCanvasElement>;
  @ViewChild('buyingsLocationChart') buyingsLocationChart?: ElementRef<HTMLCanvasElement>;
  @ViewChild('buyingsCountryChart') buyingsCountryChart?: ElementRef<HTMLCanvasElement>;
  @ViewChild('ticketsLocationChart') ticketsLocationChart?: ElementRef<HTMLCanvasElement>;
  @ViewChild('movementChart') movementChart?: ElementRef<HTMLCanvasElement>;
  @ViewChild('activityChart') activityChart?: ElementRef<HTMLCanvasElement>;

  private readonly service = inject(LocationEdaManagementService);
  private readonly router = inject(Router);
  private readonly keycloak = inject(Keycloak);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly destroy$ = new Subject<void>();
  private charts: Chart[] = [];

  email = '';
  locationSearch = '';
  selectedCountry = '';
  pageSize = 10;
  currentPage = 1;
  readonly pageSizes = [10, 25, 50];
  loading = false;
  error = '';
  result: LocationAnalysisRecord | null = null;

  ngOnInit(): void {
    const email = this.keycloak.tokenParsed?.['email'] as string | undefined;
    if (!email) {
      this.error = 'Your email could not be retrieved from the Keycloak token.';
      return;
    }
    this.email = email;
    this.runAnalysis();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.destroyCharts();
  }

  runAnalysis(): void {
    const email = this.email.trim();
    if (!this.isValidEmail(email)) {
      this.error = 'Enter a valid email address to run and save this analysis.';
      return;
    }

    this.loading = true;
    this.error = '';
    this.cdr.markForCheck();
    this.service.runAnalysis(email).pipe(takeUntil(this.destroy$)).subscribe({
      next: record => {
        this.result = record;
        this.loading = false;
        this.cdr.detectChanges();
        this.renderCharts();
      },
      error: err => {
        console.error('Unable to run location analysis:', err);
        this.loading = false;
        this.error = 'The analysis could not be generated. Check that the EDA service is available and try again.';
        this.cdr.markForCheck();
      },
    });
  }

  goBack(): void {
    void this.router.navigate(['/admin/location-eda']);
  }

  get metrics(): AnalysisMetrics {
    return (this.result?.analysis ?? {}) as AnalysisMetrics;
  }

  get expectedEvents(): number | null | undefined {
    return this.metrics.forecast?.expected_events;
  }

  get trackedUsers(): number {
    return Object.values(this.metrics.users_per_location ?? {}).reduce((total, count) => total + count, 0);
  }

  get buyingCount(): number {
    return Object.values(this.metrics.buyings_per_location ?? {}).reduce((total, count) => total + count, 0);
  }

  get locations(): NonNullable<AnalysisMetrics['location_stats']> {
    return this.metrics.location_stats ?? [];
  }

  get countries(): string[] {
    return [...new Set(this.locations.map(location => location.country).filter((country): country is string => !!country))]
      .sort((left, right) => left.localeCompare(right));
  }

  get filteredLocations(): NonNullable<AnalysisMetrics['location_stats']> {
    const search = this.locationSearch.trim().toLocaleLowerCase();
    return this.locations.filter(location => {
      const matchesCountry = !this.selectedCountry || location.country === this.selectedCountry;
      const searchable = `${location.location_name} ${location.location_id} ${location.country ?? ''}`.toLocaleLowerCase();
      return matchesCountry && (!search || searchable.includes(search));
    });
  }

  get paginatedLocations(): NonNullable<AnalysisMetrics['location_stats']> {
    const offset = (this.currentPage - 1) * this.pageSize;
    return this.filteredLocations.slice(offset, offset + this.pageSize);
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.filteredLocations.length / this.pageSize));
  }

  get firstVisibleLocation(): number {
    return this.filteredLocations.length ? (this.currentPage - 1) * this.pageSize + 1 : 0;
  }

  get lastVisibleLocation(): number {
    return Math.min(this.currentPage * this.pageSize, this.filteredLocations.length);
  }

  updateLocationFilters(): void {
    this.currentPage = 1;
  }

  changePage(amount: number): void {
    this.currentPage = Math.max(1, Math.min(this.totalPages, this.currentPage + amount));
  }

  private renderCharts(): void {
    this.destroyCharts();
    this.addBarChart(this.usersLocationChart, this.metrics.users_per_location, 'Users', '#2563eb');
    this.addBarChart(this.usersCountryChart, this.metrics.users_per_country, 'Users', '#4f46e5');
    this.addBarChart(this.suppliersChart, this.metrics.suppliers_per_location, 'Suppliers', '#16a34a');
    this.addBarChart(this.buyingsLocationChart, this.metrics.buyings_per_location, 'Buyings', '#f97316');
    this.addBarChart(this.buyingsCountryChart, this.metrics.buyings_per_country, 'Buyings', '#ea580c');
    this.addBarChart(this.ticketsLocationChart, this.metrics.tickets_per_location, 'Tickets', '#0891b2');
    this.addDoughnutChart(this.movementChart, this.metrics.movement_counts_by_entity);

    const activity = this.metrics.daily_activity ?? [];
    if (this.activityChart) {
      this.charts.push(new Chart(this.activityChart.nativeElement, {
        type: 'line',
        data: {
          labels: activity.map(item => item.date),
          datasets: [{
            label: 'Events per day',
            data: activity.map(item => item.event_count),
            borderColor: '#7c3aed',
            backgroundColor: 'rgba(124, 58, 237, 0.12)',
            fill: true,
            tension: 0.35,
          }],
        },
        options: this.chartOptions(),
      }));
    }
  }

  private addBarChart(
    canvas: ElementRef<HTMLCanvasElement> | undefined,
    counts: CountByName | undefined,
    label: string,
    color: string,
  ): void {
    if (!canvas) return;
    const entries = Object.entries(counts ?? {}).sort((a, b) => b[1] - a[1]);
    this.charts.push(new Chart(canvas.nativeElement, {
      type: 'bar',
      data: {
        labels: entries.map(([name]) => name),
        datasets: [{ label, data: entries.map(([, count]) => count), backgroundColor: color, borderRadius: 7 }],
      },
      options: this.chartOptions(),
    }));
  }

  private addDoughnutChart(
    canvas: ElementRef<HTMLCanvasElement> | undefined,
    counts: CountByName | undefined,
  ): void {
    if (!canvas) return;
    const entries = Object.entries(counts ?? {}).sort((a, b) => b[1] - a[1]);
    this.charts.push(new Chart(canvas.nativeElement, {
      type: 'doughnut',
      data: {
        labels: entries.map(([name]) => name),
        datasets: [{
          data: entries.map(([, count]) => count),
          backgroundColor: ['#2563eb', '#16a34a', '#f97316', '#9333ea', '#06b6d4', '#e11d48'],
          borderWidth: 0,
        }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        animation: { duration: 650 },
        plugins: { legend: { position: 'bottom', labels: { color: '#64748b', usePointStyle: true } } },
      },
    }));
  }

  private chartOptions(): Chart['options'] {
    return {
      responsive: true,
      maintainAspectRatio: false,
      animation: { duration: 650 },
      plugins: { legend: { display: false } },
      scales: {
        x: { grid: { display: false }, ticks: { color: '#64748b', maxRotation: 45, minRotation: 0 } },
        y: { beginAtZero: true, ticks: { precision: 0, color: '#64748b' }, grid: { color: '#eef2f7' } },
      },
    };
  }

  private destroyCharts(): void {
    this.charts.forEach(chart => chart.destroy());
    this.charts = [];
  }

  private isValidEmail(value: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
  }
}
