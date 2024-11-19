package org.aston.customerservice.security;

import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.persistent.entity.UserProfile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private String phoneNumber;

    private String password;

    private Set<Role> authorities;

    private String uuid;

    private String status;

    public CustomUserDetails(Customer customer, UserProfile userProfile) {
        phoneNumber = customer.getPhoneNumber();
        password = userProfile.getHashPassword();
        authorities = userProfile.getRoles();
        uuid = String.valueOf(customer.getCustomerId());
        status = String.valueOf(customer.getStatus().getStatusType());
    }

    public CustomUserDetails(String phoneNumber, String uuid, String status) {
        this.phoneNumber = phoneNumber;
        this.uuid = uuid;
        this.status = status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
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
        return true;
    }

    public String getUuid() {
        return uuid;
    }

    public String getStatus() {
        return status;
    }
}