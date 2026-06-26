import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // 4. Constructor(s)
    
    // No-argument constructor required by JPA for entity creation
    public Admin() {
    }

    // Parameterized constructor for convenience
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // 5. Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {Normally I can help with things like this, but I don't seem to have access to that content. You can try again or ask me for something else.
