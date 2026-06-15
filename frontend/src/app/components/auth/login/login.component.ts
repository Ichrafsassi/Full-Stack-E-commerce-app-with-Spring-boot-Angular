import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  email = '';
  password = '';
  showPwd = signal(false);
  error = signal('');

  google(): void {
    this.auth.googleSignIn().subscribe({
      error: e => this.error.set(e.error?.message ?? 'Google sign-in is not configured on the server.')
    });
  }

  submit(): void {
    this.error.set('');
    this.auth.login(this.email, this.password).subscribe({
      next: () => this.router.navigate(['/']),
      error: e => this.error.set(e.error?.message ?? 'Login failed')
    });
  }
}
