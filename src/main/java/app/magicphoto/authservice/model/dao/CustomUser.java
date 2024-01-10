package app.magicphoto.authservice.model.dao;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class CustomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(name = "access_code", unique = true)
    private String accessCode;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roleSet;

    public void setRoles(Collection<Role> rolesCollection) {
        if(roleSet == null) {
            roleSet = new HashSet<>();
        }
        roleSet.addAll(rolesCollection);
    }

    public Set<Role> getRoles() {
        return roleSet;
    }

}
