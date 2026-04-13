package com.anthony.blacksmithOnlineStore.entity;

import com.anthony.blacksmithOnlineStore.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@ToString(exclude = {"password", "orders"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User implements UserDetails {
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  @Column(unique = true, nullable = false)
  private String username;
  @Column(nullable = false)
  private String password;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;
  @Column(nullable = false, name = "birth_date")
  private LocalDate birthDate;
  @Setter(AccessLevel.NONE)
  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
  private final List<Order> orders = new ArrayList<>();

  public void addOrder(Order order) {
    orders.add(order);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(role);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User user)) return false;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
