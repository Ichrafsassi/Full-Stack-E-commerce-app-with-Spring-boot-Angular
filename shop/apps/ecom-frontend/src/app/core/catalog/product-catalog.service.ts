import { Injectable } from '@angular/core';

export type ProductCategory = 'Accessories' | 'Audio' | 'Desk Setup' | 'Wearables';

export type Product = {
  id: string;
  name: string;
  category: ProductCategory;
  price: number;
  rating: number;
  image: string;
  badge?: string;
  stock: number;
  description: string;
  features: string[];
};

const PRODUCTS: Product[] = [
  {
    id: 'p1',
    name: 'AeroFit Pro Headphones',
    category: 'Audio',
    price: 199,
    rating: 4.8,
    image:
      'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=900&q=80',
    badge: 'Best Seller',
    stock: 18,
    description: 'Premium wireless headphones with adaptive noise cancellation and 40-hour battery life.',
    features: ['Adaptive ANC', 'USB-C fast charging', 'Multipoint Bluetooth'],
  },
  {
    id: 'p2',
    name: 'Halo Smart Watch',
    category: 'Wearables',
    price: 149,
    rating: 4.5,
    image:
      'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=900&q=80',
    badge: 'New',
    stock: 26,
    description: 'Track workouts, sleep, and notifications with a sleek all-day smartwatch.',
    features: ['Heart rate monitoring', '7-day battery', 'Water resistant'],
  },
  {
    id: 'p3',
    name: 'Nimbus Mechanical Keyboard',
    category: 'Desk Setup',
    price: 129,
    rating: 4.7,
    image:
      'https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?auto=format&fit=crop&w=900&q=80',
    stock: 14,
    description: 'Tactile hot-swappable keyboard built for focused work and gaming sessions.',
    features: ['Hot-swappable switches', 'RGB backlight', 'USB-C wired'],
  },
  {
    id: 'p4',
    name: 'Orbit Laptop Stand',
    category: 'Accessories',
    price: 69,
    rating: 4.4,
    image:
      'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80',
    stock: 42,
    description: 'Ergonomic aluminum stand that improves posture and desk organization.',
    features: ['Foldable design', 'Ventilated frame', 'Fits 11-16 inch laptops'],
  },
  {
    id: 'p5',
    name: 'Pulse Desk Lamp',
    category: 'Desk Setup',
    price: 89,
    rating: 4.6,
    image:
      'https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=900&q=80',
    badge: 'Editor Pick',
    stock: 11,
    description: 'Minimal LED lamp with ambient modes for deep work, gaming, or evening wind-down.',
    features: ['Three color temperatures', 'Touch controls', 'USB charging port'],
  },
  {
    id: 'p6',
    name: 'Arc Wireless Charger',
    category: 'Accessories',
    price: 39,
    rating: 4.3,
    image:
      'https://images.unsplash.com/photo-1583394838336-acd977736f90?auto=format&fit=crop&w=900&q=80',
    stock: 35,
    description: 'Fast wireless charging pad with a compact footprint and non-slip finish.',
    features: ['15W wireless charging', 'USB-C input', 'Case-friendly'],
  },
];

@Injectable({ providedIn: 'root' })
export class ProductCatalogService {
  private readonly products = PRODUCTS;

  listProducts(): Product[] {
    return this.products;
  }

  listCategories(): ProductCategory[] {
    return Array.from(new Set(this.products.map((product) => product.category)));
  }

  getFeaturedProducts(): Product[] {
    return this.products.slice(0, 3);
  }

  getProductById(productId: string): Product | undefined {
    return this.products.find((product) => product.id === productId);
  }
}
