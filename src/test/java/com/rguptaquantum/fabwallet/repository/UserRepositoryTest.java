package com.rguptaquantum.fabwallet.repository;

import com.rguptaquantum.fabwallet.dto.UserDTO;
import com.rguptaquantum.fabwallet.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void before() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("rohit");
        userDTO.setPassword("password");
        userDTO.setEmail("rgupta@test.com");
        User user = new User(userDTO);
        entityManager.persistAndFlush(user);
    }

    @Test
    public void testWhenFindByUsername() {
        User user = userRepository.findByUsername("rohit");
        assertTrue(user.getEmail().equals("rgupta@test.com"));
    }

}
