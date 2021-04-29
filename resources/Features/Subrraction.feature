Feature: Subtraction

  	@component-Subtraction  @priority-high
  Scenario Outline: Subtract two numbers
   Given I go to the calculator page
   And The calculator page is displayed
   And I click on button <number1>
	 And I click on the subtract button
   And I click on button <number2>
   When I click on the equal button
	 Then The calculator outputs the decimal <outputValue>
	 Examples:
		 | number1 | number2 | outputValue | 
		 |5			   |5        |0.0            |
		 |5			   |6        |-1.0           |
		 |9			   |5        |4.0            |