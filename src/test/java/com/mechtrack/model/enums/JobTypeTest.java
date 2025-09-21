package com.mechtrack.model.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JobTypeTest {

    @Test
    void shouldHaveCorrectNumberOfTypes() {
        assertThat(JobType.values()).hasSize(15);
    }

    @Test
    void shouldContainExpectedTypes() {
        assertThat(JobType.values())
                .containsExactlyInAnyOrder(
                        JobType.OIL_CHANGE,
                        JobType.BRAKE_SERVICE,
                        JobType.TIRE_SERVICE,
                        JobType.ENGINE_TUNE_UP,
                        JobType.BATTERY_SERVICE,
                        JobType.TRANSMISSION_SERVICE,
                        JobType.COOLING_SYSTEM,
                        JobType.ELECTRICAL_REPAIR,
                        JobType.SUSPENSION_REPAIR,
                        JobType.EXHAUST_REPAIR,
                        JobType.AIR_CONDITIONING,
                        JobType.DIAGNOSTIC,
                        JobType.GENERAL_MAINTENANCE,
                        JobType.BODYWORK,
                        JobType.OTHER
                );
    }

    @Test
    void shouldHaveCorrectStringRepresentation() {
        assertThat(JobType.OIL_CHANGE.toString()).isEqualTo("OIL_CHANGE");
        assertThat(JobType.BRAKE_SERVICE.toString()).isEqualTo("BRAKE_SERVICE");
        assertThat(JobType.OTHER.toString()).isEqualTo("OTHER");
    }

    @Test
    void shouldParseFromString() {
        assertThat(JobType.valueOf("OIL_CHANGE")).isEqualTo(JobType.OIL_CHANGE);
        assertThat(JobType.valueOf("BRAKE_SERVICE")).isEqualTo(JobType.BRAKE_SERVICE);
        assertThat(JobType.valueOf("OTHER")).isEqualTo(JobType.OTHER);
    }
}
