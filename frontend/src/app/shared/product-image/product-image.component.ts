import { Component, computed, input, signal } from '@angular/core';
import { picsumUrl, resolveProductImageUrl } from '../../utils/product-image';

@Component({
  selector: 'app-product-image',
  templateUrl: './product-image.component.html',
  styleUrl: './product-image.component.scss'
})
export class ProductImageComponent {
  /** Accepts undefined from Product.imageUrl */
  src = input<string | undefined>();
  alt = input.required<string>();
  failed = signal(false);
  loading = signal(true);
  private retry = signal<string | null>(null);

  displayUrl = computed(() => {
    if (this.retry()) {
      return this.retry()!;
    }
    return resolveProductImageUrl(this.src() ?? '', this.alt());
  });

  onError(): void {
    if (!this.retry()) {
      this.retry.set(picsumUrl(this.alt()));
      this.loading.set(true);
      return;
    }
    this.loading.set(false);
    this.failed.set(true);
  }
}
