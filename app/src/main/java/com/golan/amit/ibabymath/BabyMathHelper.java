package com.golan.amit.ibabymath;

public class BabyMathHelper {

    public static final int NUMBER_LIMIT = 5;
    public static final int ADD = 0;
    public static final int MULT = 1;
    public static final int SUBTRACT = 2;
    public static final int DIVIDE = 3;

    public static final int BOOTSBANANA = 0;
    public static final int OLAFCARROT = 1;
    public static final int LIONSTRAWBERRY = 2;
    public static final int BALOONS = 3;
    public static final int HONEYPOOH = 4;
    public static final int MELAFEFON = 5;
    public static final int TOMATO = 6;
    public static final int CAKE = 7;
    public static final int PARPARFLOWER = 8;
    public static final int CHEESE = 9;
    public static final int GRAPESBALOO = 10;


    private int left;
    private int right;

    private int operation_ptr;
    private int pic_ptr;

    private int fail_counter;

    /**
     * Constructor
     */

    public BabyMathHelper() {
        this.operation_ptr = ADD;
        this.pic_ptr = BOOTSBANANA;
        this.fail_counter = 0;
    }

    public void generate_random_operands() {
        this.left = (int) (Math.random() * NUMBER_LIMIT + 1);
        this.right = (int) (Math.random() * NUMBER_LIMIT + 1);
        if (getOperation_ptr() == SUBTRACT) {
            this.right = (int) (Math.random() * 2 + 1);
            this.left = (int) (Math.random() * 3 + 1) + this.right;
        } else if (getOperation_ptr() == DIVIDE) {
            this.right = (int) (Math.random() * 2 + 1);
            this.left = ((int) (Math.random() * 2 + 1) * this.right);
        }
    }


    /**
     * Getters & Setters
     * @return
     */

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int result() {
        if (operation_ptr == ADD) {
            return left + right;
        } else if (operation_ptr == MULT) {
            return left * right;
        } else if (operation_ptr == SUBTRACT) {
            return left - right;
        } else {
            return left / right;
        }
    }

    public int getOperation_ptr() {
        return operation_ptr;
    }

    public void setOperation_ptr(int operation_ptr) {
        this.operation_ptr = operation_ptr;
    }

    public void generate_random_operation_ptr() {
        this.operation_ptr = (int)(Math.random() * 4);
    }

    public int getPic_ptr() {
        return pic_ptr;
    }

    public void generate_randor_pic_pointer() {
        this.pic_ptr = (int)(Math.random() * 11);
    }

    public int getFail_counter() {
        return fail_counter;
    }

    public void setFail_counter(int fail_counter) {
        this.fail_counter = fail_counter;
    }

    public void reset_fail_counter() {
        this.fail_counter = 0;
    }

    public void increase_fail_counter() {
        this.fail_counter++;
    }
}
