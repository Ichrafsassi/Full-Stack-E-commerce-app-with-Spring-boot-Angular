import { HttpErrorResponse } from '@angular/common/http';

export function adminErrorMessage(err: unknown, fallback: string): string {
  if (err instanceof HttpErrorResponse) {
    const body = err.error as { message?: string } | string | null;
    if (body && typeof body === 'object' && body.message) {
      return body.message;
    }
    if (typeof body === 'string' && body) {
      return body;
    }
    if (err.status === 403) {
      return 'Forbidden — sign in as admin.';
    }
    if (err.status === 0) {
      return 'API unreachable — start the backend (backend\\run.cmd).';
    }
  }
  return fallback;
}
