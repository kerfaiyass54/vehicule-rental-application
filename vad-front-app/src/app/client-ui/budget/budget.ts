import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { Subject, finalize, switchMap, takeUntil, timer } from 'rxjs';
import Keycloak from 'keycloak-js';
import { BudgetService } from '../../services/client-services/budget.service';
import { BudgetEvent } from '../models/budget-event.model';

Chart.register(...registerables);

@Component({
  selector: 'app-budget',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './budget.html',
  styleUrl: './budget.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Budget implements AfterViewInit, OnDestroy {
  private readonly keycloak = inject(Keycloak);
  private readonly budgetService = inject(BudgetService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly destroy$ = new Subject<void>();
  private chart?: Chart;

  readonly events = signal<BudgetEvent[]>([]);
  readonly loading = signal(true);
  readonly error = signal(false);

  ngAfterViewInit(): void {
    const email = this.keycloak.tokenParsed?.['email'] as string | undefined;
    if (!email?.trim()) {
      this.loading.set(false);
      this.error.set(true);
      return;
    }

    this.budgetService.saveCurrentBudget(email.trim()).pipe(
      switchMap(() => timer(300)),
      switchMap(() => this.budgetService.getHistory(email.trim())),
      takeUntil(this.destroy$),
      finalize(() => {
        this.loading.set(false);
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: events => {
        this.events.set(events);
        this.cdr.markForCheck();
        setTimeout(() => this.createChart());
      },
      error: error => {
        console.error('Unable to load budget history:', error);
        this.error.set(true);
        this.cdr.markForCheck();
      },
    });
  }

  ngOnDestroy(): void {
    this.chart?.destroy();
    this.destroy$.next();
    this.destroy$.complete();
  }

  refresh(): void {
    window.location.reload();
  }

  formatDate(value: string): string {
    return new Intl.DateTimeFormat(undefined, {
      dateStyle: 'medium',
      timeStyle: 'short',
    }).format(new Date(value));
  }

  formatEventType(value: string): string {
    return value.replace('budget.', '').replace('_', ' ');
  }

  private createChart(): void {
    const canvas = document.getElementById('budgetChart') as HTMLCanvasElement | null;
    const events = this.events();
    if (!canvas || events.length === 0) return;

    this.chart?.destroy();
    const config: ChartConfiguration<'line'> = {
      type: 'line',
      data: {
        labels: events.map(event => this.formatDate(event.date)),
        datasets: [{
          label: 'Budget',
          data: events.map(event => event.budget),
          borderColor: '#6366f1',
          backgroundColor: 'rgba(99, 102, 241, 0.14)',
          pointBackgroundColor: '#6366f1',
          pointRadius: 5,
          fill: true,
          tension: 0.35,
        }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
            ticks: { callback: value => `${value} €` },
          },
        },
        plugins: { legend: { display: false } },
      },
    };
    this.chart = new Chart(canvas, config);
  }
}
