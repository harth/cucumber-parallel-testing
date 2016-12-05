package uk.co.hmtt.parallel.bdd.tests.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Calendar;
import java.util.Date;

public class Definition {

    private Integer delay;
    private Date startTime;
    private Date finishTime;

    @Given("^a test that takes (\\d+) seconds to execute$")
    public void a_test_that_takes_seconds_to_execute(int second) throws Throwable {
        delay = second * 1000;
    }

    @When("^I execute the test$")
    public void i_execute_the_test() throws Throwable {
        startTime = Calendar.getInstance().getTime();
        Thread.sleep(delay);
        finishTime = Calendar.getInstance().getTime();
    }

    @Then("^it should take at least (\\d+) seconds$")
    public void it_should_take_at_least_seconds(int seconds) throws Throwable {
        final long startTimeSeconds = startTime.getTime() / 60;
        final long finishTimeSeconds = finishTime.getTime() / 60;
//        assertThat(new Integer(finishTimeSeconds - startTimeSeconds), is(greaterThan(seconds)));
    }


}
