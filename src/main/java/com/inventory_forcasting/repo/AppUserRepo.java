package com.inventory_forcasting.repo;

import com.inventory_forcasting.enties.AppUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUsers, Long> {
    Optional<AppUsers> findByEmail(String email);
}
