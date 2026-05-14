import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../api';

export type CategoryDto = {
  id: string;
  name: string;
  slug: string;
};

export type ProductDto = {
  id: string;
  name: string;
  description: string;
  price: number;
  stock: number;
  active: boolean;
  imageUrl: string | null;
  size: string | null;
  categoryId: string;
  categoryName: string;
  createdAt: string;
};

@Injectable({ providedIn: 'root' })
export class CatalogApiService {
  private readonly http = inject(HttpClient);

  listCategories() {
    return this.http.get<CategoryDto[]>(`${API_BASE_URL}/api/categories`);
  }

  listProducts(params?: { categoryId?: string; sort?: string }) {
    const query = new URLSearchParams();
    if (params?.categoryId) query.set('categoryId', params.categoryId);
    if (params?.sort) query.set('sort', params.sort);
    const suffix = query.toString() ? `?${query.toString()}` : '';
    return this.http.get<ProductDto[]>(`${API_BASE_URL}/api/products${suffix}`);
  }

  getProduct(productId: string) {
    return this.http.get<ProductDto>(`${API_BASE_URL}/api/products/${productId}`);
  }
}

