package calculator

fun main(){
    //welcome user
    println("welcome to this super calculator")
    //ask user for input
    print("enter your fist number: ")
    val num1 = readLine()!!.toInt()
    print("enter your second number: ")
    val num2 = readLine()!!. toInt()

    //print what the user entered
    //println ("you entered $num1 and $num2")
    println("sum of $num1 & $num2 = ${summationOf(num1, num2)}")
    println("sutraction of $num1 & $num2 = ${subtractionOf(num1, num2)}")
    println("division of $num1 & $num2 = ${divisionOf(num1, num2)}")
    println("multiplication of $num1 & $num2 = ${multiplicationOf(num1, num2)}")
    println("reminder of $num1 & $num2 = ${reminderOf(num1, num2)}")


}
//return the sum of two numbers
fun summationOf(num1: Int, num2: Int): Int = num1 + num2

//Returns the difference of two numbers
fun subtractionOf(num1: Int, num2: Int): Int = num1 - num2

//returns the outcome of the division of two numbers
fun divisionOf(num1: Int, num2: Int): Int = num1 / num2

//Returns the outcome of the multiplication of two numbers
fun multiplicationOf(num1: Int, num2: Int): Int = num1 * num2

//Returns t the reminder of dividing the first number by the second number
fun reminderOf(num1: Int, num2: Int): Int = num1 % num2

