import { Transmission } from './transmission.enum';

export interface CreateVehicule {
  nameVehicule: string;
  color: string;
  brand: string;
  price: number;
  highSpeed: number;
  transmission: Transmission;
}
