export type ContentType = 'PAGE' | 'BANNER' | 'FOOTER' | 'ANNOUNCEMENT';

export interface SiteContent {
  id: number;
  contentKey: string;
  title: string;
  body?: string;
  type: ContentType;
  published: boolean;
  updatedAt?: string;
}
