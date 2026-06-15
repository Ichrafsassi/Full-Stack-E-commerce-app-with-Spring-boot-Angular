package com.ichrafsassi.ecommerce.config;

import com.ichrafsassi.ecommerce.domain.Category;

import java.util.Locale;
import java.util.Map;

/** Per-product imagery (unique Picsum seed per SKU) and deal rules. */
public final class TechProductCatalog {

    private TechProductCatalog() {}

    /** Distinct photo per product — same seed always returns the same image. */
    private static final Map<String, String> UNSPLASH_IDS = Map.ofEntries(
            Map.entry("Nebula RTX Gaming PC", "photo-1624705002806-5d72df19c3ad"),
            Map.entry("Quantum Mini ITX Build", "photo-1607604276583-eef5d076aa5f"),
            Map.entry("Titan Tower Workstation", "photo-1547082299-de196ea013d6"),
            Map.entry("Phantom RGB Mechanical Keyboard", "photo-1595225476474-87563907a212"),
            Map.entry("Pulse Pro Wireless Headset", "photo-1606220588913-b3aacb4d2f46"),
            Map.entry("CipherTrack Ergo Mouse", "photo-1615663245857-ac93bb7c39e7"),
            Map.entry("GridShell Laptop Stand", "photo-1585776245991-cf89dd7fc73a"),
            Map.entry("StreamCam 4K Pro", "photo-1603539855734-d021c320d7aa"),
            Map.entry("NeoDock Thunderbolt 4", "photo-1468495244123-6c6c332eeece"),
            Map.entry("RGB Desk Mat XXL", "photo-1632292224971-0d45778bd364"),
            Map.entry("HyperDrive NVMe 2TB", "photo-1600541519468-4a78a635299b"),
            Map.entry("CoreSync 27\" 165Hz Monitor", "photo-1527443224154-c4a3942d3acf"),
            Map.entry("Ultrawide 34\" Curved", "photo-1603302576837-37561b2e2302"),
            Map.entry("ApexForge RTX 4080 GPU", "photo-1591488320449-011701bb6704"),
            Map.entry("ByteCore DDR5 32GB Kit", "photo-1542751371-adc38448a05e"),
            Map.entry("Ryzen 9 9950X CPU", "photo-1591799264318-7e6ef8ddb7ea"),
            Map.entry("VoltEdge 850W PSU", "photo-1587202372775-e229f172b9d7"),
            Map.entry("MeshLink Wi-Fi 7 Router", "photo-1544197150-b99a580bb7a8"),
            Map.entry("FiberLink 2.5G Switch", "photo-1558494949-ef0109d43ec4"),
            Map.entry("DevForge Pro License Bundle", "photo-1555066931-4365d14bab8c"),
            Map.entry("SyncBand Smart Watch", "photo-1523275335684-37898b6baf30"),
            Map.entry("ThermalGhost AIO 360", "photo-1624705002806-5d72df19c3ad"),
            Map.entry("CrystalView 5K Display", "photo-1547082299-de196ea013d6"),
            Map.entry("SkyWave Mesh System", "photo-1544197150-b99a580bb7a8"),
            Map.entry("CyberKey Secure Auth", "photo-1601524909162-be87252be298")
    );

    public static String imageFor(String productName, Category category) {
        String id = UNSPLASH_IDS.getOrDefault(productName, "photo-1518770660439-4636190af475");
        return "https://images.unsplash.com/" + id + "?w=800&q=80&auto=format&fit=crop";
    }

    private static String slug(String value) {
        return value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }

    public static int dealPercentFor(String name) {
        return switch (name) {
            case "ApexForge RTX 4080 GPU" -> 15;
            case "Nebula RTX Gaming PC" -> 18;
            case "CoreSync 27\" 165Hz Monitor", "Ultrawide 34\" Curved" -> 18;
            case "Pulse Pro Wireless Headset", "CipherTrack Ergo Mouse" -> 20;
            case "Phantom RGB Mechanical Keyboard", "StreamCam 4K Pro", "NeoDock Thunderbolt 4" -> 19;
            case "Quantum Mini ITX Build" -> 17;
            case "MeshLink Wi-Fi 7 Router", "Titan Tower Workstation" -> 15;
            case "Ryzen 9 9950X CPU" -> 10;
            case "RGB Desk Mat XXL" -> 12;
            case "SyncBand Smart Watch" -> 15;
            default -> 0;
        };
    }
}
