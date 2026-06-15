import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CatalogService } from '../../../services/catalog.service';
import { AdminService } from '../../../services/admin.service';
import { Category, Product } from '../../../models';
import { CurrencyPipe } from '@angular/common';
import { adminErrorMessage } from '../../../utils/admin-http';
import { ProductImageComponent } from '../../../shared/product-image/product-image.component';
import { picsumUrl } from '../../../utils/product-image';
import { toProductRequest } from '../../../utils/product-request';

@Component({
  selector: 'app-admin-products',
  imports: [FormsModule, CurrencyPipe, ProductImageComponent],
  templateUrl: './admin-products.component.html',
  styleUrl: './admin-products.component.scss'
})
export class AdminProductsComponent implements OnInit {
  private readonly catalog = inject(CatalogService);
  private readonly admin = inject(AdminService);
  products = signal<Product[]>([]);
  categories = signal<Category[]>([]);
  flash = signal('');
  flashErr = signal(false);
  modalOpen = signal(false);
  editingId: number | null = null;
  form = { name: '', description: '', price: 0, stock: 0, categoryId: undefined as number | undefined, imageUrl: '', active: true };

  ngOnInit(): void {
    this.catalog.getCategories().subscribe(c => this.categories.set(c));
    this.load();
  }

  previewUrl(): string {
    return this.form.imageUrl?.trim() || picsumUrl(this.form.name || 'product');
  }

  load(): void {
    this.admin.listProducts().subscribe(p => this.products.set(p));
  }

  openCreate(): void {
    this.editingId = null;
    this.form = { name: '', description: '', price: 0, stock: 0, categoryId: undefined, imageUrl: '', active: true };
    this.modalOpen.set(true);
  }

  openEdit(p: Product): void {
    this.editingId = p.id;
    this.form = {
      name: p.name, description: p.description ?? '', price: p.price, stock: p.stock,
      categoryId: p.categoryId, imageUrl: p.imageUrl ?? '', active: p.active !== false
    };
    this.modalOpen.set(true);
  }

  closeModal(): void {
    this.modalOpen.set(false);
  }

  save(): void {
    this.flash.set('');
    if (this.form.price <= 0) {
      this.flashErr.set(true);
      this.flash.set('Price must be greater than 0.');
      return;
    }
    const body = toProductRequest(this.form);
    const req = this.editingId
      ? this.admin.updateProduct(this.editingId, body)
      : this.admin.createProduct(body);
    req.subscribe({
      next: () => {
        this.flashErr.set(false);
        this.flash.set('Product saved.');
        this.closeModal();
        this.load();
      },
      error: e => { this.flashErr.set(true); this.flash.set(adminErrorMessage(e, 'Save failed')); }
    });
  }

  toggleActive(p: Product): void {
    const next = p.active === false;
    this.admin.updateProduct(p.id, toProductRequest({ ...p, active: next })).subscribe({
      next: () => {
        this.flashErr.set(false);
        this.flash.set(`Product ${next ? 'activated' : 'deactivated'}.`);
        this.load();
      },
      error: e => { this.flashErr.set(true); this.flash.set(adminErrorMessage(e, 'Update failed')); }
    });
  }

  del(p: Product): void {
    if (!confirm(`Delete "${p.name}"? It will be hidden from the shop.`)) return;
    this.admin.deleteProduct(p.id).subscribe({
      next: () => { this.flashErr.set(false); this.flash.set('Product deleted.'); this.load(); },
      error: e => { this.flashErr.set(true); this.flash.set(adminErrorMessage(e, 'Delete failed')); }
    });
  }
}
