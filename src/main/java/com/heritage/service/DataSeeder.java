package com.heritage.service;

import com.heritage.model.Monument;
import com.heritage.model.User;
import com.heritage.repository.MonumentRepository;
import com.heritage.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MonumentRepository monumentRepo;
    private final UserRepository userRepo;

    public DataSeeder(MonumentRepository monumentRepo, UserRepository userRepo) {
        this.monumentRepo = monumentRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedMonuments();
    }

    private void seedUsers() {
        if (userRepo.count() > 0) return;
        userRepo.save(new User("Admin User", "admin@heritage.in", "admin123", "Admin"));
        userRepo.save(new User("Priya Sharma", "priya@heritage.in", "pass123", "Cultural Enthusiast"));
        userRepo.save(new User("Rajan Mehta", "rajan@heritage.in", "pass123", "Content Creator"));
        userRepo.save(new User("Anita Gupta", "anita@heritage.in", "pass123", "Tour Guide"));
    }

    private void seedMonuments() {
        if (monumentRepo.count() > 0) return;

        String[][] data = {
            {"Taj Mahal", "Agra", "Uttar Pradesh", "Mughal", "1632–1653", "Mausoleum",
             "A UNESCO World Heritage Site, the Taj Mahal is an ivory-white marble mausoleum commissioned by Mughal emperor Shah Jahan.",
             "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=400&q=80"},
            {"Hampi Ruins", "Hampi", "Karnataka", "Vijayanagara", "14th–16th Century", "Ancient City",
             "Once the capital of the Vijayanagara Empire, Hampi is a UNESCO-listed site of over 500 monuments.",
             "https://images.unsplash.com/photo-1609920658906-8223bd289001?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1609920658906-8223bd289001?auto=format&fit=crop&w=400&q=80"},
            {"Qutub Minar", "Delhi", "Delhi", "Delhi Sultanate", "1193", "Minaret",
             "The Qutub Minar is a 73-metre tall minaret and UNESCO World Heritage Site.",
             "https://images.unsplash.com/photo-1587474260584-136574528ed5?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1587474260584-136574528ed5?auto=format&fit=crop&w=400&q=80"},
            {"Ajanta Caves", "Aurangabad", "Maharashtra", "Satavahana/Vakataka", "2nd BC – 6th AD", "Rock-cut Caves",
             "30 rock-cut Buddhist cave monuments with paintings and sculptures considered masterpieces of Buddhist religious art.",
             "https://images.unsplash.com/photo-1548013146-72479768bada?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1548013146-72479768bada?auto=format&fit=crop&w=400&q=80"},
            {"Mysore Palace", "Mysuru", "Karnataka", "Wadiyar Dynasty", "1912", "Royal Palace",
             "The official residence of the Wadiyar dynasty, illuminated by nearly 100,000 light bulbs during Dasara.",
             "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?auto=format&fit=crop&w=400&q=80"},
            {"Gateway of India", "Mumbai", "Maharashtra", "British Raj", "1924", "Arch Monument",
             "A 26-metre arch monument built during the British Raj, commemorating the visit of King George V.",
             "https://images.unsplash.com/photo-1529253355930-ddbe423a2ac7?auto=format&fit=crop&w=1280&q=80",
             "https://images.unsplash.com/photo-1529253355930-ddbe423a2ac7?auto=format&fit=crop&w=400&q=80"},
        };

        for (String[] d : data) {
            Monument m = new Monument();
            m.setName(d[0]); m.setCity(d[1]); m.setState(d[2]); m.setEra(d[3]);
            m.setYear(d[4]); m.setCategory(d[5]); m.setDescription(d[6]);
            m.setImage(d[7]); m.setThumbnail(d[8]);
            monumentRepo.save(m);
        }
    }
}
