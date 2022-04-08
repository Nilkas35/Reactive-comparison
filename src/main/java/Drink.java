import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Drink extends PanacheEntity {
    public String name;
}
