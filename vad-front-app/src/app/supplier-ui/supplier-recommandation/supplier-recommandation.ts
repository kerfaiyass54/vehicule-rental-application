import {
  Component,
  ElementRef,
  ViewChild,
  inject,
  signal,
  OnInit
} from '@angular/core';

import { CommonModule } from '@angular/common';

import Keycloak from 'keycloak-js';

import {
  RecommandService
} from '../../services/recommand-service';

import {
  SupplierDetailsService
} from '../../services/supplier-details-service';

@Component({
  selector: 'app-supplier-recommandation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './supplier-recommandation.html',
  styleUrl: './supplier-recommandation.css',
})
export class SupplierRecommandation
  implements OnInit {

  @ViewChild('carousel')
  carousel!: ElementRef;

  protected keycloak =
    inject(Keycloak);

  private recommandService =
    inject(RecommandService);

  private supplierService =
    inject(SupplierDetailsService);

  email = '';

  recommendations =
    signal<any[]>([]);

  totalRecommendations =
    signal(0);

  loading =
    signal(true);

  ngOnInit(): void {

    const token =
      this.keycloak.tokenParsed;

    if (token) {

      this.email =
        token['email'] ?? '';

      this.loadRecommendations();

    }

  }

  loadRecommendations() {

    this.loading.set(true);

    this.supplierService
      .getVehiculesIds(this.email)
      .subscribe({

        next: (vehicleIds) => {

          if (!vehicleIds.length) {

            this.recommendations.set([]);

            this.totalRecommendations.set(0);

            this.loading.set(false);

            return;

          }

          const allCars: any[] = [];

          let completed = 0;

          vehicleIds.forEach(id => {

            this.recommandService
              .getRecommendations(id)
              .subscribe({

                next: (data) => {

                  if (
                    data &&
                    data.recommendations
                  ) {

                    data.recommendations.forEach(
                      (car: any) => {

                        allCars.push({

                          vehicleId:
                          data.vehicleId,

                          carName:
                          car.carName,

                          brand:
                          car.brand,

                          horsepower:
                          car.horsepower,

                          topSpeed:
                          car.topSpeed,

                          price:
                          car.price,

                          torque:
                          car.torque,

                          acceleration:
                          car.acceleration0100,

                          fuelType:
                          car.fuelType

                        });

                      });

                  }

                  completed++;

                  if (
                    completed === vehicleIds.length
                  ) {

                    this.recommendations.set(
                      allCars
                    );

                    this.totalRecommendations.set(
                      allCars.length
                    );

                    this.loading.set(false);

                  }

                },

                error: () => {

                  completed++;

                  if (
                    completed === vehicleIds.length
                  ) {

                    this.recommendations.set(
                      allCars
                    );

                    this.totalRecommendations.set(
                      allCars.length
                    );

                    this.loading.set(false);

                  }

                }

              });

          });

        },

        error: () => {

          this.loading.set(false);

        }

      });

  }

  scrollLeft() {

    this.carousel
      .nativeElement
      .scrollBy({

        left: -450,

        behavior: 'smooth'

      });

  }

  scrollRight() {

    this.carousel
      .nativeElement
      .scrollBy({

        left: 450,

        behavior: 'smooth'

      });

  }

}
