import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faTruckFast } from '@fortawesome/free-solid-svg-icons';
import { faTwitter, faYoutube, faFacebook } from '@fortawesome/free-brands-svg-icons';

@Component({
  selector: 'app-footer',
  imports: [FaIconComponent, RouterModule],
  template: `
    <footer class="footer">
      <div class="footer-container">
        <div class="footer-grid">
          <div class="footer-col">
            <div class="footer-logo">
              <div class="footer-logo-icon">EC</div>
              <span class="footer-logo-text">E-Shop</span>
            </div>
            <p class="footer-desc">Your one-stop destination for quality products at great prices. Shop with confidence!</p>
            <div class="social-links">
              <a href="#" class="social-link">
                <fa-icon [icon]="faTwitter" size="lg"></fa-icon>
              </a>
              <a href="#" class="social-link">
                <fa-icon [icon]="faFacebook" size="lg"></fa-icon>
              </a>
              <a href="#" class="social-link">
                <fa-icon [icon]="faYoutube" size="lg"></fa-icon>
              </a>
            </div>
          </div>
          
          <div class="footer-col">
            <h3 class="footer-heading">Quick Links</h3>
            <ul class="footer-links">
              <li><a routerLink="/" class="footer-link">Home</a></li>
              <li><a routerLink="/products" class="footer-link">Products</a></li>
              <li><a href="#" class="footer-link">About Us</a></li>
              <li><a href="#" class="footer-link">Contact</a></li>
            </ul>
          </div>
          
          <div class="footer-col">
            <h3 class="footer-heading">Customer Service</h3>
            <ul class="footer-links">
              <li><a href="#" class="footer-link">Shipping Info</a></li>
              <li><a href="#" class="footer-link">Returns & Exchanges</a></li>
              <li><a href="#" class="footer-link">FAQ</a></li>
              <li><a href="#" class="footer-link">Track Order</a></li>
            </ul>
          </div>
          
          <div class="footer-col">
            <h3 class="footer-heading">Legal</h3>
            <ul class="footer-links">
              <li><a href="#" class="footer-link">Privacy Policy</a></li>
              <li><a href="#" class="footer-link">Terms of Service</a></li>
              <li><a href="#" class="footer-link">Cookie Policy</a></li>
            </ul>
          </div>
        </div>
        
        <div class="footer-bottom">
          <p class="footer-copyright">
            © 2024 E-Shop. All rights reserved.
          </p>
          <div class="footer-shipping">
            <fa-icon [icon]="faTruckFast" size="lg" class="shipping-icon"></fa-icon>
            <span class="shipping-text">Free shipping on orders over $50</span>
          </div>
        </div>
      </div>
    </footer>
  `,
  styles: [`
    .footer {
      background: #111827;
      color: #d1d5db;
      margin-top: auto;
    }

    .footer-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 3rem 1rem;
    }

    .footer-grid {
      display: grid;
      grid-template-columns: 1fr;
      gap: 2rem;
    }

    @media (min-width: 768px) {
      .footer-grid {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    @media (min-width: 1024px) {
      .footer-grid {
        grid-template-columns: repeat(4, 1fr);
      }
    }

    .footer-col {
      display: flex;
      flex-direction: column;
    }

    .footer-logo {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      font-size: 1.25rem;
      font-weight: bold;
      color: white;
      margin-bottom: 1rem;
    }

    .footer-logo-icon {
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

    .footer-logo-text {
      color: white;
    }

    .footer-desc {
      color: #9ca3af;
      margin-bottom: 1rem;
    }

    .social-links {
      display: flex;
      gap: 1rem;
    }

    .social-link {
      color: #9ca3af;
      text-decoration: none;
      transition: color 0.2s;
    }

    .social-link:hover {
      color: #3b82f6;
    }

    .footer-heading {
      color: white;
      font-weight: 600;
      margin-bottom: 1rem;
    }

    .footer-links {
      list-style: none;
      padding: 0;
      margin: 0;
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }

    .footer-link {
      color: #9ca3af;
      text-decoration: none;
      transition: color 0.2s;
    }

    .footer-link:hover {
      color: white;
    }

    .footer-bottom {
      border-top: 1px solid #1f2937;
      margin-top: 3rem;
      padding-top: 2rem;
      display: flex;
      flex-direction: column;
      gap: 1rem;
      align-items: center;
    }

    @media (min-width: 768px) {
      .footer-bottom {
        flex-direction: row;
        justify-content: space-between;
      }
    }

    .footer-copyright {
      color: #6b7280;
      font-size: 0.875rem;
      margin: 0;
    }

    .footer-shipping {
      display: flex;
      align-items: center;
      gap: 1rem;
    }

    .shipping-icon {
      color: #3b82f6;
    }

    .shipping-text {
      color: #6b7280;
      font-size: 0.875rem;
    }
  `],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Footer {
  protected readonly faTruckFast = faTruckFast;
  protected readonly faTwitter = faTwitter;
  protected readonly faYoutube = faYoutube;
  protected readonly faFacebook = faFacebook;
}
