package machine

class CoffeeMachine(water: Int, milk: Int, coffeeBeans: Int, disposableCups: Int, money: Int) {
    private var waterAvailable = water
    private var milkAvailable = milk
    private var coffeeBeansAvailable = coffeeBeans
    private var disposableCupsAvailable = disposableCups
    private var moneyAvailable = money

    private var currentState = CoffeeMachineState.CHOOSING_ACTION

    enum class CoffeeType(val requestedWater: Int, val requestedMilk: Int, val requestedCoffeeBeans: Int, val moneyAdded: Int) {
        ESPRESSO(250, 0, 16, 4),
        LATTE(350, 75, 20, 7),
        CAPPUCCINO(200, 100, 12, 6),
        NULL(0,0,0,0)
    }

    enum class CoffeeMachineState {
        CHOOSING_ACTION,
        CHOOSING_TYPE_OF_COFFEE,
        FILLING_WATER,
        FILLING_MILK,
        FILLING_COFFEE_BEANS,
        FILLING_DISPOSABLE_CUPS
    }

    init {
        printActionMenu()
    }

    private fun printActionMenu() {
        println("\nWrite action (buy, fill, take, remaining, exit):")
    }

    private fun printChooseCoffeeMenu() {
        println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
    }

    fun inputInterface(inputString: String) {
        when (currentState) {
            CoffeeMachineState.CHOOSING_ACTION -> {
                when (inputString.lowercase()) {
                    "buy" -> {
                        printChooseCoffeeMenu()
                        currentState = CoffeeMachineState.CHOOSING_TYPE_OF_COFFEE
                    }
                    "fill" -> {
                        println("Write how many ml of water you want to add:")
                        currentState = CoffeeMachineState.FILLING_WATER
                    }
                    "take" -> {
                        takeOperation()
                        printActionMenu()
                    }
                    "remaining" -> {
                        displayStocks()
                        printActionMenu()
                    }
                    else -> {
                        println("Invalid input for action.")
                        printActionMenu()
                    }
                }
            }
            CoffeeMachineState.CHOOSING_TYPE_OF_COFFEE -> {
                buyOperation(inputString.lowercase())
                printActionMenu()
                currentState = CoffeeMachineState.CHOOSING_ACTION
            }
            CoffeeMachineState.FILLING_WATER -> {
                fillWater(inputString.lowercase())
                println("Write how many ml of milk you want to add:")
                currentState = CoffeeMachineState.FILLING_MILK
            }
            CoffeeMachineState.FILLING_MILK -> {
                fillMilk(inputString.lowercase())
                println("Write how many grams of coffee beans you want to add:")
                currentState = CoffeeMachineState.FILLING_COFFEE_BEANS
            }
            CoffeeMachineState.FILLING_COFFEE_BEANS -> {
                fillBeans(inputString.lowercase())
                println("Write how many disposable cups you want to add:")
                currentState = CoffeeMachineState.FILLING_DISPOSABLE_CUPS
            }
            CoffeeMachineState.FILLING_DISPOSABLE_CUPS -> {
                fillCups(inputString.lowercase())
                printActionMenu()
                currentState = CoffeeMachineState.CHOOSING_ACTION
            }
        }
    }

    private fun buyOperation(inputString: String) {
        val chosenCoffee = when (inputString) {
            "1" -> CoffeeType.ESPRESSO
            "2" -> CoffeeType.LATTE
            "3" -> CoffeeType.CAPPUCCINO
            "back" -> return
            else -> CoffeeType.NULL
        }
        if (chosenCoffee != CoffeeType.NULL) {
            if (waterAvailable >= chosenCoffee.requestedWater) {
                if (milkAvailable >= chosenCoffee.requestedMilk) {
                    if (coffeeBeansAvailable >= chosenCoffee.requestedCoffeeBeans) {
                        if (disposableCupsAvailable >= 1) {
                            // Actual buying
                            println("I have enough resources, making you a coffee!")
                            waterAvailable -= chosenCoffee.requestedWater
                            milkAvailable -= chosenCoffee.requestedMilk
                            coffeeBeansAvailable -= chosenCoffee.requestedCoffeeBeans
                            disposableCupsAvailable -= 1
                            moneyAvailable += chosenCoffee.moneyAdded
                        } else println("Sorry, not enough disposable cups")
                    } else println("Sorry, not enough coffee beans!")
                } else println("Sorry, not enough milk!")
            } else println("Sorry, not enough water!")
        } else println("Sorry, not valid input for choosing coffee.")
    }

    private fun fillWater(inputString: String) {
        waterAvailable += inputString.toInt()
    }

    private fun fillMilk(inputString: String) {
        milkAvailable += inputString.toInt()
    }

    private fun fillBeans(inputString: String) {
        coffeeBeansAvailable += inputString.toInt()
    }

    private fun fillCups(inputString: String) {
        disposableCupsAvailable += inputString.toInt()
    }

    private fun takeOperation() {
        println("I gave you \$$moneyAvailable")
        moneyAvailable = 0
    }

    private fun displayStocks() {
        println()
        println("""
            The coffee machine has:
            $waterAvailable ml of water
            $milkAvailable ml of milk
            $coffeeBeansAvailable g of coffee beans
            $disposableCupsAvailable disposable cups
            ${'$'}$moneyAvailable of money
        """.trimIndent())
    }
}

fun main() {
    val coffeeMachine = CoffeeMachine(400, 540, 120, 9, 550)
    while (true) {
        val userInput = readln()
        if (userInput.lowercase() == "exit") break
        else coffeeMachine.inputInterface(userInput)
    }

}
