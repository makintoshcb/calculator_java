package MVCCalculator.mvc;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class View {

    private Scanner scanner;
    private Controller controller;

    public void startProgram() {

        initializationScanner();
        initializationController();

        int userOption = 0;
        while (userOption != 3) {
            System.out.println("""
                    1. Ввести прошлые решения
                    2. Ввести новое уровнение
                    3. Выход
                    """);
            try {
                userOption = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Нужно ввести число!");
                scanner.nextLine();
                continue;
            }
            if (userOption == 1) {
                printHistory();
            } else if (userOption == 2) {
                calculatingAndWriteUserInput();
            } else if (userOption == 3) {
                System.out.println("Выключение...");
            } else {
                System.out.println("Вы должны ввести 1, 2 или 3");
            }
        }
    }

    private void calculatingAndWriteUserInput() {
        boolean isContinueWorking = true;
        scanner.nextLine();
        while (isContinueWorking) {
            System.out.println("Введите выражение для вычисления: ");

            String userInput = scanner.nextLine();

            controller = new Controller();
            double result = controller.getResult(userInput);
            System.out.println(result);

            System.out.println("""
                    Если хотите вычислить еще одно выражение введите - продолжить
                    Если НЕ хотите продолжать введите что угодно:
                    """);
            if (!scanner.nextLine().equals("продолжить")) {
                isContinueWorking = false;
            }
        }
    }

    private void printHistory() {
        List<String> history = controller.readHistoryCalculating();
        if (history.size() == 0) {
            System.out.println("\tНе было решено еще ни одно выражение");
            return;
        }
        Integer userCount = null;
        while (true) {
            System.out.println("Введите количество прошлых уровнений, которое хотите считать:");
            System.out.println("Максимальное количество: " + history.size());
            try {
                userCount = scanner.nextInt();
            } catch (NumberFormatException e) {
                System.out.println("Вы ввели НЕ число");
                continue;
            }
            if (userCount > history.size()) {
                System.out.println("Ваше число больше максимального!");
                userCount = null;
                continue;
            }
            break;
        }

        int size = history.size();
        for (int i = 1; i <= userCount; i++) {
            System.out.println(history.get(size - i));
        }
    }

    private void initializationScanner() {
        if (scanner != null) {
            scanner.close();
        }
        scanner = new Scanner(System.in);
    }

    private void initializationController() {
        if (controller == null) {
            controller = new Controller();
        }
    }

    public Controller getController() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }
}
