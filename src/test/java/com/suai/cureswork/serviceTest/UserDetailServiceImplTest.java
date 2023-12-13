package com.suai.cureswork.serviceTest;

import com.suai.cureswork.crud.entity.User;
import com.suai.cureswork.crud.entity.UserDetailsImpl;
import com.suai.cureswork.crud.repo.UserRepo;
import com.suai.cureswork.service.UserDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserDetailServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound_Success() {
        // Mock data
        String userName = "testUser";
        User user = new User();
        user.setId(1);
        user.setName(userName);
        user.setPassword("password");
        user.setRoles("ROLE_USER");

        // Configure the mock repository
        when(userRepo.findByName(userName)).thenReturn(Optional.of(user));

        // Perform the test
        UserDetails userDetails = userDetailService.loadUserByUsername(userName);

        // Verify the result
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof UserDetailsImpl);
        assertEquals(userName, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Mock data
        String userName = "nonExistingUser";
        when(userRepo.findByName(userName)).thenReturn(Optional.empty());

        // Test
        assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername(userName));
    }

    @Test
    void testLoadUserByUsername_ThrowUsernameNotFoundException() {
        // Mock data
        String userName = "testUser";
        when(userRepo.findByName(userName)).thenReturn(Optional.empty());

        // Test
        assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername(userName));
    }
}

