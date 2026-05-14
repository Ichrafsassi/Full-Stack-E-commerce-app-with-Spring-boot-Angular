import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../api';

export type AdminCategoryDto = {
  id: string;
  name: string;
  slug: string;
};

export type AdminProductDto = {
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
export class AdminCatalogService {
  private readonly http = inject(HttpClient);

  listCategories() {
    return this.http.get<AdminCategoryDto[]>(`${API_BASE_URL}/api/admin/catalog/categories`);
  }

  createCategory(name: string) {
    return this.http.post<AdminCategoryDto>(`${API_BASE_URL}/api/admin/catalog/categories`, { name });
  }

  deleteCategory(categoryId: string) {
    return this.http.delete<void>(`${API_BASE_URL}/api/admin/catalog/categories/${categoryId}`);
  }

  createProduct(payload: {
    name: string;
    description: string;
    price: number;
    stock: number;
    categoryId: string;
    imageUrl?: string | null;
    size?: string | null;
  }) {
    return this.http.post<AdminProductDto>(`${API_BASE_URL}/api/admin/catalog/products`, payload);
  }

  deleteProduct(productId: string) {
    return this.http.delete<void>(`${API_BASE_URL}/api/admin/catalog/products/${productId}`);
  }
}

