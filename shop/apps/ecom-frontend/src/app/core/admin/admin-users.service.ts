import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../api';
import { UserDto } from '../auth/auth.service';

export interface CreateUserRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: string;
}

export interface UpdateUserRequest {
  firstName: string;
  lastName: string;
}

@Injectable({ providedIn: 'root' })
export class AdminUsersService {
  private readonly http = inject(HttpClient);

  listUsers() {
    return this.http.get<UserDto[]>(`${API_BASE_URL}/api/admin/users`);
  }

  createUser(request: CreateUserRequest) {
    return this.http.post<UserDto>(`${API_BASE_URL}/api/admin/users`, request);
  }

  updateUser(userId: string, request: UpdateUserRequest) {
    return this.http.put<UserDto>(`${API_BASE_URL}/api/admin/users/${userId}`, request);
  }

  updateRole(userId: string, role: string) {
    return this.http.put<UserDto>(`${API_BASE_URL}/api/admin/users/${userId}/role`, {
      role,
    });
  }

  deleteUser(userId: string) {
    return this.http.delete<void>(`${API_BASE_URL}/api/admin/users/${userId}`);
  }
}

