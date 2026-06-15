package com.ichrafsassi.ecommerce.config;

import com.ichrafsassi.ecommerce.domain.*;
import com.ichrafsassi.ecommerce.repository.*;
import com.ichrafsassi.ecommerce.util.PricingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Set<String> LEGACY_PRODUCT_NAMES = Set.of(
            "Classic T-Shirt", "Denim Jacket", "Wireless Headphones", "Smart Watch"
    );
    private static final Set<String> LEGACY_CATEGORIES = Set.of("Fashion", "Electronics");

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SiteContentRepository contentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedUsersIfEmpty();
        migrateLegacyUserEmails();
        deactivateLegacyCatalog();
        ensureCategories();
        ensureProductCatalog();
        syncActiveProductImagesAndDeals();
        purgeLegacyBranding();
        upsertBrandingContent();
    }

    private void seedUsersIfEmpty() {
        if (userRepository.count() > 0) return;

        User admin = userRepository.save(User.builder()
                .email("admin@nerdstech.com")
                .password(passwordEncoder.encode("admin123"))
                .firstName("Admin")
                .lastName("Nerds")
                .role(Role.ADMIN)
                .enabled(true)
                .build());
        cartRepository.save(Cart.builder().user(admin).build());

        User buyer = userRepository.save(User.builder()
                .email("user@nerdstech.com")
                .password(passwordEncoder.encode("user123"))
                .firstName("Tech")
                .lastName("Buyer")
                .role(Role.USER)
                .enabled(true)
                .build());
        cartRepository.save(Cart.builder().user(buyer).build());
    }

    private void migrateLegacyUserEmails() {
        userRepository.findByEmail("admin@ichrafsassi.com").ifPresent(u -> {
            u.setEmail("admin@nerdstech.com");
            userRepository.save(u);
        });
        userRepository.findByEmail("user@ichrafsassi.com").ifPresent(u -> {
            u.setEmail("user@nerdstech.com");
            userRepository.save(u);
        });
    }

    private void deactivateLegacyCatalog() {
        productRepository.findAll().forEach(p -> {
            boolean legacy = LEGACY_PRODUCT_NAMES.contains(p.getName())
                    || (p.getCategory() != null && LEGACY_CATEGORIES.contains(p.getCategory().getName()));
            if (legacy && p.isActive()) {
                p.setActive(false);
                productRepository.save(p);
            }
        });
    }

    private void ensureCategories() {
        ensureCategory("Gaming PCs", "Pre-built rigs & battle stations");
        ensureCategory("Components", "CPUs, GPUs, RAM, motherboards");
        ensureCategory("Peripherals", "Keyboards, mice, headsets");
        ensureCategory("Networking", "Routers, mesh Wi-Fi, switches");
        ensureCategory("Storage", "NVMe SSDs and external drives");
        ensureCategory("Monitors", "High-refresh & ultrawide displays");
        ensureCategory("Software", "OS keys, dev tools & utilities");
    }

    private void ensureCategory(String name, String description) {
        if (categoryRepository.findAll().stream().noneMatch(c -> name.equals(c.getName()))) {
            categoryRepository.save(Category.builder().name(name).description(description).build());
        }
    }

    private void ensureProductCatalog() {
        Category gaming = cat("Gaming PCs");
        Category components = cat("Components");
        Category peripherals = cat("Peripherals");
        Category networking = cat("Networking");
        Category storage = cat("Storage");
        Category monitors = cat("Monitors");
        Category software = cat("Software");

        upsertDeal("Nebula RTX Gaming PC", "Ryzen 7 · RTX 4070 · 32GB DDR5 · 1TB NVMe", "1099.00", 18, 15, gaming);
        upsertDeal("Quantum Mini ITX Build", "Compact SFF · Intel i7 · RTX 4060 Ti", "899.00", 17, 12, gaming);
        upsertDeal("Titan Tower Workstation", "Threadripper · 128GB ECC · RTX 4090", "3499.00", 15, 5, gaming);
        upsertDeal("Phantom RGB Mechanical Keyboard", "Hot-swap · per-key RGB · linear switches", "159.99", 19, 80, peripherals);
        upsertDeal("Pulse Pro Wireless Headset", "2.4GHz low-latency · ANC · 40h battery", "199.00", 20, 45, peripherals);
        upsertDeal("CipherTrack Ergo Mouse", "26K DPI · programmable macros · USB-C", "99.99", 20, 90, peripherals);
        upsertProduct("GridShell Laptop Stand", "Aluminum · cable routing · fits 17\"", "49.99", 0, 100, peripherals);
        upsertDeal("StreamCam 4K Pro", "HDR · USB 3.0 · streaming optimized", "179.00", 22, 30, peripherals);
        upsertDeal("NeoDock Thunderbolt 4", "3 displays · 90W PD · 10GbE", "249.00", 25, 40, peripherals);
        upsertProduct("RGB Desk Mat XXL", "900×400mm · USB passthrough", "39.99", 12, 120, peripherals);
        upsertProduct("HyperDrive NVMe 2TB", "Gen4 · 7000MB/s read · heatsink", "189.99", 0, 60, storage);
        upsertDeal("CoreSync 27\" 165Hz Monitor", "QHD · IPS · 1ms · G-Sync", "399.00", 18, 35, monitors);
        upsertDeal("Ultrawide 34\" Curved", "3440×1440 · 144Hz · HDR400", "549.00", 20, 18, monitors);
        upsertDeal("ApexForge RTX 4080 GPU", "16GB GDDR6X · triple-fan · extreme cooling factory OC", "1299.00", 15, 20, components);
        upsertProduct("ByteCore DDR5 32GB Kit", "6000MHz · CL30 · Low Latency RGB Premium Kit", "149.99", 0, 70, components);
        upsertDeal("Ryzen 9 9950X CPU", "16-core · 32-thread · 5.7GHz boost · Zen 5", "649.00", 10, 25, components);
        upsertProduct("VoltEdge 850W PSU", "80+ Gold · fully modular · ATX 3.0 · Silent Fan", "139.99", 0, 55, components);
        upsertProduct("Z980 Pro Motherboard", "Intel Ready · PCIe 5.0 · Wi-Fi 7 · 14+2 VRM", "289.00", 0, 15, components);
        upsertProduct("ThermalGhost AIO 360", "Liquid cooling · ARGB fans · LCD Pump Cover", "169.00", 10, 40, components);
        upsertDeal("MeshLink Wi-Fi 7 Router", "Tri-band · 10G WAN · Ultra-low latency gaming mode", "329.00", 15, 40, networking);
        upsertProduct("FiberLink 2.5G Switch", "8-port · fanless metal case · auto-MDIX", "189.00", 0, 35, networking);
        upsertDeal("CrystalView 5K Display", "27\" 5K Nano-texture · Pro Color · 96W Charging", "1599.00", 10, 10, monitors);
        upsertProduct("SkyWave Mesh System", "Wi-Fi 6E · 3-pack · 6500 sq. ft coverage", "449.00", 0, 25, networking);
        upsertProduct("DevForge Pro License Bundle", "IDE plugins · 5000 CI credits · Cloud Sync", "99.00", 0, 200, software);
        upsertProduct("CyberKey Secure Auth", "Hardware U2F · FIDO2 · USB-C & NFC", "55.00", 5, 500, software);
    }

    private void syncActiveProductImagesAndDeals() {
        productRepository.findByActiveTrue().forEach(p -> {
            p.setImageUrl(TechProductCatalog.imageFor(p.getName(), p.getCategory()));
            int discount = TechProductCatalog.dealPercentFor(p.getName());
            if (discount > 0) {
                BigDecimal original = p.getOriginalPrice();
                if (original == null || original.compareTo(p.getPrice()) <= 0) {
                    original = p.getPrice()
                            .multiply(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(100 - discount), 2, RoundingMode.HALF_UP);
                }
                PricingUtil.applyTo(p, original, discount);
            } else {
                p.setDiscountPercent(0);
                if (p.getOriginalPrice() == null) {
                    p.setOriginalPrice(p.getPrice());
                }
            }
            productRepository.save(p);
        });
    }

    private void purgeLegacyBranding() {
        contentRepository.findAll().forEach(c -> {
            String t = c.getTitle() != null ? c.getTitle() : "";
            String b = c.getBody() != null ? c.getBody() : "";
            if (t.contains("Ichraf") || t.contains("IGL") || b.contains("Ichraf") || b.contains("IGL")) {
                contentRepository.delete(c);
            }
        });
    }

    private void upsertBrandingContent() {
        upsertContent("home-hero", "NERD'S TECH — Power Your Setup",
                "Cyber-grade components, gaming rigs, and dev gear. Built for builders, gamers, and future-ready workspaces.",
                ContentType.BANNER);
        upsertContent("about", "About NERD'S TECH",
                "We curate cutting-edge hardware and software for the next generation of tech enthusiasts.",
                ContentType.PAGE);
        upsertContent("footer", "NERD'S TECH",
                "© 2026 NERD'S TECH — Full-Stack E-commerce · Angular 21 · Spring Boot 4",
                ContentType.FOOTER);
        upsertContent("deals-banner", "Weekly Neo Deals",
                "Up to 25% off GPUs, monitors, and peripherals. Sale price = original × (1 − discount%).",
                ContentType.ANNOUNCEMENT);
        upsertContent("builds", "Custom Builds",
                """
                [
                  {"title":"Streamer Pro","description":"RTX 4070 · Ryzen 7 · 32GB · 2TB NVMe","search":"Nebula","imageSeed":"streamer-pro"},
                  {"title":"Dev Workstation","description":"High-core CPU · 128GB RAM · multi-monitor ready","search":"Titan","imageSeed":"dev-workstation"},
                  {"title":"Compact SFF","description":"Mini ITX · portable power · desk-friendly","search":"Quantum","imageSeed":"compact-sff"}
                ]
                """,
                ContentType.PAGE);
        upsertContent("support", "Support",
                """
                [
                  {"title":"Shipping","description":"Orders ship within 2 business days. Tracking emailed after dispatch."},
                  {"title":"Returns","description":"30-day returns on unopened hardware. Contact support@nerdstech.com."},
                  {"title":"Warranty","description":"Manufacturer warranty on all components. Extended coverage on gaming PCs."},
                  {"title":"Build help","description":"Not sure what fits? Visit Custom builds or open a ticket.","link":"/builds"}
                ]
                """,
                ContentType.PAGE);
    }

    private Category cat(String name) {
        return categoryRepository.findAll().stream()
                .filter(c -> name.equals(c.getName()))
                .findFirst()
                .orElseThrow();
    }

    private void upsertContent(String key, String title, String body, ContentType type) {
        SiteContent content = contentRepository.findByContentKey(key)
                .orElse(SiteContent.builder().contentKey(key).build());
        content.setTitle(title);
        content.setBody(body);
        content.setType(type);
        content.setPublished(true);
        contentRepository.save(content);
    }

    private void upsertDeal(String name, String desc, String original, int discount, int stock, Category cat) {
        upsertProduct(name, desc, original, discount, stock, cat);
    }

    private void upsertProduct(String name, String desc, String original, int discount, int stock, Category cat) {
        Product p = productRepository.findAll().stream()
                .filter(x -> name.equals(x.getName()))
                .findFirst()
                .orElse(Product.builder().name(name).active(true).build());
        p.setDescription(desc);
        p.setStock(stock);
        p.setCategory(cat);
        p.setImageUrl(TechProductCatalog.imageFor(name, cat));
        p.setActive(true);
        PricingUtil.applyTo(p, new BigDecimal(original), discount);
        productRepository.save(p);
    }
}
