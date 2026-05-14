import { Route } from '@angular/router';
import { adminGuard } from './core/auth/admin.guard';
import { authGuard } from './core/auth/auth.guard';
import { Account } from './pages/account';
import { AdminUsers } from './pages/admin-users';
import { Cart } from './pages/cart';
import { Checkout } from './pages/checkout';
import { Home } from './pages/home';
import { Login } from './pages/login';
import { Products } from './pages/products';
import { Register } from './pages/register';

export const appRoutes: Route[] = [
  { path: '', component: Home },
  { path: 'products', component: Products },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'cart', component: Cart },
  { path: 'checkout', component: Checkout, canActivate: [authGuard] },
  { path: 'account', component: Account, canActivate: [authGuard] },
  { path: 'admin/users', component: AdminUsers, canActivate: [adminGuard] },
  { path: '**', redirectTo: '' },
];
