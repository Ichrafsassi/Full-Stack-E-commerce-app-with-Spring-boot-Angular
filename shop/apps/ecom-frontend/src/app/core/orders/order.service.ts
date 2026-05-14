import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CartItem } from '../cart/cart.service';

export type CheckoutPayload = {
  customerName: string;
  email: string;
  address: string;
  city: string;
  country: string;
};

export type OrderRecord = CheckoutPayload & {
  id: string;
  createdAt: string;
  total: number;
  items: CartItem[];
};

const STORAGE_KEY = 'ecom_orders';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private readonly platformId = inject(PLATFORM_ID);
  private readonly isBrowser = isPlatformBrowser(this.platformId);

  placeOrder(payload: CheckoutPayload, items: CartItem[], total: number): OrderRecord {
    const nextOrder: OrderRecord = {
      ...payload,
      id: `ORD-${Date.now()}`,
      createdAt: new Date().toISOString(),
      total,
      items,
    };

    const orders = this.listOrders();
    const updatedOrders = [nextOrder, ...orders];
    this.persist(updatedOrders);
    return nextOrder;
  }

  listOrders(): OrderRecord[] {
    if (!this.isBrowser) {
      return [];
    }

    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      return raw ? (JSON.parse(raw) as OrderRecord[]) : [];
    } catch {
      return [];
    }
  }

  listOrdersForEmail(email: string | null | undefined): OrderRecord[] {
    if (!email) {
      return [];
    }

    return this.listOrders().filter((order) => order.email.toLowerCase() === email.toLowerCase());
  }

  private persist(orders: OrderRecord[]) {
    if (!this.isBrowser) {
      return;
    }

    localStorage.setItem(STORAGE_KEY, JSON.stringify(orders));
  }
}
