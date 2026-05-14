import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { CartService } from '../core/cart/cart.service';
import { AuthService } from '../core/auth/auth.service';

@Component({
  selector: 'ecom-navbar',
  imports: [RouterLink, FaIconComponent],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {
  protected readonly auth = inject(AuthService);
  protected readonly cart = inject(CartService);
}
