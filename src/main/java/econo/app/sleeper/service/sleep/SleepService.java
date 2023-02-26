package econo.app.sleeper.service.sleep;

import econo.app.sleeper.domain.common.DatePolicy;
import econo.app.sleeper.domain.sleep.Sleep;
import econo.app.sleeper.domain.user.User;
import econo.app.sleeper.exception.RestApiException;
import econo.app.sleeper.exception.error.CommonErrorCode;
import econo.app.sleeper.repository.SleepRepository;
import econo.app.sleeper.repository.UserRepository;
import econo.app.sleeper.web.sleep.SleepDto;
import econo.app.sleeper.web.sleep.SleepRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SleepService {

    private final SleepRepository sleepRepository;
    private final UserRepository userRepository;
    private final DatePolicy datePolicy;

    @Transactional
    public Long saveSleep(SleepRequest sleepRequest){
        User user = userRepository.find(sleepRequest.getUserPk()).get();
        LocalDate decidedDate = decideDate(sleepRequest.getActualSleepTime());
        Sleep sleep = Sleep.createSleep(sleepRequest.getSetSleepTime(),sleepRequest.getSetWakeTime(),sleepRequest.getActualSleepTime(),decidedDate,user);
        sleepRepository.save(sleep);
        return sleep.getId();
    }

    private LocalDate decideDate(ZonedDateTime savedDateTime){
        LocalDate decidedDate = datePolicy.decideDate(savedDateTime);
        return decidedDate;
    }

    @Transactional
    public void updateActualWakeTime(SleepDto sleepDto){
        Sleep sleep = sleepRepository.findByPk(sleepDto.getSleepPk())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
        sleep.updateActualWakeTime(sleepDto.getActualWakeTime());
    }

}
