package company

open class Employee(val salary: Int = 10000, val allowance: Double= salary * 0.2)


class Admin:Employee(salary = 25000, allowance = 100.00)

class Other:Employee()


fun main() {
val lina = Employee()
    val rene = Admin()
    val cythia = Other()





}