package kr.gracelove.demorestapi.accounts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;
    @Test
    void findByUsername() throws Exception {

        //Given
        String password = "password";
        Account account = Account.builder()
                .email("grace@gmail.com")
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        accountRepository.save(account);

        //When
        UserDetailsService userDetailsService = (UserDetailsService)accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername("grace@gmail.com");

        //Then
        assertEquals(password, userDetails.getPassword());
    }

    @Test
    void findByUsernameFail() {
        String username = "dsakjdlsadjkl";
        assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername(username));
    }
}