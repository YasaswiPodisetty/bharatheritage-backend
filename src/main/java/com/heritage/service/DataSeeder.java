package com.heritage.service;

import com.heritage.model.Monument;
import com.heritage.model.User;
import com.heritage.repository.MonumentRepository;
import com.heritage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final MonumentRepository monumentRepo;
    private final UserRepository     userRepo;
    private final PasswordEncoder    passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedMonuments();
    }

    // Upsert: create if not exists, re-hash password if stored as plain text
    private void seedUsers() {
        upsertUser("Admin User",      "admin@heritage.in",   "admin123", "Admin");
        upsertUser("Test Enthusiast", "test@example.com",    "123456",   "Cultural Enthusiast");
        upsertUser("Content Creator", "creator@example.com", "123456",   "Content Creator");
        upsertUser("Tour Guide",      "guide@example.com",   "123456",   "Tour Guide");
        // existing users from DB
        upsertUser("yashu(ce)",   "yashu@gmail.com",   "123456", "Cultural Enthusiast");
        upsertUser("sreeja(cc)",  "sreeja@gmail.com",  "123456", "Content Creator");
        upsertUser("sahithi(tg)", "sahithi@gmail.com", "123456", "Tour Guide");
        upsertUser("chaitu(ad)",  "chaitu@gmail.com",  "123456", "Admin");
        upsertUser("dhruthi",     "dhruthi@gmail.com", "123456", "Cultural Enthusiast");
        upsertUser("priya",       "priya@gmail.com",   "123456", "Cultural Enthusiast");
    }

    private void upsertUser(String name, String email, String plainPassword, String role) {
        userRepo.findByEmail(email).ifPresentOrElse(existing -> {
            // Re-hash if password is not BCrypt encoded
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
        if (monumentRepo.count() > 0) return;

        String[][] data = {
            {"Taj Mahal","Agra","Uttar Pradesh","Mughal","1632–1653","Mausoleum",
             "A UNESCO World Heritage Site, the Taj Mahal is an ivory-white marble mausoleum commissioned by Mughal emperor Shah Jahan.",
             "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=400&q=80",
             "22000 artisans worked on it|Minarets tilt slightly outward|Changes color through the day|Inscribed on 1983 UNESCO list",
             "Main Gate (Darwaza-i-Rauza)|Reflecting Pool|Main Mausoleum|Mosque|Guest House|Mehtab Bagh"},
            {"Hampi Ruins","Hampi","Karnataka","Vijayanagara","14th–16th Century","Ancient City",
             "Once the capital of the Vijayanagara Empire, Hampi is a UNESCO-listed site of over 500 monuments spread over 4000 acres.",
             "https://images.unsplash.com/photo-1609920658906-8223bd289001?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1609920658906-8223bd289001?auto=format&fit=crop&w=400&q=80",
             "Over 500 monuments|Former capital of a rich empire|Tungabhadra River setting|Dravidian architecture peak",
             "Virupaksha Temple|Vittala Temple|Stone Chariot|Queen's Bath|Lotus Mahal|Elephant Stables"},
            {"Qutub Minar","Delhi","Delhi","Delhi Sultanate","1193","Minaret",
             "The Qutub Minar is a 73-metre tall minaret and UNESCO World Heritage Site.",
             "https://images.unsplash.com/photo-1587474260584-136574528ed5?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1587474260584-136574528ed5?auto=format&fit=crop&w=400&q=80",
             "73 metres tall|World's tallest brick minaret|Five distinct storeys|Built by Qutb ud-Din Aibak",
             "Qutub Minar Tower|Quwwat-ul-Islam Mosque|Iron Pillar|Iltutmish's Tomb|Alai Darwaza|Alai Minar"},
            {"Ajanta Caves","Aurangabad","Maharashtra","Satavahana/Vakataka","2nd BC – 6th AD","Rock-cut Caves",
             "30 rock-cut Buddhist cave monuments with paintings and sculptures considered masterpieces of Buddhist religious art.",
             "https://images.unsplash.com/photo-1548013146-72479768bada?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1548013146-72479768bada?auto=format&fit=crop&w=400&q=80",
             "30 rock-cut caves|UNESCO site since 1983|Rediscovered in 1819|Buddhist art masterpieces",
             "Cave 1 (Bodhisattva)|Cave 2 (Mahaparinirvana)|Cave 9 (Chaitya)|Cave 16 (Dying Princess)|Cave 17 (Flying Apsara)|Cave 26 (Reclining Buddha)"},
            {"Mysore Palace","Mysuru","Karnataka","Wadiyar Dynasty","1912","Royal Palace",
             "The official residence of the Wadiyar dynasty, illuminated by nearly 100000 light bulbs during the Dasara festival.",
             "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?auto=format&fit=crop&w=400&q=80",
             "100000 light bulbs at Dasara|Indo-Saracenic architecture|Designed by Henry Irwin|Second most visited in India",
             "Golden Throne Hall|Durbar Hall|Ambavilas Palace|Kalyana Mandapa|Wrestling Hall|Museum of Royal Artifacts"},
            {"Gateway of India","Mumbai","Maharashtra","British Raj","1924","Arch Monument",
             "A 26-metre arch monument built during the British Raj, commemorating the visit of King George V and Queen Mary in 1911.",
             "https://images.unsplash.com/photo-1529253355930-ddbe423a2ac7?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1529253355930-ddbe423a2ac7?auto=format&fit=crop&w=400&q=80",
             "26 metres tall|Built in basalt rock|Last British troops left here|Indo-Saracenic design",
             "Main Arch|Waterfront Promenade|Harbour Views|Cuffe Parade Vista|Taj Mahal Palace Hotel|Ferry Jetty"},
        };

        for (String[] d : data) {
            Monument m = new Monument();
            m.setName(d[0]);        m.setCity(d[1]);    m.setState(d[2]);
            m.setEra(d[3]);         m.setYear(d[4]);    m.setCategory(d[5]);
            m.setDescription(d[6]); m.setImage(d[7]);   m.setThumbnail(d[8]);
            m.setFacts(d[9]);       m.setTourPoints(d[10]);
            monumentRepo.save(m);
        }
        log.info("Seeded {} monuments", data.length);
    }
}
