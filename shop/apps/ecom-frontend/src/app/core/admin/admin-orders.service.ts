import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../api';

export type AdminOrderItemDto = {
  productId: string;
  productName: string;
  quantity: number;
  lineTotal: number;
};

export type AdminOrderDto = {
  id: string;
  createdAt: string;
  status: string;
  total: number;
  customerName: string;
  email: string;
  items: AdminOrderItemDto[];
};

@Injectable({ providedIn: 'root' })
export class AdminOrdersService {
  private readonly http = inject(HttpClient);

  listOrders() {
    return this.http.get<AdminOrderDto[]>(`${API_BASE_URL}/api/admin/orders`);
  }
}

