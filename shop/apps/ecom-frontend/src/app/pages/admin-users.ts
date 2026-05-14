import { Component, OnInit, inject } from '@angular/core';
import { finalize } from 'rxjs';
import { AdminUsersService, CreateUserRequest, UpdateUserRequest } from '../core/admin/admin-users.service';
import { AuthService, UserDto } from '../core/auth/auth.service';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'ecom-admin-users',
  imports: [FormsModule, FontAwesomeModule],
  templateUrl: './admin-users.html',
  styleUrl: './admin-users.scss',
})
export class AdminUsers implements OnInit {
  private readonly adminUsers = inject(AdminUsersService);
  private readonly auth = inject(AuthService);

  protected users: UserDto[] = [];
  protected loading = false;
  protected error: string | null = null;
  protected readonly currentUserId = this.auth.getUser()?.id ?? null;
  protected adminCount = 0;
  protected userCount = 0;
  protected showCreateModal = false;
  protected showEditModal = false;
  protected showDeleteModal = false;
  protected selectedUser: UserDto | null = null;
  protected newUser: CreateUserRequest = {
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    role: 'USER',
  };
  protected editUser: UpdateUserRequest = {
    firstName: '',
    lastName: '',
  };

  ngOnInit(): void {
    this.reload();
  }

  reload() {
    this.error = null;
    this.loading = true;
    this.adminUsers
      .listUsers()
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (users) => {
          this.users = users;
          this.adminCount = users.filter((user) => user.role === 'ADMIN').length;
          this.userCount = users.filter((user) => user.role === 'USER').length;
        },
        error: () => (this.error = 'Failed to load users'),
      });
  }

  setRole(userId: string, role: string) {
    if (this.currentUserId === userId && role !== 'ADMIN') {
      this.error = 'You cannot remove your own admin access';
      return;
    }

    this.error = null;
    this.loading = true;
    this.adminUsers
      .updateRole(userId, role)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => this.reload(),
        error: (err) => {
          const msg = err?.error?.error ?? 'Failed to update role';
          this.error = msg;
        },
      });
  }

  openCreateModal() {
    this.newUser = { email: '', password: '', firstName: '', lastName: '', role: 'USER' };
    this.showCreateModal = true;
  }

  closeCreateModal() {
    this.showCreateModal = false;
  }

  createUser() {
    this.error = null;
    this.loading = true;
    this.adminUsers
      .createUser(this.newUser)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showCreateModal = false;
          this.reload();
        },
        error: (err) => {
          const msg = err?.error?.error ?? 'Failed to create user';
          this.error = msg;
        },
      });
  }

  openEditModal(user: UserDto) {
    this.selectedUser = user;
    this.editUser = {
      firstName: user.firstName,
      lastName: user.lastName,
    };
    this.showEditModal = true;
  }

  closeEditModal() {
    this.showEditModal = false;
  }

  updateUser() {
    if (!this.selectedUser) return;
    this.error = null;
    this.loading = true;
    this.adminUsers
      .updateUser(this.selectedUser.id, this.editUser)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showEditModal = false;
          this.reload();
        },
        error: (err) => {
          const msg = err?.error?.error ?? 'Failed to update user';
          this.error = msg;
        },
      });
  }

  openDeleteModal(user: UserDto) {
    this.selectedUser = user;
    this.showDeleteModal = true;
  }

  closeDeleteModal() {
    this.showDeleteModal = false;
  }

  deleteUser() {
    if (!this.selectedUser) return;
    if (this.selectedUser.id === this.currentUserId) {
      this.error = 'You cannot delete your own account';
      return;
    }
    this.error = null;
    this.loading = true;
    this.adminUsers
      .deleteUser(this.selectedUser.id)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showDeleteModal = false;
          this.reload();
        },
        error: (err) => {
          const msg = err?.error?.error ?? 'Failed to delete user';
          this.error = msg;
        },
      });
  }
}
