import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { API_BASE_URL } from '../api';
import { decodeJwtPayload } from './jwt';

export type UserDto = {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
};

type AuthResponseDto = {
  token: string;
  user: UserDto;
};

const TOKEN_KEY = 'ecom_token';
const USER_KEY = 'ecom_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly platformId = inject(PLATFORM_ID);
  private readonly isBrowser = isPlatformBrowser(this.platformId);

  login(email: string, password: string) {
    return this.http
      .post<AuthResponseDto>(`${API_BASE_URL}/api/auth/login`, { email, password })
      .pipe(tap((res) => this.persistSession(res)));
  }

  register(payload: {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
  }) {
    return this.http
      .post<AuthResponseDto>(`${API_BASE_URL}/api/auth/register`, payload)
      .pipe(tap((res) => this.persistSession(res)));
  }

  fetchCurrentUser() {
    return this.http.get<UserDto>(`${API_BASE_URL}/api/users/me`).pipe(
      tap((user) => {
        if (!this.isBrowser) return;
        localStorage.setItem(USER_KEY, JSON.stringify(user));
      })
    );
  }

  logout() {
    if (this.isBrowser) {
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(USER_KEY);
    }
    this.router.navigateByUrl('/');
  }

  getToken(): string | null {
    if (!this.isBrowser) return null;
    return localStorage.getItem(TOKEN_KEY);
  }

  getUser(): UserDto | null {
    if (!this.isBrowser) return null;
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as UserDto;
    } catch {
      localStorage.removeItem(USER_KEY);
      return null;
    }
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    const payload = decodeJwtPayload(token);
    return !!payload && payload.exp * 1000 > Date.now();
  }

  isAdmin(): boolean {
    return this.getUser()?.role === 'ADMIN';
  }

  private persistSession(res: AuthResponseDto) {
    if (!this.isBrowser) return;
    localStorage.setItem(TOKEN_KEY, res.token);
    localStorage.setItem(USER_KEY, JSON.stringify(res.user));
  }
}
