Feature: Calculator 

  @calculator @priority-high
  Scenario Outline: Add two numbers
   Given I go to the calculator page
   And The calculator page is displayed
   And I click on button <number1>
	 And I click on the plus button
   And I click on button <number2>
   When I click on the equal button
	 Then The calculator outputs <outputValue>
	 Examples:
		 | number1 | number2 | outputValue | 
		 |1			   |2        |3            |
		 |0			   |2        |2            |
		 |0			   |0        |0            |
		 |9		     |1        |10           |
		 
	@calculator  @priority-high
  Scenario Outline: Divide two numbers
   Given I go to the calculator page
   And The calculator page is displayed
   And I click on button <number1>
	 And I click on the division button
   And I click on button <number2>
   When I click on the equal button
	 Then The calculator outputs the decimal <outputValue>
	 Examples:
		 | number1 | number2 | outputValue | 
		 |3			   |2        |1.5          |
		 |0			   |3        |0.0            |
		 |4			   |2        |2.0            |
		 |9		     |9        |1.0            |
		 
	@calculator  @priority-high
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
	 
	 
	@calculator  @priority-high
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
	 
	@calculator  @priority-high
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
		 
  @calculator  @priority-high
  Scenario Outline: Multiply two numbers
   Given I go to the calculator page
   And The calculator page is displayed
   And I click on button <number1>
	 And I click on the multiply button
   And I click on button <number2>
   When I click on the equal button
	 Then The calculator outputs the decimal <outputValue>
	 Examples:
		 | number1 | number2 | outputValue | 
		 |5			   |6        |30.0           |
		 |0			   |3        |0.0            |
		 |3			   |0        |0.0            |
		 |9		     |1        |9.0            |
