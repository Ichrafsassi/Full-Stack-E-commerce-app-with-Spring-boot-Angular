import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CatalogService } from '../../services/catalog.service';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { Category, Product } from '../../models';
import { ProductPriceComponent } from '../../shared/product-price/product-price.component';
import { ProductImageComponent } from '../../shared/product-image/product-image.component';

@Component({
  selector: 'app-products',
  imports: [RouterLink, FormsModule, ProductPriceComponent, ProductImageComponent],
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss'
})
export class ProductsComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly catalog = inject(CatalogService);
  private readonly cart = inject(CartService);
  readonly auth = inject(AuthService);
  products = signal<Product[]>([]);
  categories = signal<Category[]>([]);
  categoryId?: number;
  search = '';

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.categoryId = params['categoryId'] ? Number(params['categoryId']) : undefined;
      this.search = params['search'] ?? '';
      this.load();
    });
    this.catalog.getCategories().subscribe(c => this.categories.set(c));
  }

  load(): void {
    this.catalog.getProducts(this.categoryId, this.search || undefined)
      .subscribe(p => this.products.set(p.filter(x => x.active !== false)));
  }

  flash = signal('');
  flashErr = signal(false);

  addToCart(productId: number): void {
    if (!this.auth.isLoggedIn()) {
      this.flashErr.set(true);
      this.flash.set('Sign in to add items to your cart.');
      return;
    }
    this.cart.add(productId).subscribe({
      next: () => { this.flashErr.set(false); this.flash.set('Added to cart.'); },
      error: e => {
        this.flashErr.set(true);
        this.flash.set((e as { error?: { message?: string } })?.error?.message ?? 'Could not add to cart.');
      }
    });
  }
}
