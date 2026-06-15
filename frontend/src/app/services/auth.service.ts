import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { AuthResponse, User } from '../models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly tokenKey = 'ecommerce_token';
  private readonly userKey = 'ecommerce_user';

  readonly user = signal<User | null>(this.loadUser());
  readonly isLoggedIn = computed(() => !!this.user());
  readonly isAdmin = computed(() => this.user()?.role === 'ADMIN');

  login(email: string, password: string) {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, { email, password }).pipe(
      tap(res => this.persist(res))
    );
  }

  register(data: { email: string; password: string; firstName: string; lastName: string }) {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, data).pipe(
      tap(res => this.persist(res))
    );
  }

  /** Stub until Google OAuth client is configured on the server. */
  googleSignIn() {
    return this.http.post<{ message: string }>(`${environment.apiUrl}/auth/google`, {});
  }

  refreshProfileIfLoggedIn(): void {
    if (!this.getToken()) return;
    this.http.get<User>(`${environment.apiUrl}/auth/me`).subscribe({
      next: user => {
        if (user.enabled === false) {
          this.logout();
          return;
        }
        localStorage.setItem(this.userKey, JSON.stringify(user));
        this.user.set(user);
      },
      error: () => this.logout()
    });
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    this.user.set(null);
    this.router.navigate(['/']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private persist(res: AuthResponse): void {
    localStorage.setItem(this.tokenKey, res.token);
    localStorage.setItem(this.userKey, JSON.stringify(res.user));
    this.user.set(res.user);
  }

  private loadUser(): User | null {
    const raw = localStorage.getItem(this.userKey);
    return raw ? JSON.parse(raw) as User : null;
  }
}
