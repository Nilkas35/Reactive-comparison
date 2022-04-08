import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Cookie extends PanacheEntity {
    public String name;
}
