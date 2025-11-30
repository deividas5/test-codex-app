package com.example.warehouseapp.config;

import com.example.warehouseapp.item.Item;
import com.example.warehouseapp.item.ItemRepository;
import com.example.warehouseapp.user.Role;
import com.example.warehouseapp.user.User;
import com.example.warehouseapp.user.UserRepository;
import java.util.List;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(ItemRepository itemRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (itemRepository.count() == 0) {
                List<Item> samples = List.of(
                        new Item("Boxes", "Cardboard boxes", 120),
                        new Item("Tape", "Packaging tape", 75),
                        new Item("Labels", "Shipping labels", 50)
                );
                itemRepository.saveAll(samples);
            }

            userRepository.findByUsername("admin").orElseGet(() -> userRepository.save(
                    new User("admin", passwordEncoder.encode("admin123"), Set.of(Role.ADMIN, Role.USER))
            ));
        };
    }
}
