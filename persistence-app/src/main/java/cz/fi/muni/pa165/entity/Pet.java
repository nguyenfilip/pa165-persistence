package cz.fi.muni.pa165.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Pet {

    @Id
    @GeneratedValue
    private long id = 0;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Cage cage = null;

    @Enumerated(EnumType.STRING)
    private PetColor color;

    public enum PetColor {

        BLACK, WHITE, RED
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cage getCage() {
        return cage;
    }

    public void setCage(Cage cage) {
        this.cage = cage;
    }

    public PetColor getColor() {
        return color;
    }

    public void setColor(PetColor color) {
        this.color = color;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Pet other = (Pet) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pet [id=" + id + ", birthDate=" + birthDate + ", name=" + name + ", color=" + color + "]";
    }

}
