package MVCCalculator.mvc;

import java.util.List;

public class Model {

    Controller controller = new View().getController();

    public double calculate(List<String> parsedUserInput) {
        while (isContainsBracers(parsedUserInput, 0, parsedUserInput.size())) {
            int[] index = getIndexBraces(parsedUserInput, 0, parsedUserInput.size());
            calculateInBetween(parsedUserInput, index[0], index[1]);
        }
        Double result = calculateWithoutBracers(parsedUserInput);
        return result;
    }

    private double calculateWithoutBracers(List<String> parsedUserInput) {
        double resultCalculate = 0;
        int start = 0;
        int end = parsedUserInput.size();
        while (parsedUserInput.size() > 1) {
            int indexMostPriority = searchIndexMostPriorityInBetween(parsedUserInput, start, end);
            resultCalculate = calculateTwoDigit(parsedUserInput, indexMostPriority);
            parsedUserInput.set(indexMostPriority, String.valueOf(resultCalculate));
            parsedUserInput.remove(indexMostPriority - 1);
            end--;
            parsedUserInput.remove(indexMostPriority);
            end--;
        }
        return resultCalculate;
    }

    public void calculateInBetween(List<String> parsedUserInput, int start, int end) {
        while (isContainsOperatorInBetween(parsedUserInput, start, end)) {
            int indexMostPriority = searchIndexMostPriorityInBetween(parsedUserInput, start, end);
            double resultCalculate = calculateTwoDigit(parsedUserInput, indexMostPriority);
            parsedUserInput.set(indexMostPriority, String.valueOf(resultCalculate));
            parsedUserInput.remove(indexMostPriority - 1);
            end--;
            parsedUserInput.remove(indexMostPriority);
            end--;
        }
        parsedUserInput.remove(start);
        parsedUserInput.remove(end - 1);
    }

    private boolean isContainsBracers(List<String> parsedUserInput, int start, int end) {
        for (int i = start; i < end; i++) {
            String s = parsedUserInput.get(i);
            if (s.equals("(") || s.equals(")")) {
                return true;
            }
        }
        return false;
    }

    private boolean isContainsOperatorInBetween(List<String> parsedUserInput, int start, int end) {
        for (int i = start + 1; i < end; i++) {
            String s = parsedUserInput.get(i);
            if (s.equals("-") || s.equals("+") || s.equals("*") || s.equals("/") || s.equals("%") || s.equals("^") || s.equals("p")) {
                return true;
            }
        }
        return false;
    }

    private double calculateTwoDigit(List<String> parsedUserInput, int indexOperator) {
        String operator = parsedUserInput.get(indexOperator);
        double firstDigit = Double.parseDouble(parsedUserInput.get(indexOperator - 1));
        double secondDigit = Double.parseDouble(parsedUserInput.get(indexOperator + 1));
        return switch (operator) {
            case "-" -> firstDigit - secondDigit;
            case "+" -> firstDigit + secondDigit;
            case "*" -> firstDigit * secondDigit;
            case "/", "//" -> firstDigit / secondDigit;
            case "%" -> firstDigit % secondDigit;
            default -> 0;
        };
    }

    private int[] getIndexBraces(List<String> parsedUserInput, int start, int end) {
        int[] index = {-1, -1};
        if (end >= parsedUserInput.size()) {
            end = parsedUserInput.size() - 1;
        }
        for (int i = start; i <= end; i++) {
            if (parsedUserInput.get(i).equals("(")) {
                index[0] = i;
                break;
            }
        }
        for (int i = index[0]; i <= end; i++) {
            if (parsedUserInput.get(i).equals(")")) {
                index[1] = i;
            }
        }
        if (isContainsBracers(parsedUserInput, (index[0] + 1), (index[1] - 1))) {
            index = getIndexBraces(parsedUserInput, (index[0] + 1), (index[1] - 1));
        }
        return index;
    }

    private int searchIndexMostPriorityInBetween(List<String> parsedUserInput, int start, int end) {
        int indexMostPriority = 0;
        int maxPriority = 0;
        for (int i = start; i < end; i++) {
            String s = parsedUserInput.get(i);
            if (controller.isOperator(s)) {
                int tempPriority = getPriorityOperator(s);
                if (tempPriority > maxPriority) {
                    maxPriority = tempPriority;
                    indexMostPriority = i;
                }
            }
        }
        return indexMostPriority;
    }

    private int getPriorityOperator(String operator) {
        if ("^p".contains(operator)) {
            return 3;
        } else if ("*//%".contains(operator)) {
            return 2;
        } else if ("+-".contains(operator)) {
            return 1;
        }
        return 0;
    }
}
