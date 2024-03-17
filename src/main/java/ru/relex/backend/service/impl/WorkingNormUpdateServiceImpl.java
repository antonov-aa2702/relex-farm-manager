package ru.relex.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.relex.backend.aop.annotation.Loggable;
import ru.relex.backend.dto.product.WorkingNormDto;
import ru.relex.backend.entity.Status;
import ru.relex.backend.service.WorkingNormService;
import ru.relex.backend.service.WorkingNormUpdateService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация {@link WorkingNormUpdateService}
 */
@Service
@RequiredArgsConstructor
public class WorkingNormUpdateServiceImpl implements
        WorkingNormUpdateService {

    private final WorkingNormService workingNormService;

    @Override
    @Scheduled(cron = "${spring.scheduled.cron.expression.every_minute}")
    @Loggable
    public void updateWorkingNormPeriodically() {
        List<WorkingNormDto> workingNorms =
                workingNormService.getWorkingNormByStatus(Status.IN_PROGRESS);
        workingNorms.forEach(
                workingNorm -> {
                    boolean updateStatus = isUpdateStatusWorkingNormByConditions(workingNorm);
                    if (updateStatus) {
                        workingNormService.updateWorkingNorm(workingNorm);
                    }
                }
        );
    }

    private boolean isUpdateStatusWorkingNormByConditions(WorkingNormDto workingNormDto) {
        if (workingNormDto.getCurrentCount() >= workingNormDto.getWorkingCount()) {
            workingNormDto.setStatus(Status.DONE);
            return true;
        } else {
            if (workingNormDto.getDeadline().isBefore(LocalDateTime.now())) {
                workingNormDto.setStatus(Status.OVERDUE);
                return true;
            }
        }
        return false;
    }
}
