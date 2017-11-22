package org.activiti.cloud.services.identity.basic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class BasicAuthenticationProviderTest {

    private BasicAuthenticationProvider basicAuthenticationProvider;

    @Mock
    private UserDetailsService userDetailsService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        basicAuthenticationProvider = new BasicAuthenticationProvider(userDetailsService);
    }

    @Test
    public void testAuthenticate(){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("testrole"));
        User user = new User("test","pass",authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test","pass",authorities);

        when(userDetailsService.loadUserByUsername("test"))
                .thenReturn(user);

        assertThat(basicAuthenticationProvider.authenticate(authentication)).isNotNull();
    }
}
