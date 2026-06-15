import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../services/admin.service';
import { Role, User } from '../../../models';
import { adminErrorMessage } from '../../../utils/admin-http';

@Component({
  selector: 'app-admin-users',
  imports: [FormsModule],
  templateUrl: './admin-users.component.html',
  styleUrl: './admin-users.component.scss'
})
export class AdminUsersComponent implements OnInit {
  private readonly admin = inject(AdminService);
  users = signal<User[]>([]);
  flash = signal('');
  flashErr = signal(false);
  editId: number | null = null;
  form = { email: '', password: '', firstName: '', lastName: '', role: 'USER' as Role, enabled: true };

  ngOnInit(): void { this.load(); }
  load(): void { this.admin.users().subscribe(u => this.users.set(u)); }

  save(): void {
    this.flash.set('');
    const done = () => this.afterSave('Saved.');
    const fail = (e: unknown) => {
      this.flashErr.set(true);
      this.flash.set(adminErrorMessage(e, 'Save failed'));
    };
    if (this.editId) {
      const body: Partial<User & { password?: string }> = {
        email: this.form.email,
        firstName: this.form.firstName,
        lastName: this.form.lastName,
        role: this.form.role,
        enabled: this.form.enabled
      };
      if (this.form.password.trim()) body.password = this.form.password;
      this.admin.updateUser(this.editId, body).subscribe({ next: done, error: fail });
    } else {
      this.admin.createUser(this.form as User & { password: string }).subscribe({ next: done, error: fail });
    }
  }

  edit(u: User): void {
    this.editId = u.id;
    this.form = { email: u.email, password: '', firstName: u.firstName, lastName: u.lastName, role: u.role, enabled: u.enabled ?? true };
    this.flash.set('');
  }

  toggleEnabled(u: User): void {
    const next = u.enabled === false;
    this.admin.updateUser(u.id, { enabled: next }).subscribe({
      next: () => { this.flashErr.set(false); this.flash.set(`User ${next ? 'enabled' : 'disabled'}.`); this.load(); },
      error: e => { this.flashErr.set(true); this.flash.set(adminErrorMessage(e, 'Update failed')); }
    });
  }

  del(u: User): void {
    if (!confirm(`Delete user "${u.email}"? Their orders and cart will be removed.`)) return;
    this.admin.deleteUser(u.id).subscribe({
      next: () => { this.flashErr.set(false); this.flash.set('User deleted.'); this.load(); },
      error: e => { this.flashErr.set(true); this.flash.set(adminErrorMessage(e, 'Delete failed')); }
    });
  }

  reset(): void { this.afterSave(''); }

  afterSave(msg: string): void {
    this.editId = null;
    this.form = { email: '', password: '', firstName: '', lastName: '', role: 'USER', enabled: true };
    if (msg) { this.flashErr.set(false); this.flash.set(msg); }
    this.load();
  }
}
