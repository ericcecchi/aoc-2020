package days.Day3;

import days.Day;

import java.math.BigInteger;

class Slope {
    private final int right;
    private final int down;

    public Slope(int right, int down) {
        this.right = right;
        this.down = down;
    }

    public int getRight() {
        return right;
    }

    public int getDown() {
        return down;
    }
}

public class Day3 extends Day {
    public Day3() {
        inputFile = "days/Day3/input1.txt";
    }

    private int getTrees(String[] lines, int down, int right) {
        int trees = 0;
        for (int i = 0; i < lines.length / down; i++) {
            String line = lines[i * down];
            int lineLength = line.length();
            int rightPosition = right * i % lineLength;
            char point = line.charAt(rightPosition);
            System.out.print(point);
            if (point == '#') {
                trees++;
            }
        }
        System.out.printf("%nRight %d, down %d: %d trees%n", right, down, trees);

        return trees;
    }

    public void part1() {
        String input = this.getInput();
        String[] lines = input.split("\n");
        Slope slope = new Slope(3, 1);

        getTrees(lines, slope.getDown(), slope.getRight());
    }

    public void part2() {
        String input = this.getInput();
        String[] lines = input.split("\n");
        Slope[] slopes = {
                new Slope(1, 1),
                new Slope(3, 1),
                new Slope(5, 1),
                new Slope(7, 1),
                new Slope(1, 2),
        };
        BigInteger treeProduct = BigInteger.ONE;

        for (Slope slope : slopes) {
            int trees = getTrees(lines, slope.getDown(), slope.getRight());
            treeProduct = treeProduct.multiply(BigInteger.valueOf(trees));
        }

        System.out.printf("%nTree product: %d trees%n", treeProduct);
    }
}
