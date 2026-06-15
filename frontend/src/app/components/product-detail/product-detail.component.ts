import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CatalogService } from '../../services/catalog.service';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { Product } from '../../models';
import { ProductPriceComponent } from '../../shared/product-price/product-price.component';
import { ProductImageComponent } from '../../shared/product-image/product-image.component';
import { adminErrorMessage } from '../../utils/admin-http';

@Component({
  selector: 'app-product-detail',
  imports: [RouterLink, ProductPriceComponent, ProductImageComponent],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly catalog = inject(CatalogService);
  private readonly cart = inject(CartService);
  readonly auth = inject(AuthService);
  product = signal<Product | null>(null);
  error = signal('');
  flash = signal('');
  flashErr = signal(false);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.catalog.getProduct(id).subscribe({
      next: p => { this.product.set(p); this.error.set(''); },
      error: e => this.error.set(adminErrorMessage(e, 'Product not found.'))
    });
  }

  add(): void {
    if (!this.auth.isLoggedIn()) {
      this.flashErr.set(true);
      this.flash.set('Sign in to add items to your cart.');
      return;
    }
    const p = this.product();
    if (!p) return;
    this.cart.add(p.id).subscribe({
      next: () => { this.flashErr.set(false); this.flash.set('Added to cart.'); },
      error: e => { this.flashErr.set(true); this.flash.set(adminErrorMessage(e, 'Could not add to cart.')); }
    });
  }
}
