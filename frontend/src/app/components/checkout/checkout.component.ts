import { Component, OnInit, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../services/order.service';
import { CartService } from '../../services/cart.service';
import { Cart } from '../../models';
import { CurrencyPipe } from '@angular/common';
import { adminErrorMessage } from '../../utils/admin-http';

@Component({
  selector: 'app-checkout',
  imports: [FormsModule, RouterLink, CurrencyPipe],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.scss'
})
export class CheckoutComponent implements OnInit {
  private readonly orders = inject(OrderService);
  private readonly cartService = inject(CartService);
  private readonly router = inject(Router);

  cart = signal<Cart | null>(null);
  address = '';
  useStripe = false;
  loading = signal(false);
  error = signal('');

  ngOnInit(): void {
    this.cartService.get().subscribe({
      next: c => {
        this.cart.set(c);
        if (!c.items?.length) {
          this.error.set('Your cart is empty.');
        }
      },
      error: e => this.error.set(adminErrorMessage(e, 'Failed to load cart.'))
    });
  }

  submit(): void {
    if (!this.address.trim()) {
      this.error.set('Please provide a shipping address.');
      return;
    }
    if (!this.cart()?.items?.length) {
      this.error.set('Your cart is empty.');
      return;
    }

    this.loading.set(true);
    this.error.set('');

    this.orders.checkout(this.address, this.useStripe).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/orders']);
      },
      error: e => {
        this.error.set(adminErrorMessage(e, 'Checkout failed.'));
        this.loading.set(false);
      }
    });
  }
}
