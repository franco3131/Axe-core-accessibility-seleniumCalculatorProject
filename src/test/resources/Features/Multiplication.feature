Feature: Multiplication


  @component-Multiplication  @priority-high
  Scenario Outline: Multiply two numbers
   Given I go to the calculator page
   And The calculator page is displayed
   And I click on button <number1>
   And I click on the multiply button
   And I click on button <number2>
   When I click on the equal button
   And The calculator outputs the decimal <outputValue>
	 Examples:
		 | number1 | number2 | outputValue | 
		 |5			   |6        |30.0           |
		 |0			   |3        |0.0            |
		 |3			   |0        |0.0            |
		 |9		     |1        |9.0            |


  @component-Multiplication  @priority-high
  Scenario Outline: Multiply two numbers and check accessibility
   Given I go to the calculator page
   And The calculator page is displayed
   And I click on button 5
   And I click on the multiply button
   And I click on button 6
   When I click on the equal button
   And The calculator outputs the decimal 30
   Then the page has no WCAG AA accessibility violations
