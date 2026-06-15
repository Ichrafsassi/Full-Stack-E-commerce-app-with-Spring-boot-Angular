import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ContentService } from '../../../services/content.service';
import { ContentType, SiteContent } from '../../../models';
import { adminErrorMessage } from '../../../utils/admin-http';

@Component({
  selector: 'app-admin-cms',
  imports: [FormsModule],
  templateUrl: './admin-cms.component.html',
  styleUrl: './admin-cms.component.scss'
})
export class AdminCmsComponent implements OnInit {
  private readonly content = inject(ContentService);
  items = signal<SiteContent[]>([]);
  flash = signal('');
  flashErr = signal(false);
  types: ContentType[] = ['PAGE', 'BANNER', 'FOOTER', 'ANNOUNCEMENT'];
  editId: number | null = null;
  form = { contentKey: '', title: '', body: '', type: 'PAGE' as ContentType, published: true };

  ngOnInit(): void { this.reload(); }

  reload(): void { this.content.adminAll().subscribe(i => this.items.set(i)); }

  save(): void {
    this.flash.set('');
    const req = this.editId ? this.content.update(this.editId, this.form) : this.content.create(this.form);
    req.subscribe({
      next: () => this.cancel('Content saved.'),
      error: e => { this.flashErr.set(true); this.flash.set(adminErrorMessage(e, 'Save failed')); }
    });
  }

  startEdit(c: SiteContent): void {
    this.editId = c.id;
    this.form = { contentKey: c.contentKey, title: c.title, body: c.body ?? '', type: c.type, published: c.published };
    this.flash.set('');
  }

  del(c: SiteContent): void {
    if (!confirm(`Delete content "${c.contentKey}"?`)) return;
    this.content.delete(c.id).subscribe({
      next: () => { this.flashErr.set(false); this.flash.set('Content deleted.'); this.reload(); },
      error: e => { this.flashErr.set(true); this.flash.set(adminErrorMessage(e, 'Delete failed')); }
    });
  }

  cancel(msg = ''): void {
    this.editId = null;
    this.form = { contentKey: '', title: '', body: '', type: 'PAGE', published: true };
    if (msg) { this.flashErr.set(false); this.flash.set(msg); }
    this.reload();
  }
}
