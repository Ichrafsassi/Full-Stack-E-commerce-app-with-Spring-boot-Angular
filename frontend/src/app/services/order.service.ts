import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Order, OrderStatus } from '../models';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private readonly http = inject(HttpClient);

  myOrders() {
    return this.http.get<Order[]>(`${environment.apiUrl}/orders`);
  }

  checkout(shippingAddress: string, useStripe = false) {
    return this.http.post<Order>(`${environment.apiUrl}/orders/checkout`, { shippingAddress, useStripe });
  }

  get(id: number) {
    return this.http.get<Order>(`${environment.apiUrl}/orders/${id}`);
  }

  adminList() {
    return this.http.get<Order[]>(`${environment.apiUrl}/admin/orders`);
  }

  adminGet(id: number) {
    return this.http.get<Order>(`${environment.apiUrl}/admin/orders/${id}`);
  }

  adminCreate(order: any) {
    return this.http.post<Order>(`${environment.apiUrl}/admin/orders`, order);
  }

  adminUpdate(id: number, order: any) {
    return this.http.put<Order>(`${environment.apiUrl}/admin/orders/${id}`, order);
  }

  updateStatus(id: number, status: OrderStatus) {
    return this.http.patch<Order>(`${environment.apiUrl}/admin/orders/${id}/status`, { status });
  }

  deleteOrder(id: number) {
    return this.http.delete<void>(`${environment.apiUrl}/admin/orders/${id}`);
  }
}
