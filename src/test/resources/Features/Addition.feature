Feature: Addition

    @component-Addition @priority-high
  Scenario Outline: Add two numbers
   Given I go to the calculator page
   And The calculator page is displayed
   And I click on button <number1>
   And I click on the plus button
   And I click on button <number2>
   When I click on the equal button
   And The calculator outputs <outputValue>
	 Examples:
		 | number1 | number2 | outputValue | 
		 |1			   |2        |3            |
		 |0			   |2        |2            |
		 |0			   |0        |0            |
		 |9		     |1        |10           |


    @component-Addition @priority-high @accessibility
  Scenario: Add two numbers and check accessibility
   Given I go to the calculator page
   And The calculator page is displayed
   And I click on button 1
   And I click on the plus button
   And I click on button 2
   When I click on the equal button
   And The calculator outputs 3
   Then the page has no WCAG AA accessibility violations
		 
