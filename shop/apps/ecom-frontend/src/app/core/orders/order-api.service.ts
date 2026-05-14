import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../api';

export type CreateOrderLineDto = {
  productId: string;
  quantity: number;
};

export type CreateOrderDto = {
  customerName: string;
  email: string;
  address: string;
  city: string;
  country: string;
  items: CreateOrderLineDto[];
};

export type OrderItemDto = {
  productId: string;
  productName: string;
  unitPrice: number;
  quantity: number;
  lineTotal: number;
};

export type OrderDto = {
  id: string;
  createdAt: string;
  status: string;
  total: number;
  items: OrderItemDto[];
  customerName: string;
  email: string;
  address: string;
  city: string;
  country: string;
};

@Injectable({ providedIn: 'root' })
export class OrderApiService {
  private readonly http = inject(HttpClient);

  placeOrder(payload: CreateOrderDto) {
    return this.http.post<OrderDto>(`${API_BASE_URL}/api/orders`, payload);
  }

  listMyOrders() {
    return this.http.get<OrderDto[]>(`${API_BASE_URL}/api/orders/my`);
  }
}

