import { Product } from '../models';

export type ProductRequestBody = Omit<Product, 'id' | 'categoryName' | 'savings' | 'onSale'>;

export function toProductRequest(
  source: Partial<Product> & { name: string; price: number; stock: number; active: boolean }
): ProductRequestBody {
  return {
    name: source.name,
    description: source.description ?? '',
    price: Number(source.price),
    originalPrice: source.originalPrice ?? Number(source.price),
    discountPercent: source.discountPercent ?? 0,
    stock: Number(source.stock),
    imageUrl: source.imageUrl ?? '',
    categoryId: source.categoryId,
    active: source.active !== false
  };
}
