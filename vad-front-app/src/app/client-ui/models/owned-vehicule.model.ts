import { Transmission } from '../enums/transmission';

export interface OwnedVehicule {
  nameVehicule: string;
  brand: string;
  transmission: Transmission;
  supplierName: string;
}
