import { Transmission } from './transmission.enum';
import { VehiculeStatus } from './vehicule-status.enum';

export interface Vehicule {
  idVehicule: number;
  nameVehicule: string;
  color: string;
  brand: string;
  price: number;
  highSpeed: number;
  transmission: Transmission;
  vehiculeStatus: VehiculeStatus;
  supplier: string;
}
