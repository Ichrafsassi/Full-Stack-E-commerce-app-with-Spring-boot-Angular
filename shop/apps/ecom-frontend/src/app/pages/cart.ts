import { CurrencyPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CartService } from '../core/cart/cart.service';

@Component({
  selector: 'ecom-cart',
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './cart.html',
  styleUrl: './cart.scss',
})
export class Cart {
  protected readonly cart = inject(CartService);

  decrement(productId: string, quantity: number) {
    this.cart.updateQuantity(productId, quantity - 1);
  }

  increment(productId: string, quantity: number) {
    this.cart.updateQuantity(productId, quantity + 1);
  }
}
