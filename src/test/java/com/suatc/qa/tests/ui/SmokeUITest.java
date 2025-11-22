package com.suatc.qa.tests.ui;

import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SmokeUITest {

    @Test
    public void frameworkIsWiredCorrectly() {
        assertThat(true).isTrue();
    }
}
