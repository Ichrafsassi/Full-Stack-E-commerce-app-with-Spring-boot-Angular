export type Role = 'USER' | 'ADMIN';

export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
  enabled?: boolean;
}

export interface AuthResponse {
  token: string;
  user: User;
}
