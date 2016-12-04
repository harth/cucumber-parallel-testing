Business Need: Simulates a test that takes ten seconds to run

  Scenario: Simulate a test that takes 2 seconds to execute
    Given a test that takes 2 seconds to execute
    When I execute the test
    Then it should take at least 2 seconds