import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  firstName = '';
  lastName = '';
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
    this.auth.register({ email: this.email, password: this.password, firstName: this.firstName, lastName: this.lastName }).subscribe({
      next: () => this.router.navigate(['/']),
      error: e => this.error.set(e.error?.message ?? 'Registration failed')
    });
  }
}
