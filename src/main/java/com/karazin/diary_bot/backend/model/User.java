package com.karazin.diary_bot.backend.model;

import com.karazin.diary_bot.bot.util.BotState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "telegram_id", unique = true)
    private Long telegramId;

    @Enumerated(EnumType.STRING)
    @Column(name = "bot_state")
    private BotState botState;

    @Column(name = "chosen_post_id")
    private Long currentIdPostForCommand;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Post> posts;

    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", telegramId='" + telegramId + '\'' +
                ", botState='" + botState + '\'' +
                '}';
    }

}
