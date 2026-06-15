import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CatalogService } from '../../../services/catalog.service';
import { AdminService } from '../../../services/admin.service';
import { Category } from '../../../models';
import { adminErrorMessage } from '../../../utils/admin-http';

@Component({
  selector: 'app-admin-categories',
  imports: [FormsModule],
  templateUrl: './admin-categories.component.html',
  styleUrl: './admin-categories.component.scss'
})
export class AdminCategoriesComponent implements OnInit {
  private readonly catalog = inject(CatalogService);
  private readonly admin = inject(AdminService);
  categories = signal<Category[]>([]);
  flash = signal('');
  flashErr = signal(false);
  name = '';
  description = '';
  imageUrl = '';
  editId: number | null = null;

  ngOnInit(): void { this.load(); }
  load(): void { this.catalog.getCategories().subscribe(c => this.categories.set(c)); }

  save(): void {
    this.flash.set('');
    const body = { name: this.name, description: this.description, imageUrl: this.imageUrl };
    const req = this.editId ? this.admin.updateCategory(this.editId, body) : this.admin.createCategory(body);
    req.subscribe({
      next: () => this.cancel('Category saved.'),
      error: e => { this.flashErr.set(true); this.flash.set(adminErrorMessage(e, 'Save failed')); }
    });
  }

  startEdit(c: Category): void {
    this.editId = c.id;
    this.name = c.name;
    this.description = c.description ?? '';
    this.imageUrl = c.imageUrl ?? '';
    this.flash.set('');
  }

  del(c: Category): void {
    if (!confirm(`Delete category "${c.name}"?`)) return;
    this.admin.deleteCategory(c.id).subscribe({
      next: () => { this.flashErr.set(false); this.flash.set('Category deleted.'); this.load(); },
      error: e => { this.flashErr.set(true); this.flash.set(adminErrorMessage(e, 'Delete failed')); }
    });
  }

  cancel(msg = ''): void {
    this.name = '';
    this.description = '';
    this.imageUrl = '';
    this.editId = null;
    if (msg) { this.flashErr.set(false); this.flash.set(msg); }
    this.load();
  }
}
