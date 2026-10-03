import { SupplierRecommendation } from './supplier-recommendation.model';

export interface RecommendationResponse {
  email: string;
  locationName: string;
  budget: number;
  suppliers: SupplierRecommendation[];
  generatedAt: string;
}
