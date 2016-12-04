Business Need: Simulates a test that takes five seconds to run

  Scenario: Simulate a test that takes 1 seconds to execute
    Given a test that takes 1 seconds to execute
    When I execute the test
    Then it should take at least 1 seconds