import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService, UserDto } from '../core/auth/auth.service';
import { OrderApiService, OrderDto } from '../core/orders/order-api.service';

@Component({
  selector: 'ecom-account',
  imports: [CurrencyPipe, DatePipe, RouterLink],
  templateUrl: './account.html',
  styleUrl: './account.scss',
})
export class Account implements OnInit {
  private readonly auth = inject(AuthService);
  private readonly orderApi = inject(OrderApiService);

  protected loading = false;
  protected error: string | null = null;
  protected user: UserDto | null = this.auth.getUser();
  protected orders: OrderDto[] = [];

  ngOnInit(): void {
    this.loading = true;
    this.auth
      .fetchCurrentUser()
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (user) => {
          this.user = user;
          this.orderApi.listMyOrders().subscribe({
            next: (orders) => (this.orders = orders),
            error: () => (this.orders = []),
          });
        },
        error: (err) => {
          this.error = err?.error?.error ?? 'Failed to load account details';
        },
      });
  }
}
