package com.game.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "race")
    private Race race;

    @Enumerated(EnumType.STRING)
    @Column(name = "profession")
    private Profession profession;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "level")
    private Integer level;

    @Column(name = "untilNextLevel")
    private Integer untilNextLevel;

    @Column(name = "birthday")
    private Date birthday;

    @ColumnDefault("false")
    @Column(name = "banned")
    private Boolean banned;

    public Player setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Player setTitle(String title) {
        this.title = title;
        return this;
    }

    public Race getRace() {
        return race;
    }

    public Player setRace(Race race) {
        this.race = race;
        return this;
    }

    public Profession getProfession() {
        return profession;
    }

    public Player setProfession(Profession profession) {
        this.profession = profession;
        return this;
    }

    public Integer getExperience() {
        return experience;
    }

    public Player setExperience(Integer experience) {
        this.experience = experience;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public Player setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public Player setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
        return this;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Player setBirthday(Date birthday) {
        this.birthday = birthday;
        return this;
    }

    public Boolean getBanned() {
        return banned;
    }

    public Player setBanned(Boolean banned) {
        this.banned = banned;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Player{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", race=").append(race);
        sb.append(", profession=").append(profession);
        sb.append(", experience=").append(experience);
        sb.append(", level=").append(level);
        sb.append(", untilNextLevel=").append(untilNextLevel);
        sb.append(", birthday=").append(birthday);
        sb.append(", banned=").append(banned);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) && Objects.equals(name, player.name) && Objects.equals(title, player.title) && race == player.race && profession == player.profession && Objects.equals(experience, player.experience) && Objects.equals(level, player.level) && Objects.equals(untilNextLevel, player.untilNextLevel) && Objects.equals(birthday, player.birthday) && Objects.equals(banned, player.banned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, race, profession, experience, level, untilNextLevel, birthday, banned);
    }
}
