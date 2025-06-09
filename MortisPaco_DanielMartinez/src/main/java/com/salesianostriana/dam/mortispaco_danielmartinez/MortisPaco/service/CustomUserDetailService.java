package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.service;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}