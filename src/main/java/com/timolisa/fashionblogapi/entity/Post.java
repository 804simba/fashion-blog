package com.timolisa.fashionblogapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.timolisa.fashionblogapi.enums.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
@Builder

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity{
    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Like> likes;

    @JsonIgnore
    @ManyToOne
    private User user;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
