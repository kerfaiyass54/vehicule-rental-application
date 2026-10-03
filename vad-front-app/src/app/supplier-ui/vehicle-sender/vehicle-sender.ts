import { ChangeDetectionStrategy, ChangeDetectorRef, Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { VehicleSenderResponse, VehicleSenderService } from '../../services/supplier-services/vehicle-sender.service';

@Component({
  selector: 'app-vehicle-sender',
  standalone: true,
  imports: [CommonModule, FormsModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './vehicle-sender.html',
  styleUrl: './vehicle-sender.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class VehicleSender {
  private readonly service = inject(VehicleSenderService);
  private readonly cdr = inject(ChangeDetectorRef);

  readonly prompt = signal('');
  readonly result = signal<VehicleSenderResponse | null>(null);
  readonly loading = signal(false);
  readonly error = signal('');

  submit(): void {
    const text = this.prompt().trim();
    if (!text || this.loading()) return;
    this.loading.set(true);
    this.error.set('');
    this.service.generate(text).subscribe({
      next: response => {
        this.result.set(response);
        this.loading.set(false);
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Unable to interpret vehicle request:', error);
        this.error.set('The vehicle request could not be processed. Please try again.');
        this.loading.set(false);
        this.cdr.markForCheck();
      },
    });
  }

  downloadCsv(): void {
    const vehicles = this.result()?.vehicles ?? [];
    if (!vehicles.length) return;
    const columns = ['vehicleName', 'brand', 'color', 'price', 'maxSpeed', 'transmission', 'status', 'fuelType', 'horsepower', 'matchScore', 'reason'];
    const escape = (value: unknown) => `"${String(value ?? '').replaceAll('"', '""')}"`;
    const csv = [columns.join(','), ...vehicles.map(vehicle => columns.map(column => escape(vehicle[column as keyof typeof vehicle])).join(','))].join('\r\n');
    const link = document.createElement('a');
    link.href = URL.createObjectURL(new Blob([csv], { type: 'text/csv;charset=utf-8' }));
    link.download = 'vehicle-sender-results.csv';
    link.click();
    URL.revokeObjectURL(link.href);
  }
}
