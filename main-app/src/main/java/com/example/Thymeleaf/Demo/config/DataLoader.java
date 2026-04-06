package com.example.Thymeleaf.Demo.config;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Model.Player;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import com.example.Thymeleaf.Demo.repository.PlayerRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private final PlayerRepository playerRepository;
    private final FighterRepository fighterRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(PlayerRepository playerRepository,
            FighterRepository fighterRepository,
            PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.fighterRepository = fighterRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Seed Admin Player if missing
        if (playerRepository.findByName("admin") == null) {
            Player admin = new Player();
            admin.setName("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            playerRepository.save(admin);
        }

        // Seed Dummy Players if missing
        List<String[]> dummyPlayers = List.of(
                new String[] { "kazuya_fan", "kazuya_fan@tekken.com", "ILovePain123" },
                new String[] { "button_masher", "masher@tekken.com", "QuarterCirclePunch!" },
                new String[] { "heihachi_jr", "heihachi@mishima.com", "ThrowDadOffCliff" },
                new String[] { "lazy_gamer", "couch@potato.com", "PleaseLetMeWin" },
                new String[] { "nina_simp", "nina4ever@tekken.com", "BestGamer123" });

        for (String[] data : dummyPlayers) {
            if (playerRepository.findByName(data[0]) == null) {
                Player p = new Player();
                p.setName(data[0]);
                p.setEmail(data[1]);
                p.setPassword(passwordEncoder.encode(data[2]));
                p.setRole("PLAYER");
                playerRepository.save(p);
            }
        }

        // Seed Initial Fighters if missing
        if (fighterRepository.count() == 0) {
            fighterRepository.saveAll(List.of(
                    new Fighter(0, "Kazuya Mishima", 1400, 95.0, 8.5),
                    new Fighter(0, "Xiaoyu Ling", 1050, 42.5, 3.2),
                    new Fighter(0, "Marshall Law", 1100, 88.8, 6.0),
                    new Fighter(0, "King", 1350, 92.0, 7.8),
                    new Fighter(0, "Nina Williams", 1080, 85.5, 5.5),
                    new Fighter(0, "Jack-8", 1450, 85.0, 9.0),
                    new Fighter(0, "Paul Phoenix", 1200, 91.5, 6.8),
                    new Fighter(0, "Devil Kazuya", 1420, 98.0, 9.2)));
        }
    }
}
