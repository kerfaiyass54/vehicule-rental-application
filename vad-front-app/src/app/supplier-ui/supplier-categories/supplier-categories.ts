import { Component, OnInit, inject } from '@angular/core';
import Keycloak from 'keycloak-js';
import { CategoriesService, CategoryDTO } from '../../services/categories-service';

@Component({
  selector: 'app-supplier-categories',
  templateUrl: './supplier-categories.html',
  styleUrl: './supplier-categories.css'
})
export class SupplierCategories implements OnInit {

  categories: CategoryDTO[] = [];

  totalStock = 0;

  email: string = '';

  loading = false;
  error = '';

  /**
   * Stores progress percentage per category
   * Example:
   * {
   *   "Sedan": 100,
   *   "SUV": 75
   * }
   */
  categoryPercentages: { [key: string]: number } = {};

  protected keycloak = inject(Keycloak);

  constructor(private categoriesService: CategoriesService) {}



  ngOnInit(): void {

    const token = this.keycloak.tokenParsed;

    if (token) {

      this.email = token['email'] ?? '';

      this.loadTotalStock();
      this.loadCategories();

    }

  }



  /**
   * Load supplier categories
   */
  loadCategories(): void {

    if (!this.email) return;

    this.loading = true;

    this.categoriesService
      .getCategoryList(this.email)
      .subscribe({

        next: (data) => {

          this.categories = data;

          this.loading = false;

          this.calculateCategoryPercentages();

        },

        error: () => {

          this.error = 'Failed to load categories';
          this.loading = false;

        }

      });

  }



  /**
   * Load total fleet stock
   */
  loadTotalStock(): void {

    if (!this.email) return;

    this.categoriesService
      .getTotalStock(this.email)
      .subscribe({

        next: (data) => {

          this.totalStock = data;

        },

        error: () => {

          console.error('Stock loading error');

        }

      });

  }



  /**
   * Calculate progress bar percentage
   * Formula:
   * vehicles inside category / category stock * 100
   */
  calculateCategoryPercentages(): void {

    this.categories.forEach(category => {

      this.categoriesService
        .getStockContent(this.email, category.typeCategory)
        .subscribe({

          next: (vehicleCount) => {

            if (category.stock > 0) {

              this.categoryPercentages[category.typeCategory] =
                (vehicleCount / category.stock) * 100;

            }

          }

        });

    });

  }



  /**
   * Category icon mapping
   */
  getCategoryIcon(name: string): string {

    switch (name) {

      case 'PASSENGER_VEHICLES': return '🚗';
      case 'COMMERCIAL_VEHICLES': return '🚚';
      case 'MOTORCYCLES': return '🏍️';
      case 'ELECTRIC_VEHICLES': return '⚡';
      case 'OFF_ROAD_VEHICLES': return '🚙';
      case 'HEAVY_DUTY_VEHICLES': return '🏗️';
      case 'EMERGENCY_VEHICLES': return '🚑';
      case 'AGRICULTURAL_VEHICLES': return '🚜';
      case 'MARINE_VEHICLES': return '🚤';
      case 'AERIAL_VEHICLES': return '✈️';

      default: return '🚘';

    }

  }



  /**
   * Format enum label nicely for UI
   * Example:
   * PASSENGER_VEHICLES → PASSENGER VEHICLES
   */
  formatName(name: string): string {

    return name.replaceAll('_', ' ');

  }

}
