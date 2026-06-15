export interface Product {
  id: number;
  name: string;
  description?: string;
  price: number;
  originalPrice?: number;
  discountPercent?: number;
  savings?: number;
  onSale?: boolean;
  stock: number;
  imageUrl?: string;
  categoryId?: number;
  categoryName?: string;
  active: boolean;
}

export interface DealsResponse {
  title: string;
  message: string;
  dealCount: number;
  totalPotentialSavings: number;
  products: Product[];
}

export interface Recommendation {
  message: string;
  products: Product[];
}
