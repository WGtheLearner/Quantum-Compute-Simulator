package com.company;

import java.util.Scanner;

public class Complex {
    double real;  // 实部
    double image; // 虚部

    Complex(){  // 不带参数的构造方法
        Scanner input = new Scanner(System.in);
        double real = input.nextDouble();
        double image = input.nextDouble();
        Complex(real,image);
    }

    private void Complex(double real, double image) { // 供不带参数的构造方法调用
        this.real = real;
        this.image = image;
    }

    Complex(double real,double image){ // 带参数的构造方法
        this.real = real;
        this.image = image;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImage() {
        return image;
    }

    public void setImage(double image) {
        this.image = image;
    }

    /**
     * 复数相加
     * @param a 被加数
     * @return 值
     */
    public Complex add(Complex a){
        double real2 = a.getReal();
        double image2 = a.getImage();
        double newReal = real + real2;
        double newImage = image + image2;
        Complex result = new Complex(newReal,newImage);
        return result;
    }

    /**
     * 复数相减
     * @param a 减数
     * @return 值
     */
    public Complex sub(Complex a){
        double real2 = a.getReal();
        double image2 = a.getImage();
        double newReal = real - real2;
        double newImage = image - image2;
        Complex result = new Complex(newReal,newImage);
        return result;
    }

    /**
     * 复数相乘
     * @param a 乘数
     * @return 值
     */
    public Complex mul(Complex a){
        double real2 = a.getReal();
        double image2 = a.getImage();
        double newReal = real*real2 - image*image2;
        double newImage = image*real2 + real*image2;
        Complex result = new Complex(newReal,newImage);
        return result;
    }

    /**
     * 复数相除
     * @param a 除数
     * @return 值
     */
    public Complex div(Complex a){
        double real2 = a.getReal();
        double image2 = a.getImage();
        double newReal = (real*real2 + image*image2)/(real2*real2 + image2*image2);
        double newImage = (image*real2 - real*image2)/(real2*real2 + image2*image2);
        Complex result = new Complex(newReal,newImage);
        return result;
    }

    /**
     * 复数赋值
     * @param real 实部
     * @param image 虚部
     * @return 值
     */
    public Complex assign(double real, double image){
        this.real = real;
        this.image = image;
        return this;
    }

    public void print(){
        if(image > 0 && real != 0){
            System.out.print(String.format("%.5f", real) + " + " + String.format("%.5f", image) + "i");
        }
        else if(image < 0 && real != 0){
            System.out.print(String.format("%.5f", real) + "" + String.format("%.5f", image) + "i");
        }
        else if(real == 0) {
            System.out.print(String.format("%.5f", image) + "i");
        }
        else{
            System.out.print(String.format("%.5f", real));
        }
    }
}
