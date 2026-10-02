import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-location-eda',
  standalone: true,
  imports: [MatIconModule],
  templateUrl: './admin-location-eda.html',
  styleUrl: './admin-location-eda.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdminLocationEda {
  private readonly router = inject(Router);

  goToLocationAnalysis(): void {
    void this.router.navigate(['/admin/location-eda/analyze']);
  }

  goToAnalysisList(): void {
    void this.router.navigate(['/admin/location-eda/analyses']);
  }
}
