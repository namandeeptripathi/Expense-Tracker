package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.CreateIncomeRequest;
import com.namandeep.expensetracker.dto.IncomeFilter;
import com.namandeep.expensetracker.dto.IncomeResponse;
import com.namandeep.expensetracker.dto.PageResponse;
import com.namandeep.expensetracker.dto.UpdateIncomeRequest;
import com.namandeep.expensetracker.entity.Income;
import com.namandeep.expensetracker.entity.User;
import com.namandeep.expensetracker.exception.IncomeNotFoundException;
import com.namandeep.expensetracker.repository.IncomeRepository;
import com.namandeep.expensetracker.repository.IncomeSpecifications;
import com.namandeep.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Transactional income management implementation for the current user. */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final IncomeMapper incomeMapper;

    @Override
    @Transactional
    public IncomeResponse create(String userEmail, CreateIncomeRequest request) {
        User user = currentUser(userEmail);
        return incomeMapper.toResponse(incomeRepository.save(incomeMapper.toEntity(request, user)));
    }

    @Override
    @Transactional
    public IncomeResponse update(String userEmail, Long incomeId, UpdateIncomeRequest request) {
        User user = currentUser(userEmail);
        Income income = findOwnedIncome(incomeId, user.getId());
        incomeMapper.updateEntity(income, request);
        return incomeMapper.toResponse(income);
    }

    @Override
    @Transactional
    public void delete(String userEmail, Long incomeId) {
        User user = currentUser(userEmail);
        incomeRepository.delete(findOwnedIncome(incomeId, user.getId()));
    }

    @Override
    public IncomeResponse getById(String userEmail, Long incomeId) {
        User user = currentUser(userEmail);
        return incomeMapper.toResponse(findOwnedIncome(incomeId, user.getId()));
    }

    @Override
    public PageResponse<IncomeResponse> getAll(String userEmail, IncomeFilter filter, Pageable pageable) {
        User user = currentUser(userEmail);
        Page<IncomeResponse> page = incomeRepository
                .findAll(IncomeSpecifications.forUserAndFilter(user.getId(), filter), pageable)
                .map(incomeMapper::toResponse);
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                pageable.getSort().toString());
    }

    private User currentUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IncomeNotFoundException("Authenticated user was not found"));
    }

    private Income findOwnedIncome(Long incomeId, Long userId) {
        return incomeRepository.findByIdAndUserId(incomeId, userId)
                .orElseThrow(() -> new IncomeNotFoundException("Income not found: " + incomeId));
    }
}
