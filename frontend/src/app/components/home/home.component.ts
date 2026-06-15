import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CatalogService } from '../../services/catalog.service';
import { ContentService } from '../../services/content.service';
import { DealsResponse, Product, SiteContent } from '../../models';
import { ProductPriceComponent } from '../../shared/product-price/product-price.component';
import { ProductImageComponent } from '../../shared/product-image/product-image.component';

@Component({
  selector: 'app-home',
  imports: [RouterLink, ProductPriceComponent, ProductImageComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  private readonly catalog = inject(CatalogService);
  private readonly content = inject(ContentService);
  hero = signal<SiteContent | null>(null);
  categories = signal<{ id: number; name: string }[]>([]);
  deals = signal<DealsResponse | null>(null);
  featured = signal<Product[]>([]);

  ngOnInit(): void {
    this.content.byKey('home-hero').subscribe({
      next: h => this.hero.set(h),
      error: () => this.hero.set({
        id: 0, contentKey: 'home-hero', title: "NERD'S TECH — Power Your Setup",
        body: 'Cyber-grade components, gaming rigs, and dev gear.',
        type: 'BANNER', published: true
      })
    });
    this.catalog.getCategories().subscribe(c => {
      this.categories.set(c.map(x => ({ id: x.id, name: x.name })));
    });
    this.catalog.getDeals().subscribe({
      next: d => this.deals.set(d),
      error: () => this.deals.set(null)
    });
    this.catalog.getProducts().subscribe(p => {
      this.featured.set(p.filter(x => x.active !== false).slice(0, 4));
    });
  }
}
