import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Category, Product, User } from '../models';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly http = inject(HttpClient);

  users() {
    return this.http.get<User[]>(`${environment.apiUrl}/admin/users`);
  }

  createUser(body: User & { password: string }) {
    return this.http.post<User>(`${environment.apiUrl}/admin/users`, body);
  }

  updateUser(id: number, body: Partial<User & { password?: string }>) {
    return this.http.put<User>(`${environment.apiUrl}/admin/users/${id}`, body);
  }

  deleteUser(id: number) {
    return this.http.delete<void>(`${environment.apiUrl}/admin/users/${id}`);
  }

  listProducts() {
    return this.http.get<Product[]>(`${environment.apiUrl}/admin/products`);
  }

  createProduct(body: Omit<Product, 'id' | 'categoryName'>) {
    return this.http.post<Product>(`${environment.apiUrl}/admin/products`, body);
  }

  updateProduct(id: number, body: Omit<Product, 'id' | 'categoryName'>) {
    return this.http.put<Product>(`${environment.apiUrl}/admin/products/${id}`, body);
  }

  deleteProduct(id: number) {
    return this.http.delete<void>(`${environment.apiUrl}/admin/products/${id}`);
  }

  createCategory(body: Omit<Category, 'id'>) {
    return this.http.post<Category>(`${environment.apiUrl}/admin/categories`, body);
  }

  updateCategory(id: number, body: Omit<Category, 'id'>) {
    return this.http.put<Category>(`${environment.apiUrl}/admin/categories/${id}`, body);
  }

  deleteCategory(id: number) {
    return this.http.delete<void>(`${environment.apiUrl}/admin/categories/${id}`);
  }
}
