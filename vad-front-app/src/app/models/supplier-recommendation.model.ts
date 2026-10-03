import { VehicleRecommendation } from './vehicle-recommendation.model';

export interface SupplierRecommendation {
  supplierId: number;
  supplierName: string;
  road: string;
  addressNumber: number;
  vehicles: VehicleRecommendation[];
}
