import { CurrencyPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { CartService } from '../core/cart/cart.service';
import { AuthService } from '../core/auth/auth.service';
import { OrderApiService, OrderDto } from '../core/orders/order-api.service';

@Component({
  selector: 'ecom-checkout',
  imports: [ReactiveFormsModule, RouterLink, CurrencyPipe],
  templateUrl: './checkout.html',
  styleUrl: './checkout.scss',
})
export class Checkout {
  private readonly fb = inject(FormBuilder);
  private readonly cart = inject(CartService);
  private readonly auth = inject(AuthService);
  private readonly ordersApi = inject(OrderApiService);
  private readonly currentUser = this.auth.getUser();

  protected readonly cartService = this.cart;
  protected placedOrder: OrderDto | null = null;
  protected loading = false;
  protected error: string | null = null;

  protected readonly form = this.fb.nonNullable.group({
    customerName: [
      this.currentUser ? `${this.currentUser.firstName} ${this.currentUser.lastName}` : '',
      [Validators.required],
    ],
    email: [this.currentUser?.email ?? '', [Validators.required, Validators.email]],
    address: ['', [Validators.required]],
    city: ['', [Validators.required]],
    country: ['United States', [Validators.required]],
  });

  submit() {
    this.error = null;
    if (this.form.invalid || this.cart.items().length === 0) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    const payload = this.form.getRawValue();
    const items = this.cart.items().map((item) => ({
      productId: item.productId,
      quantity: item.quantity,
    }));

    this.ordersApi
      .placeOrder({ ...payload, items })
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (order) => {
          this.placedOrder = order;
          this.cart.clear();
        },
        error: (err) => {
          this.error = err?.error?.error ?? 'Failed to place order';
        },
      });
  }
}
