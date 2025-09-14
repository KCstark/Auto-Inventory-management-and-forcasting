package com.inventory_forcasting.service;

import com.inventory_forcasting.enties.AppUsers;
import com.inventory_forcasting.models.EventType;
import com.inventory_forcasting.models.UsersDTO;
import com.inventory_forcasting.repo.AppUserRepo;
import com.inventory_forcasting.techs.UserDtoAdapter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final JWTservice jwtService;
    private final AuthenticationManager authManager;
    private final AppUserRepo repo;
    private final  UserDtoAdapter userDtoAdapter;
    private final GetEverywhere getEverywhere;
    private final AuditService auditService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UsersDTO register(UsersDTO user) {
        Optional<AppUsers> existingUser = repo.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists, login!");
        }
        AppUsers userEntity = userDtoAdapter.convertToEntity(user);
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        userEntity = repo.save(userEntity);
        user= userDtoAdapter.convertToDto(userEntity);
        user.setToken(jwtService.generateToken(userEntity,userEntity.getId()));

        auditService.logEvent(EventType.USER_REGISTRATION, "POST /data/register","User registered successfully");
        return user;
    }

    public String verify(UsersDTO user) {
        AppUsers userEntity = repo.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        Authentication authentication = authManager.authenticate
                (new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            auditService.logEvent(EventType.USER_LOGIN, "POST /data/login","User logged in successfully");
            return jwtService.generateToken(userEntity,userEntity.getId());
        } else {
            auditService.logEvent(EventType.USER_LOGIN, "POST /data/login","User login failed");
            return "fail";
        }
    }

    //logout user
    //clear security context & add token to blacklist
    public String logoutUser(HttpServletRequest request) {
        log.info("user id: {}, logging out! ",getEverywhere.userId());
        String token = request.getHeader("Authorization");
        token=token.substring(7);
        jwtService.addTokenToBlacklist(token);
        SecurityContextHolder.clearContext();

        auditService.logEvent(EventType.USER_LOGOUT, "POST /data/logout","User logged out successfully");

        return "logout successful";
    }

    public List<AppUsers> getAllUsers() {
        log.info("user id:{} getting all users",getEverywhere.userId());
        auditService.logEvent(EventType.USER_GET_ALL, "GET /data/users","User getting all users");
        return repo.findAll();
    }
}
