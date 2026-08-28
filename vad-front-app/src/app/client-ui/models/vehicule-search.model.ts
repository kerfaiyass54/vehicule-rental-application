import { Transmission } from '../enums/transmission';
import { VehiculeStatus } from '../enums/vehicule-status';

export interface VehiculeSearchDTO {

  idVehicule: number;

  nameVehicule: string;

  brand: string;

  color: string;

  price: number;

  highSpeed: number;

  transmission: Transmission;

  vehiculeStatus: VehiculeStatus;

}
