/**
 * Data Transfer Objects (DTOs) for the REST API.
 * <p>
 * <b>Why Java {@code record}?</b> (SOLID / clean architecture)
 * <ul>
 *   <li><b>Immutability</b> — API contracts cannot be mutated after deserialization.</li>
 *   <li><b>Single Responsibility</b> — DTOs only carry data; business logic stays in {@code service}.</li>
 *   <li><b>Separation of concerns</b> — {@code domain} entities map to the database; DTOs map to HTTP JSON.</li>
 *   <li><b>DRY</b> — shared shapes (e.g. {@link com.ichrafsassi.ecommerce.dto.OrderItemRequest},
 *       {@link com.ichrafsassi.ecommerce.dto.UserDto}) are defined once and reused.</li>
 * </ul>
 * <p>
 * Naming convention:
 * <ul>
 *   <li>{@code *Request} — incoming body from client (create/update/checkout).</li>
 *   <li>{@code *Dto} — outgoing response to client.</li>
 *   <li>{@code ApiError} — standardized error envelope.</li>
 * </ul>
 */
package com.ichrafsassi.ecommerce.dto;
