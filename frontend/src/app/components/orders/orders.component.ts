import { Component, OnInit, inject, signal } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { Order } from '../../models';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { adminErrorMessage } from '../../utils/admin-http';

@Component({
  selector: 'app-orders',
  imports: [CurrencyPipe, DatePipe],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.scss'
})
export class OrdersComponent implements OnInit {
  private readonly orderService = inject(OrderService);
  orders = signal<Order[]>([]);
  error = signal('');
  loading = signal(true);

  ngOnInit(): void {
    this.orderService.myOrders().subscribe({
      next: o => {
        this.orders.set(o);
        this.loading.set(false);
        this.error.set('');
      },
      error: e => {
        this.loading.set(false);
        this.error.set(adminErrorMessage(e, 'Failed to load orders.'));
      }
    });
  }
}
