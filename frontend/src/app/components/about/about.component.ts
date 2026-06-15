import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ContentService } from '../../services/content.service';
import { SiteContent } from '../../models';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrl: './about.component.scss',
  imports: [RouterLink]
})
export class AboutComponent implements OnInit {
  private readonly content = inject(ContentService);
  page = signal<SiteContent | null>(null);

  ngOnInit(): void {
    this.content.byKey('about').subscribe({
      next: p => this.page.set(p),
      error: () => this.page.set({ id: 0, contentKey: 'about', title: "About NERD'S TECH", body: 'Your cyber hardware destination.', type: 'PAGE', published: true })
    });
  }
}
