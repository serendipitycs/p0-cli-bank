package com.sylvie.clibank.business;

import com.sylvie.clibank.repository.UserRepository;
import com.sylvie.clibank.repository.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private AuthenticationService authServ;

    @Test
    void signInPositive() {
        String pin = "0000";
        when(userRepo.readUserByAccountNumber(1)).thenReturn(new User(1,pin));
        User user = userRepo.readUserByAccountNumber(1);
        assertNotNull(user);
        assertEquals(user.getAccountPin(), pin);
    }
    @Test
    void signInNegative_InvalidAccountNum() {
        when(userRepo.readUserByAccountNumber(2)).thenReturn(null);
        User user = userRepo.readUserByAccountNumber(2);
        assertNull(user);
    }
    @Test
    void signInNegative_WrongPin() {
        String correctPin = "1111";
        String incorrectPin = "1112";
        when(userRepo.readUserByAccountNumber(3)).thenReturn(new User(3,correctPin));
        User user = userRepo.readUserByAccountNumber(3);
        assertNotNull(user);
        assertNotEquals(user.getAccountPin(), incorrectPin);
    }
}
