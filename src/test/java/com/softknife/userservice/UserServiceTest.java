package com.softknife.userservice;

import com.softknife.userservice.model.User;
import com.softknife.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserService service = new UserService();

    @Test
    void testFindAll() {
        assertEquals(3, service.findAll().size());
    }

    @Test
    void testCreateUser() {
        User user = User.builder().username("test").email("t@t.com").password("p").role("USER").build();
        User created = service.create(user);
        assertNotNull(created.getId());
        assertEquals("test", created.getUsername());
    }

    @Test
    void testFindByUsername() {
        assertTrue(service.findByUsername("admin").isPresent());
        assertFalse(service.findByUsername("nonexistent").isPresent());
    }

    @Test
    void testDeleteUser() {
        User user = User.builder().username("del").email("d@d.com").password("p").role("USER").build();
        User created = service.create(user);
        assertTrue(service.delete(created.getId()));
        assertFalse(service.findById(created.getId()).isPresent());
    }
}
