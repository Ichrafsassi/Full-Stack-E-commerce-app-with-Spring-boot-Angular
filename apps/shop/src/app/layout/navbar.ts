import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faCartShopping } from '@fortawesome/free-solid-svg-icons';
import { faUser } from '@fortawesome/free-regular-svg-icons';

@Component({
  selector: 'app-navbar',
  imports: [RouterModule, FaIconComponent],
  template: `
    <nav class="navbar">
      <div class="nav-container">
        <div class="nav-content">
          <div class="nav-left">
            <a routerLink="/" class="logo-link">
              <div class="logo-icon">EC</div>
              <span class="logo-text">E-Shop</span>
            </a>
            
            <div class="nav-links">
              <a routerLink="/" class="nav-link">Home</a>
              <a routerLink="/products" class="nav-link">Products</a>
            </div>
          </div>

          <div class="nav-right">
            <a routerLink="/cart" class="cart-link">
              <fa-icon [icon]="faCartShopping" size="lg"></fa-icon>
              <span class="cart-badge">0</span>
            </a>
            
            <button class="account-btn">
              <fa-icon [icon]="faUser"></fa-icon>
              <span class="account-text">Account</span>
            </button>
          </div>
        </div>
      </div>
    </nav>
  `,
  styles: [`
    .navbar {
      background: white;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
      border-bottom: 1px solid #e5e7eb;
      position: sticky;
      top: 0;
      z-index: 50;
      backdrop-filter: blur(8px);
    }

    .nav-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 1rem;
    }

    .nav-content {
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 64px;
    }

    .nav-left {
      display: flex;
      align-items: center;
      gap: 2rem;
    }

    .logo-link {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      font-size: 1.5rem;
      font-weight: bold;
      color: #3b82f6;
      text-decoration: none;
      transition: color 0.2s;
    }

    .logo-link:hover {
      color: #2563eb;
    }

    .logo-icon {
      width: 40px;
      height: 40px;
      background: #3b82f6;
      border-radius: 0.5rem;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-weight: bold;
    }

    .logo-text {
      color: #1f2937;
    }

    .nav-links {
      display: none;
      align-items: center;
      gap: 1.5rem;
    }

    @media (min-width: 768px) {
      .nav-links {
        display: flex;
      }
    }

    .nav-link {
      color: #374151;
      font-weight: 500;
      text-decoration: none;
      transition: color 0.2s;
    }

    .nav-link:hover {
      color: #3b82f6;
    }

    .nav-right {
      display: flex;
      align-items: center;
      gap: 1rem;
    }

    .cart-link {
      position: relative;
      padding: 0.5rem;
      color: #374151;
      text-decoration: none;
      border-radius: 9999px;
      transition: all 0.2s;
    }

    .cart-link:hover {
      color: #3b82f6;
      background: #f3f4f6;
    }

    .cart-badge {
      position: absolute;
      top: -4px;
      right: -4px;
      width: 20px;
      height: 20px;
      background: #3b82f6;
      color: white;
      font-size: 0.75rem;
      border-radius: 9999px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
    }

    .account-btn {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      padding: 0.5rem 1rem;
      background: #3b82f6;
      color: white;
      border: none;
      border-radius: 0.5rem;
      font-weight: 500;
      cursor: pointer;
      transition: background 0.2s;
    }

    .account-btn:hover {
      background: #2563eb;
    }

    .account-text {
      display: none;
    }

    @media (min-width: 640px) {
      .account-text {
        display: inline;
      }
    }

    .navbar a.active {
      color: #3b82f6;
      font-weight: 600;
    }
  `],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Navbar {
  protected readonly faCartShopping = faCartShopping;
  protected readonly faUser = faUser;
}
