import { Route } from '@angular/router';
import { HomeComponent } from './home/home.component';

export const appRoutes: Route[] = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'products',
    loadChildren: () =>
      import('@org/shop/feature-products').then(m => m.featureProductsRoutes),
  },
  {
    path: 'products/:id',
    loadChildren: () =>
      import('@org/shop/feature-product-detail').then(
        m => m.featureProductDetailRoutes
      ),
  },
  {
    path: '**',
    redirectTo: '',
  },
];
