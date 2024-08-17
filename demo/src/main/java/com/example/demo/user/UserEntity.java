package com.example.demo.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.Book.Book;
import com.example.demo.History.BookTransactionHistory;
import com.example.demo.role.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue()
    private Integer id;
    private String firstname;
    private String lastname;

    @Column(unique = true)
    private String email;

    private boolean accountLocked;
    
    private boolean enabled;
    
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @CreatedDate
    @Column(nullable = false , updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate; 

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    @OneToMany(mappedBy = "user")
    private List<BookTransactionHistory> histories;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());        
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFullName(){
        return firstname + " "+lastname;
    }
    
}
