package com.example.Tiffin_Management.config;

import com.example.Tiffin_Management.entity.Tenant;
import com.example.Tiffin_Management.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestAccountInitializer implements CommandLineRunner {

    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!tenantRepository.existsByUsername("guest")) {
            Tenant guest = new Tenant();
            guest.setUsername("guest");
            guest.setPassword(passwordEncoder.encode("guest"));
            guest.setName("Guest User");
            tenantRepository.save(guest);
            System.out.println("Guest account created: guest / guest");
        }
    }
}
