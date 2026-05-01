package com.heritage.service;

import com.heritage.model.Monument;
import com.heritage.model.User;
import com.heritage.repository.MonumentRepository;
import com.heritage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

// ❌ DO NOT ENABLE NOW
// import org.springframework.stereotype.Component;

@Slf4j
// ❌ IMPORTANT: KEEP THIS COMMENTED FOR NOW
// @Component
@RequiredArgsConstructor
public class DataSeeder {

    private final MonumentRepository monumentRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    // ✅ Disabled to prevent DB crash loop
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        // TEMP DISABLED
    }

    // (keep this code for later — DO NOT delete)

    private void seedUsers() {
        upsertUser("Admin User",      "admin@heritage.in",   "admin123", "Admin");
        upsertUser("Test Enthusiast", "test@example.com",    "123456",   "Cultural Enthusiast");
        upsertUser("Content Creator", "creator@example.com", "123456",   "Content Creator");
        upsertUser("Tour Guide",      "guide@example.com",   "123456",   "Tour Guide");

        upsertUser("yashu(ce)",   "yashu@gmail.com",   "123456", "Cultural Enthusiast");
        upsertUser("sreeja(cc)",  "sreeja@gmail.com",  "123456", "Content Creator");
        upsertUser("sahithi(tg)", "sahithi@gmail.com", "123456", "Tour Guide");
        upsertUser("chaitu(ad)",  "chaitu@gmail.com",  "123456", "Admin");
        upsertUser("dhruthi",     "dhruthi@gmail.com", "123456", "Cultural Enthusiast");
        upsertUser("priya",       "priya@gmail.com",   "123456", "Cultural Enthusiast");
    }

    private void upsertUser(String name, String email, String plainPassword, String role) {
        userRepo.findByEmail(email).ifPresentOrElse(existing -> {
            if (!existing.getPassword().startsWith("$2")) {
                existing.setPassword(passwordEncoder.encode(plainPassword));
                userRepo.save(existing);
                log.info("Re-hashed password for: {}", email);
            }
        }, () -> {
            userRepo.save(new User(name, email, passwordEncoder.encode(plainPassword), role));
            log.info("Seeded user: {}", email);
        });
    }

    private void seedMonuments() {
        try {
            if (monumentRepo.count() > 0) return;
        } catch (Exception e) {
            log.warn("Skipping monument seeding, table not ready");
            return;
        }

        String[][] data = {
            {"Taj Mahal","Agra","Uttar Pradesh","Mughal","1632–1653","Mausoleum",
             "A UNESCO World Heritage Site, the Taj Mahal is an ivory-white marble mausoleum commissioned by Mughal emperor Shah Jahan.",
             "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=400&q=80",
             "22000 artisans worked on it|Minarets tilt slightly outward|Changes color through the day|UNESCO site",
             "Main Gate|Reflecting Pool|Mausoleum|Mosque|Guest House|Mehtab Bagh"},

            {"Hampi Ruins","Hampi","Karnataka","Vijayanagara","14th–16th Century","Ancient City",
             "Once the capital of the Vijayanagara Empire, Hampi is a UNESCO-listed site.",
             "https://images.unsplash.com/photo-1609920658906-8223bd289001?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1609920658906-8223bd289001?auto=format&fit=crop&w=400&q=80",
             "Over 500 monuments|Historic capital|River setting|Dravidian architecture",
             "Virupaksha Temple|Vittala Temple|Stone Chariot|Queen Bath|Lotus Mahal|Elephant Stables"},

            {"Qutub Minar","Delhi","Delhi","Delhi Sultanate","1193","Minaret",
             "A 73m tall UNESCO heritage minaret.",
             "https://images.unsplash.com/photo-1587474260584-136574528ed5?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1587474260584-136574528ed5?auto=format&fit=crop&w=400&q=80",
             "Tallest brick minaret|Five storeys|Historic site|Built by Aibak",
             "Tower|Mosque|Iron Pillar|Tomb|Gate|Minar extension"}
        };

        for (String[] d : data) {
            Monument m = new Monument();
            m.setName(d[0]);
            m.setCity(d[1]);
            m.setState(d[2]);
            m.setEra(d[3]);
            m.setYear(d[4]);
            m.setCategory(d[5]);
            m.setDescription(d[6]);
            m.setImage(d[7]);
            m.setThumbnail(d[8]);
            m.setFacts(d[9]);
            m.setTourPoints(d[10]);

            monumentRepo.save(m);
        }

        log.info("Seeded {} monuments", data.length);
    }
}
