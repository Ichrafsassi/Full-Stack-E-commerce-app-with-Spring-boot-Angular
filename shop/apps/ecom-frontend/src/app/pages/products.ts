import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { CatalogApiService, CategoryDto, ProductDto } from '../core/catalog/catalog-api.service';
import { ProductCatalogService } from '../core/catalog/product-catalog.service';
import { CartService } from '../core/cart/cart.service';

type SortOption = 'newest' | 'oldest' | 'price-asc' | 'price-desc';

@Component({
  selector: 'ecom-products',
  imports: [FormsModule, CurrencyPipe],
  templateUrl: './products.html',
  styleUrl: './products.scss',
})
export class Products {
  private readonly catalogApi = inject(CatalogApiService);
  private readonly fallbackCatalog = inject(ProductCatalogService);
  private readonly cart = inject(CartService);

  protected categories: CategoryDto[] = [];
  protected products: ProductDto[] = [];
  protected loading = false;
  protected error: string | null = null;

  protected search = '';
  protected selectedCategoryId = 'All';
  protected sortBy: SortOption = 'newest';

  ngOnInit(): void {
    this.loading = true;
    this.error = null;

    this.catalogApi
      .listCategories()
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (cats) => (this.categories = cats),
        error: () => {
          this.categories = this.fallbackCatalog
            .listCategories()
            .map((name, idx) => ({ id: `fallback-${idx}`, name, slug: name.toLowerCase() }));
        },
      });

    this.reloadProducts();
  }

  reloadProducts() {
    this.loading = true;
    this.error = null;

    const categoryId = this.selectedCategoryId === 'All' ? undefined : this.selectedCategoryId;

    this.catalogApi
      .listProducts({ categoryId, sort: this.sortBy })
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (products) => (this.products = products),
        error: () => {
          this.error = 'Failed to load products from API, showing demo catalog.';
          this.products = this.fallbackCatalog.listProducts().map((p) => ({
            id: p.id,
            name: p.name,
            description: p.description,
            price: p.price,
            stock: p.stock,
            active: true,
            imageUrl: p.image,
            size: null,
            categoryId: p.category,
            categoryName: p.category,
            createdAt: new Date().toISOString(),
          }));
        },
      });
  }

  protected get filteredProducts(): ProductDto[] {
    const searchValue = this.search.trim().toLowerCase();

    const filtered = this.products.filter((product) => {
      const matchesSearch =
        !searchValue ||
        product.name.toLowerCase().includes(searchValue) ||
        product.description.toLowerCase().includes(searchValue);
      const matchesCategory = this.selectedCategoryId === 'All' || product.categoryId === this.selectedCategoryId;

      return matchesSearch && matchesCategory;
    });

    return [...filtered].sort((left, right) => {
      switch (this.sortBy) {
        case 'price-asc':
          return left.price - right.price;
        case 'price-desc':
          return right.price - left.price;
        case 'oldest':
          return new Date(left.createdAt).getTime() - new Date(right.createdAt).getTime();
        case 'newest':
          return new Date(right.createdAt).getTime() - new Date(left.createdAt).getTime();
        default:
          return 0;
      }
    });
  }

  addToCart(product: ProductDto) {
    this.cart.addProduct(product);
  }
}
