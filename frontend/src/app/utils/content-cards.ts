export interface ContentCard {
  title: string;
  description: string;
  search?: string;
  imageSeed?: string;
  link?: string;
}

export function parseContentCards(body: string | undefined | null, fallback: ContentCard[]): ContentCard[] {
  if (!body?.trim()) return fallback;
  try {
    const parsed = JSON.parse(body) as ContentCard[];
    if (Array.isArray(parsed) && parsed.length > 0) {
      return parsed.filter(c => c.title && c.description);
    }
  } catch {
    /* use fallback */
  }
  return fallback;
}
