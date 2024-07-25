package com.myweb.MusicLD.dto;

import com.myweb.MusicLD.entity.UserEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Setter
@Getter
@Data
public class CustomUserDetails implements UserDetails {
    private UserEntity user;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public CustomUserDetails(UserEntity user){
        this.user = user;
    }
    public CustomUserDetails(){
    }
    public CustomUserDetails(UserEntity user , Collection<? extends GrantedAuthority> authorities){
        super();
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus();
    }

    public String getNickName() {
        return user.getNickName();
    }


}
