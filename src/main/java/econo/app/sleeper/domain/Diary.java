package econo.app.sleeper.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "DIARY")
public class Diary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryPk;
    @Lob
    private String content;
    @Column(name = "date")
    private LocalDate localDate;

    @Column(name = "delete_date")
    private LocalDate deleteLocalDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user; // 연관관계의 주인

    @Builder
    public Diary(String content, LocalDate localDate, User user){
        this.content = content;
        this.localDate = localDate;
        this.user = user;
    }

    public void updateContent(String content){
        this.content = content;
    }

}
