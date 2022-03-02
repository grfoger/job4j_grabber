package ru.job4j.model;

import java.time.LocalDateTime;

public class Post {
    int id;
    String title;
    String link;
    String description;
    LocalDateTime created;

    public Post() {
    }

    public Post(String title, String link, String description, LocalDateTime created) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    @Override
    public String toString() {
        return "Post{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", link='" + link + '\''
                + ", description='" + description + '\''
                + ", created=" + created
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Post post = (Post) o;

        if (title != null ? !title.equals(post.title) : post.title != null) {
            return false;
        }
        if (link != null ? !link.equals(post.link) : post.link != null) {
            return false;
        }
        if (description != null ? !description.equals(post.description) : post.description != null) {
            return false;
        }
        return created != null ? created.equals(post.created) : post.created == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }
}
