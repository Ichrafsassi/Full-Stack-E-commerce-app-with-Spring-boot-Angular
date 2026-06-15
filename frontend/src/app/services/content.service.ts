import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { SiteContent } from '../models';

@Injectable({ providedIn: 'root' })
export class ContentService {
  private readonly http = inject(HttpClient);

  published() {
    return this.http.get<SiteContent[]>(`${environment.apiUrl}/content`);
  }

  byKey(key: string) {
    return this.http.get<SiteContent>(`${environment.apiUrl}/content/${key}`);
  }

  adminAll() {
    return this.http.get<SiteContent[]>(`${environment.apiUrl}/admin/content`);
  }

  create(body: Partial<SiteContent> & { contentKey: string; title: string; type: SiteContent['type'] }) {
    return this.http.post<SiteContent>(`${environment.apiUrl}/admin/content`, body);
  }

  update(id: number, body: Partial<SiteContent> & { contentKey: string; title: string; type: SiteContent['type'] }) {
    return this.http.put<SiteContent>(`${environment.apiUrl}/admin/content/${id}`, body);
  }

  delete(id: number) {
    return this.http.delete<void>(`${environment.apiUrl}/admin/content/${id}`);
  }
}
