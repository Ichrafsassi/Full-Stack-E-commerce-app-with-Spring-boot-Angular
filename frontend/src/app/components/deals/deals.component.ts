import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CatalogService } from '../../services/catalog.service';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { DealsResponse } from '../../models';
import { ProductPriceComponent } from '../../shared/product-price/product-price.component';
import { ProductImageComponent } from '../../shared/product-image/product-image.component';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-deals',
  imports: [RouterLink, ProductPriceComponent, ProductImageComponent, CurrencyPipe],
  templateUrl: './deals.component.html',
  styleUrl: './deals.component.scss'
})
export class DealsComponent implements OnInit {
  private readonly catalog = inject(CatalogService);
  private readonly cart = inject(CartService);
  readonly auth = inject(AuthService);
  deals = signal<DealsResponse | null>(null);
  loadError = signal(false);
  flash = signal('');
  flashErr = signal(false);

  ngOnInit(): void {
    this.catalog.getDeals().subscribe({
      next: d => { this.deals.set(d); this.loadError.set(false); },
      error: () => { this.loadError.set(true); this.deals.set(null); }
    });
  }

  add(id: number): void {
    if (!this.auth.isLoggedIn()) {
      this.flashErr.set(true);
      this.flash.set('Sign in to add items to your cart.');
      return;
    }
    this.cart.add(id).subscribe({
      next: () => { this.flashErr.set(false); this.flash.set('Added to cart.'); },
      error: e => {
        this.flashErr.set(true);
        this.flash.set((e as { error?: { message?: string } })?.error?.message ?? 'Could not add to cart.');
      }
    });
  }
}
