package com.mechtrack.model.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JobStatusTest {

    @Test
    void shouldHaveCorrectNumberOfStatuses() {
        assertThat(JobStatus.values()).hasSize(3);
    }

    @Test
    void shouldContainExpectedStatuses() {
        assertThat(JobStatus.values())
                .containsExactlyInAnyOrder(
                        JobStatus.WAITING,
                        JobStatus.IN_PROGRESS,
                        JobStatus.DONE
                );
    }

    @Test
    void shouldHaveCorrectStringRepresentation() {
        assertThat(JobStatus.WAITING.toString()).isEqualTo("WAITING");
        assertThat(JobStatus.IN_PROGRESS.toString()).isEqualTo("IN_PROGRESS");
        assertThat(JobStatus.DONE.toString()).isEqualTo("DONE");
    }

    @Test
    void shouldParseFromString() {
        assertThat(JobStatus.valueOf("WAITING")).isEqualTo(JobStatus.WAITING);
        assertThat(JobStatus.valueOf("IN_PROGRESS")).isEqualTo(JobStatus.IN_PROGRESS);
        assertThat(JobStatus.valueOf("DONE")).isEqualTo(JobStatus.DONE);
    }
}
