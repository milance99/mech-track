package com.mechtrack.repository.specification;

import com.mechtrack.model.dto.JobSearchCriteria;
import com.mechtrack.model.entity.Job;
import com.mechtrack.model.entity.Part;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class JobSpecifications {

    public static Specification<Job> withCriteria(JobSearchCriteria criteria) {
        return GenericQuerySpec.build((root, query, cb, predicates) -> {
            addBasicFilters(root, cb, predicates, criteria);
            addStatusFilters(root, cb, predicates, criteria);
            addTypeFilters(root, cb, predicates, criteria);
            addPartFilters(root, query, cb, predicates, criteria);
        });
    }

    private static void addBasicFilters(jakarta.persistence.criteria.Root<Job> root, 
                                      jakarta.persistence.criteria.CriteriaBuilder cb, 
                                      java.util.List<jakarta.persistence.criteria.Predicate> predicates, 
                                      JobSearchCriteria criteria) {
        GenericQuerySpec.addStringFilter(root, cb, predicates, "customerName", criteria.getCustomerName());
        GenericQuerySpec.addStringFilter(root, cb, predicates, "carModel", criteria.getCarModel());
        GenericQuerySpec.addStringFilter(root, cb, predicates, "description", criteria.getDescription());
        GenericQuerySpec.addDateRangeFilter(root, cb, predicates, "date", criteria.getStartDate(), criteria.getEndDate());
        GenericQuerySpec.addDecimalRangeFilter(root, cb, predicates, "income", criteria.getMinIncome(), criteria.getMaxIncome());
    }

    private static void addStatusFilters(jakarta.persistence.criteria.Root<Job> root, 
                                       jakarta.persistence.criteria.CriteriaBuilder cb, 
                                       java.util.List<jakarta.persistence.criteria.Predicate> predicates, 
                                       JobSearchCriteria criteria) {
        if (criteria.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        }
        
        if (!CollectionUtils.isEmpty(criteria.getStatuses())) {
            predicates.add(root.get("status").in(criteria.getStatuses()));
        }
    }

    private static void addTypeFilters(jakarta.persistence.criteria.Root<Job> root, 
                                     jakarta.persistence.criteria.CriteriaBuilder cb, 
                                     java.util.List<jakarta.persistence.criteria.Predicate> predicates, 
                                     JobSearchCriteria criteria) {
        if (criteria.getType() != null) {
            predicates.add(cb.equal(root.get("type"), criteria.getType()));
        }
        
        if (!CollectionUtils.isEmpty(criteria.getTypes())) {
            predicates.add(root.get("type").in(criteria.getTypes()));
        }
    }

    private static void addPartFilters(jakarta.persistence.criteria.Root<Job> root, 
                                     jakarta.persistence.criteria.CriteriaQuery<?> query, 
                                     jakarta.persistence.criteria.CriteriaBuilder cb, 
                                     java.util.List<jakarta.persistence.criteria.Predicate> predicates, 
                                     JobSearchCriteria criteria) {
        if (StringUtils.hasText(criteria.getPartName()) || criteria.getMinPartCost() != null || criteria.getMaxPartCost() != null) {
            Join<Job, Part> partJoin = root.join("parts", JoinType.LEFT);
            
            if (StringUtils.hasText(criteria.getPartName())) {
                predicates.add(cb.like(cb.upper(partJoin.get("name")), "%" + criteria.getPartName().toUpperCase() + "%"));
            }
            
            GenericQuerySpec.addDecimalRangeFilter(partJoin, cb, predicates, "cost", criteria.getMinPartCost(), criteria.getMaxPartCost());
            query.distinct(true);
        }
    }

} 
