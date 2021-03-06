package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "users")
@NamedQueries(
        {
                @NamedQuery(name = "userByUuid", query = "select u from UserEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email =:email"),
                @NamedQuery(name = "userByUserName", query = "select u from UserEntity u where u.username =:username")
        }
)
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 200)
    private String uuid;

    @NotNull
    @Size(max = 30)
    private String firstname;

    @NotNull
    @Size(max = 30)
    private String lastname;

    @NotNull
    @Size(max = 30)
    private String username;

    @NotNull
    @Size(max = 50)
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 200)
    private String salt;

    @NotNull
    @Size(max = 30)
    private String country;

    @NotNull
    @Size(max = 50)
    private String aboutme;

    @NotNull
    @Size(max = 30)
    private String dob;

    @NotNull
    @Size(max = 30)
    private String role;

    @NotNull
    @Size(max = 30)
    private String contactnumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", country='" + country + '\'' +
                ", aboutme='" + aboutme + '\'' +
                ", dob='" + dob + '\'' +
                ", role='" + role + '\'' +
                ", contactnumber='" + contactnumber + '\'' +
                '}';
    }
}
