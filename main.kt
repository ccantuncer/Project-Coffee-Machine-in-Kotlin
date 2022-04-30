fun main() {
    val coffeeMachine = CoffeeMachine()
    coffeeMachine.start()
}

enum class State {
    CHOOSE_ACTION, BUY, FILL, WITHDRAW, SHOW_INFO, EXIT, ERROR
}

enum class Coffee(
    val water: Int,
    val milk: Int,
    val coffeeBeans: Int,
    val price: Int
    ) {
    ESPRESSO(250, 0, 16, 4),
    LATTE(350, 75, 20, 7),
    CAPPUCCINO(200, 100, 12, 6),
}

class CoffeeMachine(
    private var water: Int = 400,
    private var milk: Int = 540,
    private var coffeeBeans: Int = 120,
    private var disposableCups: Int = 9,
    private var money: Int = 550
) {
   private var currentState = State.CHOOSE_ACTION

    fun start() {
        while (currentState != State.EXIT) {
            when (currentState) {
                State.CHOOSE_ACTION -> this.chooseAction()
                State.BUY -> this.buy()
                State.FILL -> this.fill()
                State.SHOW_INFO -> this.showInfo()
                State.WITHDRAW -> this.withdraw()
                else -> {
                    println("Invalid Input")
                    this.currentState = State.CHOOSE_ACTION
                }
            }
        }
    }

    private fun chooseAction() {
        print("Write action (buy, fill, take, remaining, exit): ")
        val input = readLine()!!
        this.currentState = when (input) {
            "buy" -> State.BUY
            "fill" -> State.FILL
            "take" -> State.WITHDRAW
            "remaining" -> State.SHOW_INFO
            "exit" -> State.EXIT
            else -> State.ERROR
        }
    }

    private fun checkSupplies(coffee: Coffee): Boolean {
        if (this.water < coffee.water) {
            println("Sorry, not enough water!")
            return false
        }
        if (this.milk < coffee.milk) {
            println("Sorry, not enough milk!")
            return false
        }
        if (this.coffeeBeans < coffee.coffeeBeans) {
            println("Sorry, not enough coffee beans!")
            return false
        }
        if (this.disposableCups < 1) {
            println("Sorry, not enough disposable cups!")
            return false
        }

        println("I have enough resources, making you a coffee!")
        return true
    }

    private fun prepareCoffee(coffee: Coffee) {
        val isEnoughSupplies = checkSupplies(coffee)

        if (isEnoughSupplies) {
            this.water -= coffee.water
            this.milk -= coffee.milk
            this.coffeeBeans -= coffee.coffeeBeans
            this.disposableCups--
            this.money += coffee.price
        }
    }

    private fun buy() {
        print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
        when (readLine()!!) {
            "1" -> prepareCoffee(Coffee.ESPRESSO)
            "2" -> prepareCoffee(Coffee.LATTE)
            "3" -> prepareCoffee(Coffee.CAPPUCCINO)
            "back" -> {
                this.currentState = State.CHOOSE_ACTION
                return
            }
            else -> {
                println("Invalid input")
                return
            }
        }

        this.currentState = State.CHOOSE_ACTION
    }

    private fun fill() {
        print("Write how many ml of water do you want to add: ")
        this.water += readLine()!!.toInt()

        print("Write how many ml of milk do you want to add: ")
        this.milk += readLine()!!.toInt()

        print("Write how many grams of coffee beans do you want to add: ")
        this.coffeeBeans += readLine()!!.toInt()

        print("Write how many disposable cups of coffee do you want to add: ")
        this.disposableCups += readLine()!!.toInt()

        this.currentState = State.CHOOSE_ACTION
    }

    private fun showInfo() {
        println("The coffee machine has:")
        println("${this.water} of water")
        println("${this.milk} of milk")
        println("${this.coffeeBeans} of coffee beans")
        println("${this.disposableCups} of disposable cups")
        println("${this.money} of money")

        this.currentState = State.CHOOSE_ACTION
    }

    private fun withdraw() {
        println("I gave you \$${this.money}")
        this.money = 0

        this.currentState = State.CHOOSE_ACTION
    }
}
