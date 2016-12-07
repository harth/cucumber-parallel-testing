Business Need: Simulates the first test

  Scenario: Simulate the first test
    Given a test that takes 5 seconds to execute
    When I execute the test
    Then it should take at least the execution time to complete