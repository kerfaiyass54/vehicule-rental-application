import { Transmission } from '../enums/transmission';

export interface VehiculeSupplier {
  idVehicule: number;
  nameVehicule: string;
  color: string;
  brand: string;
  price: number;
  highSpeed: number;
  transmission: Transmission;
}
