import {Component, inject, Input, OnInit, PLATFORM_ID, ChangeDetectionStrategy} from '@angular/core';
import {isPlatformBrowser} from "@angular/common";


@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-bar-charts',
  imports: [
  ],
  templateUrl: './bar-charts.html',
  styleUrl: './bar-charts.css',
})
export class BarCharts implements OnInit {
  basicData: any;
  basicOptions: any;
  platformId = inject(PLATFORM_ID);
  @Input() labels: any[] = [];
  @Input() label: any = '';
  @Input() data: any[] = [];
  @Input() colors: any[] = [];

  ngOnInit() {
    this.initChart();
  }

  initChart() {
    if (isPlatformBrowser(this.platformId)) {
      const documentStyle = getComputedStyle(document.documentElement);
      const textColor = documentStyle.getPropertyValue('--p-text-color');
      const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
      const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

      this.basicData = {
        labels: this.labels,
        datasets: [
          {
            label: this.label,
            data: this.data,
            backgroundColor: this.colors,
            borderColor: this.colors,
            borderWidth: 1
          }
        ]
      };

      this.basicOptions = {
        maintainAspectRatio: false,
        aspectRatio: 0.8,
        plugins: {
          legend: {
            labels: {
              color: textColor
            }
          }
        },
        scales: {
          x: {
            ticks: {
              color: textColorSecondary
            },
            grid: {
              color: surfaceBorder
            }
          },
          y: {
            beginAtZero: true,
            ticks: {
              color: textColorSecondary
            },
            grid: {
              color: surfaceBorder
            }
          }
        }
      };
    }
  }
}
