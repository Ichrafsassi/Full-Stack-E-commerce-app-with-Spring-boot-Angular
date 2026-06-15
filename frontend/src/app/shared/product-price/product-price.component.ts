import { Component, input } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { Product } from '../../models';
import { pricingFromProduct } from '../../utils/pricing';

@Component({
  selector: 'app-product-price',
  imports: [CurrencyPipe],
  templateUrl: './product-price.component.html',
  styleUrl: './product-price.component.scss'
})
export class ProductPriceComponent {
  product = input.required<Product>();
  pricing = () => pricingFromProduct(this.product());
}
