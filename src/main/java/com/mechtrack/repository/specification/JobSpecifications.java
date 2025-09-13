package com.mechtrack.repository.specification;

import com.mechtrack.model.dto.JobSearchCriteria;
import com.mechtrack.model.entity.Job;
import com.mechtrack.model.entity.Part;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class JobSpecifications {

    public static Specification<Job> withCriteria(JobSearchCriteria criteria) {
        return GenericQuerySpec.build((root, query, cb, predicates) -> {
            GenericQuerySpec.addStringFilter(root, cb, predicates, "customerName", criteria.getCustomerName());
            GenericQuerySpec.addStringFilter(root, cb, predicates, "carModel", criteria.getCarModel());
            GenericQuerySpec.addStringFilter(root, cb, predicates, "description", criteria.getDescription());
            GenericQuerySpec.addDateRangeFilter(root, cb, predicates, "date", criteria.getStartDate(), criteria.getEndDate());
            GenericQuerySpec.addDecimalRangeFilter(root, cb, predicates, "income", criteria.getMinIncome(), criteria.getMaxIncome());

            if (StringUtils.hasText(criteria.getPartName()) || criteria.getMinPartCost() != null || criteria.getMaxPartCost() != null) {
                Join<Job, Part> partJoin = root.join("parts", JoinType.LEFT);
                
                if (StringUtils.hasText(criteria.getPartName())) {
                    predicates.add(cb.like(cb.upper(partJoin.get("name")), "%" + criteria.getPartName().toUpperCase() + "%"));
                }
                
                GenericQuerySpec.addDecimalRangeFilter(partJoin, cb, predicates, "cost", criteria.getMinPartCost(), criteria.getMaxPartCost());
                query.distinct(true);
            }
        });
    }

} 