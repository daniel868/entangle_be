package org.example.entablebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "ent_user")
@Getter
@Setter
@Entity
@NamedQueries(value = {
        @NamedQuery(
                name = "UserEntangle.fetchUserById",
                query = "select us from UserEntangle us where us.id =:userId"
        )
})
public class UserEntangle implements UserDetails {
    @Id
    @SequenceGenerator(name = "ent_user_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ent_user_sequence")
    private Long id;

    private String username;
    private String password;
    private String email;

    private String info;

    @Column(name = "validation_email_token")
    private String validationEmailToken;

    @Column(name = "account_activate")
    private boolean accountActivate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "ent_user_competence",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "competence_id")}
    )
    private Set<Competence> competences;


    @OneToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    @JoinColumn(name = "user_id")
    private Set<Treatment> treatments;

    @Lob
    @Column(name = "profile_image_base64")
    private String profileImage;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    public void addCompetence(Competence competence) {
        if (competences == null) {
            competences = new HashSet<>();
        }
        competences.add(competence);
    }

    public void addTreatment(Treatment treatment) {
        if (treatments == null) {
            treatments = new HashSet<>();
        }
        treatments.add(treatment);
    }
}
