package mate.academy.spring.service.impl;

import static org.springframework.security.core.userdetails.User.withUsername;

import mate.academy.spring.model.User;
import mate.academy.spring.service.UserService;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("Can't find user by email " + username));
        UserBuilder builder = withUsername(username);
        builder.password(user.getPassword());
        builder.roles();
        builder.authorities(getAuthorities(user));
        return builder.build();
    }

    private String[] getAuthorities(User user) {
        return user.getRoles().stream()
                .map(r -> r.getName().name())
                .toArray(String[]::new);
    }
}