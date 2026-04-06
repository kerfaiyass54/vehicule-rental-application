import { Component } from '@angular/core';

export type CategoryName =
  | 'PASSENGER_VEHICLES'
  | 'COMMERCIAL_VEHICLES'
  | 'MOTORCYCLES'
  | 'ELECTRIC_VEHICLES'
  | 'OFF_ROAD_VEHICLES'
  | 'HEAVY_DUTY_VEHICLES'
  | 'EMERGENCY_VEHICLES'
  | 'AGRICULTURAL_VEHICLES'
  | 'MARINE_VEHICLES'
  | 'AERIAL_VEHICLES';

interface Category {

  nameCategory: CategoryName;
  typeCategory: string;
  stock: number;

}

@Component({
  selector: 'app-supplier-categories',
  templateUrl: './supplier-categories.html',
  styleUrl: './supplier-categories.css'
})
export class SupplierCategories {

  categories: Category[] = [

    {
      nameCategory: 'PASSENGER_VEHICLES',
      typeCategory: 'Daily transport & comfort',
      stock: 18
    },

    {
      nameCategory: 'COMMERCIAL_VEHICLES',
      typeCategory: 'Business logistics',
      stock: 10
    },

    {
      nameCategory: 'MOTORCYCLES',
      typeCategory: 'Fast & flexible mobility',
      stock: 7
    },

    {
      nameCategory: 'ELECTRIC_VEHICLES',
      typeCategory: 'Eco-friendly transport',
      stock: 5
    },

    {
      nameCategory: 'OFF_ROAD_VEHICLES',
      typeCategory: 'Adventure & terrain',
      stock: 6
    },

    {
      nameCategory: 'HEAVY_DUTY_VEHICLES',
      typeCategory: 'Industrial usage',
      stock: 4
    },

    {
      nameCategory: 'EMERGENCY_VEHICLES',
      typeCategory: 'Critical response units',
      stock: 3
    },

    {
      nameCategory: 'AGRICULTURAL_VEHICLES',
      typeCategory: 'Farming operations',
      stock: 5
    },

    {
      nameCategory: 'MARINE_VEHICLES',
      typeCategory: 'Water transport',
      stock: 2
    },

    {
      nameCategory: 'AERIAL_VEHICLES',
      typeCategory: 'Air mobility',
      stock: 1
    }

  ];



  get totalStock(): number {

    return this.categories.reduce(

      (sum, c) => sum + c.stock,

      0

    );

  }



  getCategoryIcon(name: CategoryName): string {

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



  formatName(name: string): string {

    return name.replaceAll('_', ' ');

  }

}
