import { Component, ElementRef, ViewChild } from '@angular/core';

type VehiculeStatus = 'AVAILABLE' | 'TAKEN' | 'REPARATION';

interface Vehicule {

  nameVehicule: string;

  brand: string;

  color: string;

  price: number;

  highSpeed: number;

  transmission: string;

  vehiculeStatus: VehiculeStatus;

}

@Component({

  selector: 'app-supplier-vehicules',

  templateUrl: './supplier-vehicules.html',

  styleUrl: './supplier-vehicules.css',

})
export class SupplierVehicules {

  @ViewChild('carousel') carousel!: ElementRef;



  vehicules: Vehicule[] = [

    {

      nameVehicule: 'BMW X6',

      brand: 'BMW',

      color: 'Black',

      price: 120,

      highSpeed: 250,

      transmission: 'AUTOMATIC',

      vehiculeStatus: 'AVAILABLE'

    },

    {

      nameVehicule: 'Audi Q7',

      brand: 'Audi',

      color: 'White',

      price: 110,

      highSpeed: 240,

      transmission: 'AUTOMATIC',

      vehiculeStatus: 'TAKEN'

    },

    {

      nameVehicule: 'Toyota Corolla',

      brand: 'Toyota',

      color: 'Grey',

      price: 60,

      highSpeed: 190,

      transmission: 'MANUAL',

      vehiculeStatus: 'REPARATION'

    },

    {

      nameVehicule: 'Mercedes GLC',

      brand: 'Mercedes',

      color: 'Silver',

      price: 140,

      highSpeed: 255,

      transmission: 'AUTOMATIC',

      vehiculeStatus: 'AVAILABLE'

    },

    {

      nameVehicule: 'Range Rover Evoque',

      brand: 'Range Rover',

      color: 'Green',

      price: 160,

      highSpeed: 260,

      transmission: 'AUTOMATIC',

      vehiculeStatus: 'AVAILABLE'

    }

  ];



  /** DASHBOARD COUNTERS */

  get totalVehicules() {

    return this.vehicules.length;

  }

  get availableVehicules() {

    return this.vehicules.filter(v =>

      v.vehiculeStatus === 'AVAILABLE'

    ).length;

  }

  get takenVehicules() {

    return this.vehicules.filter(v =>

      v.vehiculeStatus === 'TAKEN'

    ).length;

  }

  get repairVehicules() {

    return this.vehicules.filter(v =>

      v.vehiculeStatus === 'REPARATION'

    ).length;

  }



  /** CAROUSEL CONTROLS */

  scrollLeft() {

    this.carousel.nativeElement.scrollBy({

      left: -320,

      behavior: 'smooth'

    });

  }



  scrollRight() {

    this.carousel.nativeElement.scrollBy({

      left: 320,

      behavior: 'smooth'

    });

  }

}
