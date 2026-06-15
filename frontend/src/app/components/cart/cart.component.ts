import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { Cart } from '../../models';
import { CurrencyPipe } from '@angular/common';
import { adminErrorMessage } from '../../utils/admin-http';

@Component({
  selector: 'app-cart',
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent implements OnInit {
  private readonly cartService = inject(CartService);
  cart = signal<Cart | null>(null);
  error = signal('');

  ngOnInit(): void { this.refresh(); }

  refresh(): void {
    this.cartService.get().subscribe({
      next: c => { this.cart.set(c); this.error.set(''); },
      error: e => this.error.set(adminErrorMessage(e, 'Failed to load cart.'))
    });
  }

  qty(productId: number, quantity: number): void {
    if (quantity < 1) {
      this.remove(productId);
      return;
    }
    this.cartService.update(productId, quantity).subscribe({
      next: c => { this.cart.set(c); this.error.set(''); },
      error: e => this.error.set(adminErrorMessage(e, 'Failed to update quantity.'))
    });
  }

  remove(productId: number): void {
    this.cartService.remove(productId).subscribe({
      next: c => { this.cart.set(c); this.error.set(''); },
      error: e => this.error.set(adminErrorMessage(e, 'Failed to remove item.'))
    });
  }
}
