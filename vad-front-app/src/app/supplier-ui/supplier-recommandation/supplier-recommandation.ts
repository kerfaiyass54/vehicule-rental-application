import {
  Component,
  ElementRef,
  ViewChild,
  inject,
  signal,
  OnInit
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  FormsModule
} from '@angular/forms';

import Keycloak from 'keycloak-js';

import {
  RecommandService
} from '../../services/recommand-service';

import {
  SupplierDetailsService
} from '../../services/supplier-details-service';

import {
  VehiculesService,
  VehiculeDTO
} from '../../services/vehicules-service';

declare var bootstrap: any;

@Component({
  selector: 'app-supplier-recommandation',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl:
    './supplier-recommandation.html',
  styleUrl:
    './supplier-recommandation.css',
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

  private vehiculeService =
    inject(VehiculesService);

  email = '';

  recommendations =
    signal<any[]>([]);

  totalRecommendations =
    signal(0);

  loading =
    signal(true);

  selectedCar: any = null;

  addingVehicule =
    signal(false);

  vehiculeForm: any = {

    nameVehicule: '',

    color: '',

    brand: '',

    price: 0,

    highSpeed: 0,

    transmission: 'AUTOMATIC',

    vehiculeStatus: 'AVAILABLE',

    supplier: ''

  };

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

  openAddModal(car: any) {

    this.selectedCar = car;

    this.vehiculeForm = {

      nameVehicule:
      car.carName,

      color: '',

      brand:
      car.brand,

      price:
      car.price,

      highSpeed:
      car.topSpeed,

      transmission:
        'AUTOMATIC',

      vehiculeStatus:
        'AVAILABLE',

      supplier:
      this.email

    };

    const modal =
      new bootstrap.Modal(
        document.getElementById(
          'addVehiculeModal'
        )
      );

    modal.show();

  }

  addVehicule() {

    this.addingVehicule.set(true);

    this.vehiculeService
      .addVehicule(
        this.vehiculeForm
      )
      .subscribe({

        next: () => {

          this.addingVehicule.set(false);

          const modal =
            bootstrap.Modal.getInstance(
              document.getElementById(
                'addVehiculeModal'
              )
            );

          modal.hide();

        },

        error: () => {

          this.addingVehicule.set(false);

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
