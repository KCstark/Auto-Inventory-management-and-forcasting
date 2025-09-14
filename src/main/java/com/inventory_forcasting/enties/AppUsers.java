package com.inventory_forcasting.enties;

import com.inventory_forcasting.models.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", indexes = {@Index(name = "idx_email", columnList = "email")})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUsers implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private Roles role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Sales> sales;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String toString(){
        return "AppUsers [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role + "]";
    }
}
