import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Subject, catchError, filter, interval, startWith, switchMap, take, takeUntil, throwError, throwIfEmpty } from 'rxjs';
import Keycloak from 'keycloak-js';
import { RecommendationResponse, RecommendationService } from '../../services/client-services/recommendation.service';

@Component({
  selector: 'app-recommendations',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './recommendations.html',
  styleUrl: './recommendations.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Recommendations implements OnInit, OnDestroy {
  private readonly keycloak = inject(Keycloak);
  private readonly service = inject(RecommendationService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly destroy$ = new Subject<void>();

  readonly result = signal<RecommendationResponse | null>(null);
  readonly loading = signal(true);
  readonly error = signal(false);

  ngOnInit(): void {
    const email = this.keycloak.tokenParsed?.['email'] as string | undefined;
    if (!email?.trim()) {
      this.loading.set(false);
      this.error.set(true);
      return;
    }
    this.service.request(email.trim()).pipe(
      switchMap(() => interval(500).pipe(
        startWith(0),
        switchMap(() => this.service.latest(email.trim())),
        filter(value => value !== null),
        take(30),
        throwIfEmpty(() => new Error('Recommendation response timed out')),
        catchError(error => throwError(() => error)),
      )),
      take(1),
      takeUntil(this.destroy$),
    ).subscribe({
      next: value => {
        this.result.set(value);
        this.loading.set(false);
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Unable to load recommendations:', error);
        this.loading.set(false);
        this.error.set(true);
        this.cdr.markForCheck();
      },
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  refresh(): void {
    window.location.reload();
  }
}
