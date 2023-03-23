package MVCCalculator.mvc;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {

    Model model;

    public Double getResult(String userInput) {

        initializationModel();

        List<String> parsedUserInput = parse(userInput);
        double result = model.calculate(parsedUserInput);

        writeResultCalculating(userInput, Double.toString(result));

        return result;
    }

    public List<String> parse(String userInput) {
        List<String> parsedInput = new ArrayList<>();
        String[] splitUserInput = userInput.split("");   //разбиение выражения на отдельные СИМВОЛЫ

        StringBuilder digit = new StringBuilder();
        for (String letter : splitUserInput) {
            //проверка на операторы и скобки
            if (isOperator(letter) || letter.equals("(") || letter.equals(")")) {
                //сложение цифр в число
                if (!digit.toString().equals("")) {
                    parsedInput.add(digit.toString());
                    digit = new StringBuilder();
                }

                //для оператора "//"
                if (letter.equals("/") && parsedInput.get(parsedInput.size() - 1).equals("/")) {
                    parsedInput.remove(parsedInput.size() - 1);
                    parsedInput.add("//");
                }
                //для унарных минусов
                else if (letter.equals("-") && (parsedInput.isEmpty() || parsedInput.get(parsedInput.size() - 1).equals("("))) {
                    digit.append(letter);
                }
                //для процентов (p) --экспериментальная функция
                else if (letter.equals("p")) {
                    //Преобразование выражения типа A+B% -> A+A%B
                    String tmp = parsedInput.get(parsedInput.size() - 1);
                    parsedInput.remove(parsedInput.size() - 1);
                    if (parsedInput.get(parsedInput.size() - 2).equals(")")) {  //Если A - число
                        parsedInput.add("&");
                        parsedInput.add(letter);
                        parsedInput.add(tmp);
                    } else {                                                                //Если A - выражение
                        parsedInput.add(parsedInput.get(parsedInput.size() - 2));
                        parsedInput.add(letter);
                        parsedInput.add(tmp);
                    }
                }

                //для остальных операторов
                else {
                    parsedInput.add(letter);
                }
            }
            //проверка на цифры
            else if (Character.isDigit(letter.charAt(0)) || letter.equals(".")) {
                digit.append(letter);
            }
        }

        //для последней цифры в выражении
        if (!digit.toString().equals("")) {
            parsedInput.add(digit.toString());
        }

        return parsedInput;
    }

    public boolean isOperator(String operator) {
        String operators = "+-*/^//%p";
        return operators.contains(operator);
    }

    public void writeResultCalculating(String userInput, String result) {
        try (
                FileWriter writer = new FileWriter(System.getProperty("user.dir") + "/container.txt", true);
        ) {
            String outputStr = userInput + "=" + result + "\n";
            writer.write(outputStr);
        } catch (IOException e) {
            System.out.println("ОШИБКА!!!");
            System.out.println("Не удалось записать значение в файл! " + e);
        }
    }

    public List<String> readHistoryCalculating() {
        ArrayList<String> historyCalculating = new ArrayList<>();
        try (
                FileReader reader = new FileReader(System.getProperty("user.dir") + "/container.txt");
        ) {
            Scanner scannerReader = new Scanner(reader);
            while (scannerReader.hasNextLine()) {
                historyCalculating.add((scannerReader.nextLine()));
            }
        } catch (IOException e) {
            System.out.println("ОШИБКА!!!");
            System.out.println("Файл не найден или его не существует! " + e);
        }
        return historyCalculating;
    }

    private void initializationModel() {
        if (model == null) {
            model = new Model();
        }
    }
}
