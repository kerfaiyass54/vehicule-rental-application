import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CategoryDTO {
  idCategory: number;
  nameCategory: string;
  typeCategory: string;
  stock: number;
  supplierEmail: string;
}

@Injectable({
  providedIn: 'root',
})
export class CategoriesService {

  private baseUrl = 'http://localhost:8100/supplier/categories';

  constructor(private http: HttpClient) {}



  /**
   * Get total number of categories for supplier
   */
  getTotalCategories(email: string): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/total/${email}`
    );
  }



  /**
   * Get total stock across all categories
   */
  getTotalStock(email: string): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/stock/${email}`
    );
  }



  /**
   * Get all categories for supplier
   */
  getCategoryList(email: string): Observable<CategoryDTO[]> {
    return this.http.get<CategoryDTO[]>(
      `${this.baseUrl}/list/${email}`
    );
  }



  /**
   * Get number of vehicles inside a category
   */
  getStockContent(
    email: string,
    nameCategory: string
  ): Observable<number> {

    return this.http.get<number>(
      `${this.baseUrl}/stock-content/${email}?nameCategory=${nameCategory}`
    );
  }



  /**
   * Add new category
   */
  addCategory(
    category: CategoryDTO,
    email: string
  ): Observable<CategoryDTO> {

    return this.http.post<CategoryDTO>(
      `${this.baseUrl}/add/${email}`,
      category
    );
  }

}
