import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Category, DealsResponse, Product, Recommendation } from '../models';

@Injectable({ providedIn: 'root' })
export class CatalogService {
  private readonly http = inject(HttpClient);

  getProducts(categoryId?: number, search?: string) {
    let params = new HttpParams();
    if (categoryId) params = params.set('categoryId', categoryId);
    if (search) params = params.set('search', search);
    return this.http.get<Product[]>(`${environment.apiUrl}/products`, { params });
  }

  getProduct(id: number) {
    return this.http.get<Product>(`${environment.apiUrl}/products/${id}`);
  }

  getCategories() {
    return this.http.get<Category[]>(`${environment.apiUrl}/categories`);
  }

  getRecommendations() {
    return this.http.get<Recommendation>(`${environment.apiUrl}/recommendations`);
  }

  getDeals() {
    return this.http.get<DealsResponse>(`${environment.apiUrl}/deals`);
  }
}
