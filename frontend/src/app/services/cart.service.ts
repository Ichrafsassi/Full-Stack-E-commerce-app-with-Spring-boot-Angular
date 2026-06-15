import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Cart } from '../models';

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly http = inject(HttpClient);

  get() {
    return this.http.get<Cart>(`${environment.apiUrl}/cart`);
  }

  add(productId: number, quantity = 1) {
    return this.http.post<Cart>(`${environment.apiUrl}/cart/items`, { productId, quantity });
  }

  update(productId: number, quantity: number) {
    return this.http.put<Cart>(`${environment.apiUrl}/cart/items/${productId}`, null, {
      params: { quantity }
    });
  }

  remove(productId: number) {
    return this.http.delete<Cart>(`${environment.apiUrl}/cart/items/${productId}`);
  }
}
