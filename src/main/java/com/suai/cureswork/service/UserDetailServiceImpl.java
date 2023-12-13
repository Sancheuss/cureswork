package com.suai.cureswork.service;

import com.suai.cureswork.crud.entity.User;
import com.suai.cureswork.crud.entity.UserDetailsImpl;
import com.suai.cureswork.crud.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByName(userName);
        user.orElseThrow(() -> new UsernameNotFoundException(userName + " not found."));
        return user.map(UserDetailsImpl::new).get();
    }
}

