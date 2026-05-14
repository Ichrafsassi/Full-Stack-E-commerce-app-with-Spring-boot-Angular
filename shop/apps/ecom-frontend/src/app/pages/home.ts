import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { CatalogApiService, CategoryDto, ProductDto } from '../core/catalog/catalog-api.service';
import { ProductCatalogService } from '../core/catalog/product-catalog.service';
import { CartService } from '../core/cart/cart.service';

@Component({
  selector: 'ecom-home',
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home implements OnInit {
  private readonly catalogApi = inject(CatalogApiService);
  private readonly fallbackCatalog = inject(ProductCatalogService);
  private readonly cart = inject(CartService);

  protected featuredProducts: ProductDto[] = [];
  protected categories: CategoryDto[] = [
    { id: 'initial-1', name: 'Loading...', slug: 'loading' }
  ];
  protected loading = true;

  ngOnInit(): void {
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

    this.catalogApi.listProducts({ sort: 'newest' }).subscribe({
      next: (products) => (this.featuredProducts = products.slice(0, 3)),
      error: () => {
        this.featuredProducts = this.fallbackCatalog.getFeaturedProducts().map((p) => ({
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

  addToCart(product: ProductDto) {
    this.cart.addProduct(product);
  }
}
