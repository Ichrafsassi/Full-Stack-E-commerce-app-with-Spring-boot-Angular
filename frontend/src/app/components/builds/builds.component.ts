import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ContentService } from '../../services/content.service';
import { ProductImageComponent } from '../../shared/product-image/product-image.component';
import { picsumUrl } from '../../utils/product-image';
import { ContentCard, parseContentCards } from '../../utils/content-cards';

const DEFAULT_BUILDS: ContentCard[] = [
  { title: 'Streamer Pro', description: 'RTX 4070 · Ryzen 7 · 32GB · 2TB NVMe', search: 'Nebula', imageSeed: 'streamer-pro' },
  { title: 'Dev Workstation', description: 'High-core CPU · 128GB RAM · multi-monitor ready', search: 'Titan', imageSeed: 'dev-workstation' },
  { title: 'Compact SFF', description: 'Mini ITX · portable power · desk-friendly', search: 'Quantum', imageSeed: 'compact-sff' }
];

@Component({
  selector: 'app-builds',
  imports: [RouterLink, ProductImageComponent],
  templateUrl: './builds.component.html',
  styleUrl: './builds.component.scss'
})
export class BuildsComponent implements OnInit {
  private readonly content = inject(ContentService);
  title = signal('Custom Builds');
  subtitle = signal('Pre-configured battle stations — or mix components from our catalog.');
  cards = signal<ContentCard[]>(DEFAULT_BUILDS);

  ngOnInit(): void {
    this.content.byKey('builds').subscribe({
      next: c => {
        this.title.set(c.title);
        this.cards.set(parseContentCards(c.body, DEFAULT_BUILDS));
      },
      error: () => this.cards.set(DEFAULT_BUILDS)
    });
  }

  img(seed: string): string {
    return picsumUrl(seed);
  }
}
