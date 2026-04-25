export interface AddressCreation {
  road: string;
  number: number;
  location: string;
}

export type Transmission = 'MANUAL' | 'AUTOMATIC';

export interface VehiculeCreation {
  nameVehicule: string;
  color: string;
  brand: string;
  price: number;
  highSpeed: number;
  transmission: Transmission;
  category: string;
}


export interface CategoryCreation {

  nameCategory: string;

  typeCategory: string;

  stock: number;

}
