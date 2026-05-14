import { PLATFORM_ID, computed, inject, Injectable, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

export type AddToCartProduct = {
  id: string;
  name: string;
  price: number;
  imageUrl?: string | null;
  image?: string;
};

export type CartItem = {
  productId: string;
  name: string;
  price: number;
  image: string;
  quantity: number;
};

const STORAGE_KEY = 'ecom_cart';

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly platformId = inject(PLATFORM_ID);
  private readonly isBrowser = isPlatformBrowser(this.platformId);
  private readonly itemsState = signal<CartItem[]>(this.readInitialState());

  readonly items = this.itemsState.asReadonly();
  readonly itemCount = computed(() =>
    this.itemsState().reduce((sum, item) => sum + item.quantity, 0)
  );
  readonly subtotal = computed(() =>
    this.itemsState().reduce((sum, item) => sum + item.price * item.quantity, 0)
  );
  readonly shipping = computed(() => (this.subtotal() >= 150 || this.subtotal() === 0 ? 0 : 12));
  readonly total = computed(() => this.subtotal() + this.shipping());

  addProduct(product: AddToCartProduct, quantity = 1) {
    this.itemsState.update((items) => {
      const existing = items.find((item) => item.productId === product.id);
      if (existing) {
        return items.map((item) =>
          item.productId === product.id ? { ...item, quantity: item.quantity + quantity } : item
        );
      }

      return [
        ...items,
        {
          productId: product.id,
          name: product.name,
          price: product.price,
          image: product.imageUrl ?? product.image ?? '',
          quantity,
        },
      ];
    });
    this.persist();
  }

  updateQuantity(productId: string, quantity: number) {
    if (quantity <= 0) {
      this.removeItem(productId);
      return;
    }

    this.itemsState.update((items) =>
      items.map((item) => (item.productId === productId ? { ...item, quantity } : item))
    );
    this.persist();
  }

  removeItem(productId: string) {
    this.itemsState.update((items) => items.filter((item) => item.productId !== productId));
    this.persist();
  }

  clear() {
    this.itemsState.set([]);
    this.persist();
  }

  private readInitialState(): CartItem[] {
    if (!this.isBrowser) {
      return [];
    }

    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      return raw ? (JSON.parse(raw) as CartItem[]) : [];
    } catch {
      return [];
    }
  }

  private persist() {
    if (!this.isBrowser) {
      return;
    }

    localStorage.setItem(STORAGE_KEY, JSON.stringify(this.itemsState()));
  }
}
