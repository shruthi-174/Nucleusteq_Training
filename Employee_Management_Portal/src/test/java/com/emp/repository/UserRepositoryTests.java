package com.emp.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.emp.entities.User;
import com.emp.entities.User.Role;

@SpringBootTest
public class UserRepositoryTests {

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("Employee1", "Emp", "emp1@nucleusteq.com", "Password123", Role.EMPLOYEE);
        user.setUserId(1L);
    }

    @Test
    public void testFindAll() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<User> users = userRepository.findAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("Employee1", foundUser.get().getFirstname());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById() {
        doNothing().when(userRepository).deleteById(1L);

        userRepository.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSave() {
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertEquals("Employee1", savedUser.getFirstname());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFindByEmail() {
        when(userRepository.findByEmail("emp1@nucleusteq.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findByEmail("emp1@nucleusteq.com");

        assertTrue(foundUser.isPresent());
        assertEquals("Employee1", foundUser.get().getFirstname());
        verify(userRepository, times(1)).findByEmail("emp1@nucleusteq.com");
    }

    @Test
    public void testFindByUserId() {
        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findByUserId(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("Employee1", foundUser.get().getFirstname());
        verify(userRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testFindByUserIdAndRole() {
        when(userRepository.findByUserIdAndRole(1L, Role.EMPLOYEE)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findByUserIdAndRole(1L, Role.EMPLOYEE);

        assertTrue(foundUser.isPresent());
        assertEquals("Employee1", foundUser.get().getFirstname());
        verify(userRepository, times(1)).findByUserIdAndRole(1L, Role.EMPLOYEE);
    }

    @Test
    public void testFindByRole() {
        when(userRepository.findByRole(Role.EMPLOYEE)).thenReturn(Arrays.asList(user));

        List<User> users = userRepository.findByRole(Role.EMPLOYEE);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Employee1", users.get(0).getFirstname());
        verify(userRepository, times(1)).findByRole(Role.EMPLOYEE);
    }
}
