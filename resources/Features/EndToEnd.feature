Feature: End to End

  @component-EndToEnd  @priority-medium
  Scenario: Multiple operations 
   Given I go to the calculator page
   And The calculator page is displayed
   And I click on button 2
   And I click on the subtract button
   And I click on button 4
   And I click on the equal button
   And I click on the multiply button
   And I click on button 3
   And I click on the equal button
   And I click on the division button
   And I click on button 4
   When I click on the equal button
   Then The calculator outputs the decimal -1.5
