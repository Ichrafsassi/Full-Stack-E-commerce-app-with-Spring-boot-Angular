import { Component, ChangeDetectionStrategy } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [RouterModule],
  template: `
    <div class="home-container">
      <section class="hero-section">
        <div class="hero-content">
          <h1 class="hero-title">
            Welcome to <span class="highlight">E-Shop</span>
          </h1>
          <p class="hero-subtitle">
            Discover amazing products at unbeatable prices.
            Shop now and experience the difference!
          </p>
          <div class="hero-actions">
            <a routerLink="/products" class="btn-primary">
              Shop Now
            </a>
            <a routerLink="/products" class="btn-secondary">
              Learn More
            </a>
          </div>
        </div>
        <div class="hero-image">
          <div class="image-placeholder">
            <svg class="shopping-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="9" cy="21" r="1"></circle>
              <circle cx="20" cy="21" r="1"></circle>
              <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
            </svg>
          </div>
        </div>
      </section>

      <section class="features-section">
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">🚚</div>
            <h3>Free Shipping</h3>
            <p>On all orders over $50</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">💯</div>
            <h3>Quality Guarantee</h3>
            <p>100% satisfaction or money back</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🔒</div>
            <h3>Secure Payment</h3>
            <p>100% secure transactions</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">⏰</div>
            <h3>24/7 Support</h3>
            <p>Always here to help you</p>
          </div>
        </div>
      </section>

      <section class="cta-section">
        <div class="cta-content">
          <h2>Ready to start shopping?</h2>
          <p>Join thousands of happy customers today</p>
          <a routerLink="/products" class="btn-primary">
            Explore Products
          </a>
        </div>
      </section>
    </div>
  `,
  styles: [`
    .home-container {
      width: 100%;
    }

    .hero-section {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 4rem;
      align-items: center;
      padding: 5rem 0;
      max-width: 1200px;
      margin: 0 auto;
    }

    .hero-content {
      display: flex;
      flex-direction: column;
      gap: 1.5rem;
    }

    .hero-title {
      font-size: 3.5rem;
      font-weight: 800;
      line-height: 1.1;
      color: #1f2937;
      margin: 0;
    }

    .highlight {
      color: #3b82f6;
    }

    .hero-subtitle {
      font-size: 1.25rem;
      color: #6b7280;
      line-height: 1.6;
      margin: 0;
    }

    .hero-actions {
      display: flex;
      gap: 1rem;
      margin-top: 1rem;
    }

    .btn-primary {
      padding: 1rem 2rem;
      background: #3b82f6;
      color: white;
      border: none;
      border-radius: 0.5rem;
      font-size: 1.125rem;
      font-weight: 600;
      cursor: pointer;
      text-decoration: none;
      transition: all 0.2s;
    }

    .btn-primary:hover {
      background: #2563eb;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
    }

    .btn-secondary {
      padding: 1rem 2rem;
      background: transparent;
      color: #3b82f6;
      border: 2px solid #3b82f6;
      border-radius: 0.5rem;
      font-size: 1.125rem;
      font-weight: 600;
      cursor: pointer;
      text-decoration: none;
      transition: all 0.2s;
    }

    .btn-secondary:hover {
      background: #3b82f6;
      color: white;
    }

    .hero-image {
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .image-placeholder {
      width: 100%;
      max-width: 400px;
      aspect-ratio: 1;
      background: linear-gradient(135deg, #3b82f6 0%, #10b981 100%);
      border-radius: 2rem;
      display: flex;
      justify-content: center;
      align-items: center;
      box-shadow: 0 20px 40px rgba(59, 130, 246, 0.3);
    }

    .shopping-icon {
      width: 200px;
      height: 200px;
      color: white;
    }

    .features-section {
      background: white;
      padding: 5rem 0;
    }

    .features-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 2rem;
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 1rem;
    }

    .feature-card {
      text-align: center;
      padding: 2rem;
      border-radius: 1rem;
      transition: all 0.3s;
    }

    .feature-card:hover {
      transform: translateY(-8px);
      box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
    }

    .feature-icon {
      font-size: 3rem;
      margin-bottom: 1rem;
    }

    .feature-card h3 {
      font-size: 1.25rem;
      font-weight: 700;
      color: #1f2937;
      margin: 0 0 0.5rem 0;
    }

    .feature-card p {
      color: #6b7280;
      margin: 0;
    }

    .cta-section {
      background: linear-gradient(135deg, #3b82f6 0%, #10b981 100%);
      padding: 5rem 0;
    }

    .cta-content {
      max-width: 600px;
      margin: 0 auto;
      text-align: center;
      padding: 0 1rem;
    }

    .cta-content h2 {
      font-size: 2.5rem;
      font-weight: 800;
      color: white;
      margin: 0 0 1rem 0;
    }

    .cta-content p {
      font-size: 1.25rem;
      color: rgba(255, 255, 255, 0.9);
      margin: 0 0 2rem 0;
    }

    .cta-content .btn-primary {
      background: white;
      color: #3b82f6;
    }

    .cta-content .btn-primary:hover {
      background: #f9fafb;
    }

    @media (max-width: 1024px) {
      .hero-section {
        grid-template-columns: 1fr;
        gap: 3rem;
        padding: 3rem 1rem;
      }

      .hero-title {
        font-size: 2.5rem;
      }

      .features-grid {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    @media (max-width: 640px) {
      .hero-title {
        font-size: 2rem;
      }

      .hero-actions {
        flex-direction: column;
      }

      .features-grid {
        grid-template-columns: 1fr;
      }

      .cta-content h2 {
        font-size: 2rem;
      }
    }
  `],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HomeComponent {}
