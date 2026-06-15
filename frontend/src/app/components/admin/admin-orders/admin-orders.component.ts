import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../../services/order.service';
import { AdminService } from '../../../services/admin.service';
import { Order, OrderStatus, Product, User, FormItem } from '../../../models';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { adminErrorMessage } from '../../../utils/admin-http';

@Component({
  selector: 'app-admin-orders',
  imports: [FormsModule, CurrencyPipe, DatePipe],
  templateUrl: './admin-orders.component.html',
  styleUrl: './admin-orders.component.scss'
})
export class AdminOrdersComponent implements OnInit {
  private readonly orderService = inject(OrderService);
  private readonly adminService = inject(AdminService);

  orderList = signal<Order[]>([]);
  productList = signal<Product[]>([]);
  userList = signal<User[]>([]);

  flash = signal('');
  flashErr = signal(false);
  showModal = signal(false);
  isEdit = signal(false);
  loading = signal(false);
  modalError = signal('');

  currentOrderId: number | null = null;
  customerEmail = '';
  shippingAddress = '';
  status: OrderStatus = 'PENDING';
  formItems = signal<FormItem[]>([]);

  selectedProductId: number | null = null;
  selectedQuantity = 1;

  statuses: OrderStatus[] = ['PENDING', 'PAID', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

  ngOnInit(): void {
    this.load();
    this.adminService.listProducts().subscribe({
      next: prods => this.productList.set(prods.filter(p => p.active !== false && p.stock > 0)),
      error: () => this.showFlash('Failed to load products.', true)
    });
    this.adminService.users().subscribe({
      next: users => this.userList.set(users.filter(u => u.enabled !== false)),
      error: () => this.showFlash('Failed to load customers.', true)
    });
  }

  load(): void {
    this.orderService.adminList().subscribe({
      next: orders => this.orderList.set(orders),
      error: e => this.showFlash(adminErrorMessage(e, 'Failed to load orders.'), true)
    });
  }

  showFlash(msg: string, isError = false): void {
    this.flash.set(msg);
    this.flashErr.set(isError);
    setTimeout(() => this.flash.set(''), 4000);
  }

  openCreate(): void {
    this.isEdit.set(false);
    this.currentOrderId = null;
    this.customerEmail = '';
    this.shippingAddress = '';
    this.status = 'PENDING';
    this.formItems.set([]);
    this.selectedProductId = null;
    this.selectedQuantity = 1;
    this.modalError.set('');
    this.showModal.set(true);
  }

  openEdit(order: Order): void {
    this.isEdit.set(true);
    this.currentOrderId = order.id;
    this.customerEmail = order.customerEmail ?? '';
    this.shippingAddress = order.shippingAddress ?? '';
    this.status = order.status;
    this.modalError.set('');

    const items: FormItem[] = (order.items ?? []).map(i => {
      const match = this.productList().find(p => p.id === i.productId);
      return {
        productId: i.productId,
        quantity: i.quantity,
        name: i.productName ?? match?.name ?? 'Unknown product',
        price: i.unitPrice ?? match?.price ?? 0
      };
    });
    this.formItems.set(items);
    this.selectedProductId = null;
    this.selectedQuantity = 1;
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  addFormItem(): void {
    if (!this.selectedProductId) {
      this.modalError.set('Please select a product from the catalog.');
      return;
    }
    const product = this.productList().find(p => p.id === this.selectedProductId);
    if (!product) return;

    if (this.selectedQuantity < 1) {
      this.modalError.set('Quantity must be at least 1.');
      return;
    }

    const currentList = [...this.formItems()];
    const existing = currentList.find(i => i.productId === product.id);

    if (existing) {
      existing.quantity += this.selectedQuantity;
    } else {
      currentList.push({
        productId: product.id,
        quantity: this.selectedQuantity,
        name: product.name,
        price: product.price
      });
    }

    this.formItems.set(currentList);
    this.modalError.set('');
    this.selectedProductId = null;
    this.selectedQuantity = 1;
  }

  adjustItemQty(index: number, diff: number): void {
    const list = [...this.formItems()];
    const item = list[index];
    if (!item) return;

    item.quantity += diff;
    if (item.quantity <= 0) {
      list.splice(index, 1);
    }
    this.formItems.set(list);
  }

  removeFormItem(index: number): void {
    const list = [...this.formItems()];
    list.splice(index, 1);
    this.formItems.set(list);
  }

  calculateFormTotal(): number {
    return this.formItems().reduce((sum, item) => sum + (item.price ?? 0) * item.quantity, 0);
  }

  saveOrder(): void {
    if (this.formItems().length === 0) {
      this.modalError.set('An order must contain at least one item.');
      return;
    }
    if (!this.shippingAddress.trim()) {
      this.modalError.set('Shipping address is required.');
      return;
    }
    if (!this.isEdit() && !this.customerEmail.trim()) {
      this.modalError.set('Customer is required.');
      return;
    }

    this.loading.set(true);
    this.modalError.set('');

    const payloadItems = this.formItems().map(i => ({
      productId: i.productId,
      quantity: i.quantity
    }));

    if (this.isEdit()) {
      this.orderService.adminUpdate(this.currentOrderId!, {
        shippingAddress: this.shippingAddress,
        status: this.status,
        items: payloadItems
      }).subscribe({
        next: () => {
          this.loading.set(false);
          this.showModal.set(false);
          this.showFlash(`Order #${this.currentOrderId} updated.`);
          this.load();
        },
        error: e => {
          this.loading.set(false);
          this.modalError.set(adminErrorMessage(e, 'Failed to update order.'));
        }
      });
    } else {
      this.orderService.adminCreate({
        customerEmail: this.customerEmail,
        shippingAddress: this.shippingAddress,
        status: this.status,
        items: payloadItems
      }).subscribe({
        next: newOrder => {
          this.loading.set(false);
          this.showModal.set(false);
          this.showFlash(`Order #${newOrder.id} created.`);
          this.load();
        },
        error: e => {
          this.loading.set(false);
          this.modalError.set(adminErrorMessage(e, 'Failed to create order.'));
        }
      });
    }
  }

  del(o: Order): void {
    if (!confirm(`Delete order #${o.id}? Stock will be restored.`)) return;
    this.orderService.deleteOrder(o.id).subscribe({
      next: () => {
        this.showFlash(`Order #${o.id} deleted.`);
        this.load();
      },
      error: e => this.showFlash(adminErrorMessage(e, 'Failed to delete order.'), true)
    });
  }
}
