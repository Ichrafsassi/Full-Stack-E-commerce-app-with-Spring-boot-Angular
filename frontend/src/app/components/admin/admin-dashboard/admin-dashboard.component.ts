import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CatalogService } from '../../../services/catalog.service';
import { AdminService } from '../../../services/admin.service';
import { OrderService } from '../../../services/order.service';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-admin-dashboard',
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss'
})
export class AdminDashboardComponent implements OnInit {
  private readonly catalog = inject(CatalogService);
  private readonly admin = inject(AdminService);
  private readonly orders = inject(OrderService);
  productCount = signal(0);
  categoryCount = signal(0);
  orderCount = signal(0);
  userCount = signal(0);
  recentRevenue = signal(0);

  ngOnInit(): void {
    this.admin.listProducts().subscribe(p => this.productCount.set(p.filter(x => x.active !== false).length));
    this.catalog.getCategories().subscribe(c => this.categoryCount.set(c.length));
    this.orders.adminList().subscribe(o => {
      this.orderCount.set(o.length);
      this.recentRevenue.set(o.reduce((s, x) => s + x.totalAmount, 0));
    });
    this.admin.users().subscribe(u => this.userCount.set(u.length));
  }
}
