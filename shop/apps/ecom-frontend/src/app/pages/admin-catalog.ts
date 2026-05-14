import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { CatalogApiService, ProductDto } from '../core/catalog/catalog-api.service';
import { AdminCatalogService, AdminCategoryDto } from '../core/admin/admin-catalog.service';

@Component({
  selector: 'ecom-admin-catalog',
  imports: [ReactiveFormsModule, CurrencyPipe],
  templateUrl: './admin-catalog.html',
  styleUrl: './admin-catalog.scss',
})
export class AdminCatalog implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly adminCatalog = inject(AdminCatalogService);
  private readonly catalogApi = inject(CatalogApiService);

  protected loading = false;
  protected error: string | null = null;
  protected categories: AdminCategoryDto[] = [];
  protected products: ProductDto[] = [];

  protected readonly categoryForm = this.fb.nonNullable.group({
    name: ['', [Validators.required]],
  });

  protected readonly productForm = this.fb.nonNullable.group({
    name: ['', [Validators.required]],
    description: ['', [Validators.required]],
    price: [99, [Validators.required, Validators.min(1)]],
    stock: [10, [Validators.required, Validators.min(0)]],
    categoryId: ['', [Validators.required]],
    imageUrl: [''],
    size: [''],
  });

  ngOnInit(): void {
    this.reload();
  }

  reload() {
    this.error = null;
    this.loading = true;
    this.adminCatalog
      .listCategories()
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (categories) => {
          this.categories = categories;
          if (!this.productForm.controls.categoryId.value && categories.length > 0) {
            this.productForm.controls.categoryId.setValue(categories[0].id);
          }
          this.reloadProducts();
        },
        error: () => (this.error = 'Failed to load admin catalog'),
      });
  }

  reloadProducts() {
    this.catalogApi.listProducts({ sort: 'newest' }).subscribe({
      next: (products) => (this.products = products),
      error: () => (this.products = []),
    });
  }

  createCategory() {
    this.error = null;
    if (this.categoryForm.invalid) {
      this.categoryForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.adminCatalog
      .createCategory(this.categoryForm.getRawValue().name)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.categoryForm.reset({ name: '' });
          this.reload();
        },
        error: (err) => (this.error = err?.error?.error ?? 'Failed to create category'),
      });
  }

  deleteCategory(categoryId: string) {
    this.error = null;
    this.loading = true;
    this.adminCatalog
      .deleteCategory(categoryId)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => this.reload(),
        error: (err) => (this.error = err?.error?.error ?? 'Failed to delete category'),
      });
  }

  createProduct() {
    this.error = null;
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    const value = this.productForm.getRawValue();
    this.adminCatalog
      .createProduct({
        name: value.name,
        description: value.description,
        price: Number(value.price),
        stock: Number(value.stock),
        categoryId: value.categoryId,
        imageUrl: value.imageUrl || null,
        size: value.size || null,
      })
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.productForm.patchValue({ name: '', description: '', imageUrl: '', size: '' });
          this.reloadProducts();
        },
        error: (err) => (this.error = err?.error?.error ?? 'Failed to create product'),
      });
  }

  deleteProduct(productId: string) {
    this.error = null;
    this.loading = true;
    this.adminCatalog
      .deleteProduct(productId)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => this.reloadProducts(),
        error: (err) => (this.error = err?.error?.error ?? 'Failed to delete product'),
      });
  }
}

