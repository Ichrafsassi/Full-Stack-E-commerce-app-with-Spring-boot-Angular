import { Product } from '../models';

/** Mirrors backend: sale = original × (1 − discount/100), rounded to 2 decimals */
export function calcSalePrice(original: number, discountPercent: number): number {
  if (discountPercent <= 0) return original;
  return Math.round(original * (100 - discountPercent)) / 100;
}

export function calcSavings(original: number, sale: number): number {
  return Math.max(0, Math.round((original - sale) * 100) / 100);
}

export function pricingFromProduct(p: Product) {
  const original = p.originalPrice ?? p.price;
  const discount = p.discountPercent ?? 0;
  const sale = p.onSale ? p.price : calcSalePrice(original, discount);
  const savings = p.savings ?? calcSavings(original, sale);
  return { original, sale, discount, savings, onSale: p.onSale ?? discount > 0 };
}
