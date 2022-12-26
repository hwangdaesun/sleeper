package econo.app.sleeper.service.diary;

import econo.app.sleeper.domain.Diary;
import econo.app.sleeper.domain.User;
import econo.app.sleeper.repository.CharacterRepository;
import econo.app.sleeper.repository.DiaryRepository;
import econo.app.sleeper.repository.UserRepository;
import econo.app.sleeper.util.DateJudgementUtil;
import econo.app.sleeper.util.InitCharacter;
import econo.app.sleeper.util.MoneyManager;
import econo.app.sleeper.web.diary.DiaryDateDto;
import econo.app.sleeper.web.diary.DiaryTimeDto;
import econo.app.sleeper.web.diary.DiaryRequestForm;
import econo.app.sleeper.web.diary.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    @Transactional
    public DiaryResponse saveDiary(DiaryTimeDto diaryTimeDto){
        LocalDate localDate = DateJudgementUtil.checkSavingDate(diaryTimeDto.getLocalDateTime());
        User user = userRepository.findById(diaryTimeDto.getUserId()).get();// 로그인 할 때 필터링이 되있기 때문에 null 체크 안 함
        Diary diary = diaryTimeDto.toEntity(localDate, user);
        diaryRepository.save(diary);
        // 돈 증가
        Integer judgeMoney = MoneyManager.judgeMoney(diaryTimeDto.getContent());
        Integer increasedMoney = MoneyManager.earnMoney(user.getUserMoney(), judgeMoney);
        user.updateMoney(increasedMoney);
        return DiaryResponse.of(diary.getDiaryPk(),judgeMoney);
    }

    @Transactional
    public void updateDiary(Long diaryPk,String content) {
        Optional<Diary> Odiary = diaryRepository.findByPk(diaryPk);
        Odiary.get().updateContent(content);
    }

    @Transactional
    public void deleteDiary(Long diaryPk){
        Optional<Diary> Odiary = diaryRepository.findByPk(diaryPk);
        diaryRepository.delete(Odiary.get());
    }

    // 관리자에서 이용
    public Diary findDiary(Long diaryPk) {
        return diaryRepository.findByPk(diaryPk).orElseThrow(NullPointerException::new);
    }

    public List<Diary> findDiariesByUser(String userId){
        User user = userRepository.findById(userId).get();
        return diaryRepository.findAllByPk(user.getUserPk());
    }

    public List<Diary> findDiariesByDate(DiaryDateDto diaryDateDto){
        User user = userRepository.findById(diaryDateDto.getUserId()).get();
        Long userPk = user.getUserPk();
        return diaryRepository.findByDate(userPk, diaryDateDto.getLocalDate());
    }

}
