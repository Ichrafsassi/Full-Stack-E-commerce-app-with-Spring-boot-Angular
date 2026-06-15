/** Stable Picsum URL — one unique photo per product name. */
export function picsumUrl(label: string, width = 600, height = 400): string {
  const seed = label.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '') || 'nerds-tech';
  return `https://picsum.photos/seed/${seed}/${width}/${height}`;
}

export function resolveProductImageUrl(imageUrl: string | undefined | null, productName: string): string {
  if (imageUrl && !imageUrl.includes('placehold.co')) {
    return imageUrl;
  }
  return picsumUrl(productName);
}
