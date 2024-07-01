var customers = 10


fun main(){
    val h: String = "Katz" //val is a read only variable, var is mutable
    customers += 3
    customers -= 4
    println(h)
    println("es gibt ${customers+1} im laden")
}
