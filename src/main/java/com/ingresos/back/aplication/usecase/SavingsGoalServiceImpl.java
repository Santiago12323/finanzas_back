package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.enums.savings.GoalStatus;
import com.ingresos.back.domain.exception.SavingsGoalException;
import com.ingresos.back.domain.model.SavingsContribution;
import com.ingresos.back.domain.model.SavingsGoal;
import com.ingresos.back.domain.port.AccountService;
import com.ingresos.back.domain.port.SavingsGoalService;
import com.ingresos.back.domain.port.UseService;
import com.ingresos.back.domain.repository.SavingsContributionRepository;
import com.ingresos.back.domain.repository.SavingsGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SavingsGoalServiceImpl implements SavingsGoalService {

    @Autowired
    private SavingsGoalRepository goalRepository;

    @Autowired
    private SavingsContributionRepository contributionRepository;

    @Autowired
    private UseService useService;

    @Autowired
    private AccountService accountService;

    // ---------- Metas ----------

    @Override
    public SavingsGoal createGoal(SavingsGoal goal) {
        if (goal.getStartDate() == null) {
            goal.setStartDate(LocalDate.now());
        }
        goal.setStatus(GoalStatus.ACTIVE);
        validate(goal);
        return enrich(goalRepository.save(goal));
    }

    @Override
    public SavingsGoal findById(String id) {
        return enrich(getGoal(id));
    }

    @Override
    public List<SavingsGoal> findByUserId(String userId, GoalStatus status) {
        useService.findById(userId);
        List<SavingsGoal> goals = status == null
                ? goalRepository.findByUserId(userId)
                : goalRepository.findByUserIdAndStatus(userId, status);
        return goals.stream().map(this::enrich).toList();
    }

    @Override
    @Transactional
    public SavingsGoal updateGoal(String id, SavingsGoal goal) {
        SavingsGoal existing = getGoal(id);
        goal.setId(id);
        goal.setUserId(existing.getUserId());   // el dueño no se puede cambiar
        goal.setStatus(existing.getStatus());   // el estado se cambia con changeStatus
        if (goal.getStartDate() == null) {
            goal.setStartDate(existing.getStartDate());
        }
        validate(goal);
        SavingsGoal saved = goalRepository.save(goal);
        return enrich(syncStatus(saved));
    }

    @Override
    public SavingsGoal changeStatus(String id, GoalStatus status) {
        if (status == null) {
            throw new SavingsGoalException("El estado es obligatorio");
        }
        SavingsGoal goal = getGoal(id);
        if (goal.getStatus() == GoalStatus.COMPLETED || goal.getStatus() == GoalStatus.CANCELLED) {
            throw new SavingsGoalException("No se puede cambiar el estado de una meta completada o cancelada");
        }
        if (status == GoalStatus.COMPLETED) {
            throw new SavingsGoalException("La meta se completa automáticamente al alcanzar el monto objetivo");
        }
        goal.setStatus(status);
        return enrich(goalRepository.save(goal));
    }

    @Override
    @Transactional
    public void deleteGoal(String id) {
        getGoal(id);
        contributionRepository.deleteByGoalId(id);
        goalRepository.deleteById(id);
    }

    // ---------- Aportes ----------

    @Override
    @Transactional
    public SavingsContribution addContribution(String goalId, SavingsContribution contribution) {
        SavingsGoal goal = getGoal(goalId);
        if (goal.getStatus() != GoalStatus.ACTIVE) {
            throw new SavingsGoalException("Solo se pueden registrar aportes en metas activas");
        }
        if (contribution.getAmount() == null || contribution.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new SavingsGoalException("El monto del aporte debe ser mayor a cero");
        }

        if (contribution.getContributionDate() == null) {
            contribution.setContributionDate(LocalDate.now());
        }
        contribution.setId(null);
        contribution.setGoalId(goalId);

        SavingsContribution saved = contributionRepository.save(contribution);
        syncStatus(goal);   // si con este aporte llegó a la meta, pasa a COMPLETED
        return saved;
    }

    @Override
    public List<SavingsContribution> findContributionsByGoalId(String goalId) {
        getGoal(goalId);
        return contributionRepository.findByGoalId(goalId);
    }

    @Override
    @Transactional
    public void deleteContribution(String goalId, String contributionId) {
        SavingsGoal goal = getGoal(goalId);
        SavingsContribution contribution = contributionRepository.findById(contributionId)
                .orElseThrow(() -> new SavingsGoalException("Aporte no encontrado con ID: " + contributionId));
        if (!goalId.equals(contribution.getGoalId())) {
            throw new SavingsGoalException("El aporte no pertenece a la meta indicada");
        }
        contributionRepository.deleteById(contributionId);
        syncStatus(goal);   // si estaba COMPLETED y ya no alcanza, vuelve a ACTIVE
    }

    // ---------- Internos ----------

    private SavingsGoal getGoal(String id) {
        return goalRepository.findById(id)
                .orElseThrow(() -> new SavingsGoalException("Meta de ahorro no encontrada con ID: " + id));
    }

    private void validate(SavingsGoal goal) {
        if (goal.getName() == null || goal.getName().isBlank()) {
            throw new SavingsGoalException("El nombre de la meta es obligatorio");
        }
        if (goal.getTargetAmount() == null || goal.getTargetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new SavingsGoalException("El monto objetivo debe ser mayor a cero");
        }
        if (goal.getCurrency() == null) {
            throw new SavingsGoalException("La moneda es obligatoria");
        }
        if (goal.getPlannedMonthlyAmount() != null
                && goal.getPlannedMonthlyAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new SavingsGoalException("El ahorro mensual planeado debe ser mayor a cero");
        }
        if (goal.getTargetDate() != null && goal.getStartDate() != null
                && goal.getTargetDate().isBefore(goal.getStartDate())) {
            throw new SavingsGoalException("La fecha objetivo no puede ser anterior a la fecha de inicio");
        }
        useService.findById(goal.getUserId());
    }

    /** Pasa ACTIVE -> COMPLETED al alcanzar la meta, y COMPLETED -> ACTIVE si deja de alcanzarla. */
    private SavingsGoal syncStatus(SavingsGoal goal) {
        BigDecimal saved = contributionRepository.sumByGoalId(goal.getId());
        boolean reached = saved.compareTo(goal.getTargetAmount()) >= 0;

        if (goal.getStatus() == GoalStatus.ACTIVE && reached) {
            goal.setStatus(GoalStatus.COMPLETED);
            return goalRepository.save(goal);
        }
        if (goal.getStatus() == GoalStatus.COMPLETED && !reached) {
            goal.setStatus(GoalStatus.ACTIVE);
            return goalRepository.save(goal);
        }
        return goal;
    }

    /** Calcula progreso, meses restantes y ahorro mensual requerido. */
    private SavingsGoal enrich(SavingsGoal goal) {
        BigDecimal saved = contributionRepository.sumByGoalId(goal.getId());
        BigDecimal target = goal.getTargetAmount();
        BigDecimal remaining = target.subtract(saved).max(BigDecimal.ZERO);

        BigDecimal progress = saved.multiply(BigDecimal.valueOf(100))
                .divide(target, 2, RoundingMode.HALF_UP)
                .min(BigDecimal.valueOf(100));

        goal.setSavedAmount(saved);
        goal.setRemainingAmount(remaining);
        goal.setProgressPercent(progress);

        BigDecimal planned = goal.getPlannedMonthlyAmount();

        if (remaining.signum() == 0) {
            goal.setMonthsLeft(0);
        } else if (planned != null && planned.signum() > 0) {
            goal.setMonthsLeft(remaining.divide(planned, 0, RoundingMode.CEILING).intValue());
        } else {
            goal.setMonthsLeft(null);
        }

        if (goal.getTargetDate() != null) {
            BigDecimal required;
            if (remaining.signum() == 0) {
                required = BigDecimal.ZERO;
            } else {
                long months = ChronoUnit.MONTHS.between(
                        YearMonth.from(LocalDate.now()), YearMonth.from(goal.getTargetDate()));
                required = remaining.divide(BigDecimal.valueOf(Math.max(1, months)), 2, RoundingMode.UP);
            }
            goal.setRequiredMonthlyAmount(required);
            goal.setOnTrack(planned == null ? null : planned.compareTo(required) >= 0);
        } else {
            goal.setRequiredMonthlyAmount(null);
            goal.setOnTrack(null);
        }
        return goal;
    }
}