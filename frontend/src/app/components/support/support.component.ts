import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ContentService } from '../../services/content.service';
import { ContentCard, parseContentCards } from '../../utils/content-cards';

const DEFAULT_SUPPORT: ContentCard[] = [
  { title: 'Shipping', description: 'Orders ship within 2 business days. Tracking emailed after dispatch.' },
  { title: 'Returns', description: '30-day returns on unopened hardware. Contact support@nerdstech.com.' },
  { title: 'Warranty', description: 'Manufacturer warranty on all components. Extended coverage on gaming PCs.' },
  { title: 'Build help', description: 'Not sure what fits? Visit Custom builds or open a ticket.', link: '/builds' }
];

@Component({
  selector: 'app-support',
  imports: [RouterLink],
  templateUrl: './support.component.html'
})
export class SupportComponent implements OnInit {
  private readonly content = inject(ContentService);
  title = signal('Support');
  cards = signal<ContentCard[]>(DEFAULT_SUPPORT);

  ngOnInit(): void {
    this.content.byKey('support').subscribe({
      next: c => {
        this.title.set(c.title);
        this.cards.set(parseContentCards(c.body, DEFAULT_SUPPORT));
      },
      error: () => this.cards.set(DEFAULT_SUPPORT)
    });
  }
}
