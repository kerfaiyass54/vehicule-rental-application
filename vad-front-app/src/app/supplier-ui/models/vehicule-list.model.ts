import { Transmission } from './transmission.enum';

export interface VehiculeList {
  idVehicule: number;
  nameVehicule: string;
  price: number;
  transmission: Transmission;
}
