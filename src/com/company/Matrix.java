package com.company;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;


public class Matrix {

    private int M;

    private int N;

    float[][] data;
    /**
     * 无参构造，这是一个形式方法，实际中不可能有0*0的空矩阵，注意避免空指针异常
     */
    public Matrix() {
        this.M = 0;
        this.N = 0;
        this.data = new float[M][N];
    }

    /**
     * 含维度构造
     * @param M 行
     * @param N 列
     */
    public Matrix(int M,int N) {
        this.M = M;
        this.N = N;
        this.data = new float[M][N];
        for(int i=0;i<M;i++) {
            for(int j=0;j<N;j++) {
                data[i][j]=0;
            }
        }
    }

    /**
     * 用二维浮点型数组来构造矩阵
     * @param data 浮点型数组
     */
    public Matrix(float[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new float[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    /**
     * 用二维双精度浮点型数组来构造矩阵
     * @param data 浮点型数组
     */
    public Matrix(double[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new float[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.data[i][j] = (float) data[i][j];
            }
        }
    }

    /**
     * 用二维数组来构造矩阵
     * @param data 整型数组
     */
    public Matrix(int[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new float[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    /**
     * 初始化矩阵为相同的数
     * @param M 行数
     * @param N 列数
     * @param allvalue 特定浮点型数值
     */
    public Matrix(int M,int N,float allvalue)
    {
        this.M = M;
        this.N = N;
        float[][]tmp = new float[M][N];
        for(int i=0;i<M;i++) {
            for(int j=0;j<N;j++) {
                tmp[i][j]=allvalue;
            }
        }
        data=tmp;
    }

    /**
     * 初始化矩阵为指定大小的单位矩阵
     * @param size 方阵大小
     */
    public Matrix (int size) {
        this.M = size;
        this.N = size;
        this.data = new float[M][N];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j)
                    this.data[i][j] = 1;
                else
                    this.data[i][j] = 0;
            }
        }
    }

    /**
     * 用二维双精度浮点型数组来为矩阵赋值
     * @param data 浮点型数组
     */
    public Matrix assign(double[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new float[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.data[i][j] = (float) data[i][j];
            }
        }
        return this;
    }
    /**
     * 初始化矩阵为相同的数
     * @param M 行数
     * @param N 列数
     * @param allvalue 特定整型数值
     */
    public Matrix(int M,int N,int allvalue)
    {
        this.M = M;
        this.N = N;
        float[][]tmp = new float[M][N];
        for(int i=0;i<M;i++) {
            for(int j=0;j<N;j++) {
                tmp[i][j]=allvalue;
            }
        }
        data=tmp;
    }

    /**
     * 返回矩阵行数
     * @return 行数
     */
    public int getRowNumber() {
        return this.M;
    }

    /**
     * 返回矩阵列数
     * @return 列数
     */
    public int getColoumNumber() {
        return this.N;
    }

    /**
     * 返回特定索引的元素值
     * @param Row 行索引
     * @param Coloum 列索引
     * @return 元素
     */
    public float getElement(int Row, int Coloum) {
        return this.data[Row][Coloum];
    }

    /**
     * 产生随机矩阵
     * @param Row 行索引
     * @param Coloum 列索引
     * @return 值
     */
    public static Matrix random(int Row, int Coloum) {
        Matrix A = new Matrix(Row, Coloum);
        for(int i = 0; i < Row; i++) {
            for(int j = 0; j < Coloum; j++) {
                A.data[i][j] = (float) Math.random();
            }
        }
        return A;
    }

    /**
     * 设定指定索引元素为特定值
     * @param Row 行索引
     * @param Coloum 列索引
     * @param e 值
     */
    public void setToSpecifiedValue(int Row, int Coloum, float e) {
        this.data[Row][Coloum] = e;
    }

    /**
     * 设定指定行所有元素为特定值
     * @param Row 行索引
     * @param e 值
     * @return 设定后的矩阵
     */
    public Matrix setRowToSpecifiedValue(int Row, float e) {
        for (int j = 0; j < N; j++) {
            this.data[Row][j] = e;
        }
        return this;
    }

    /**
     * 矩阵转置
     * @return 转置后的矩阵
     */
    public Matrix transpose() {
        Matrix A = new Matrix(N, M);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                A.data[j][i] = this.data[i][j];
            }
        }
        return A;
    }

    /**
     * 求开方
     * @return 矩阵
     */
    public Matrix sqrt() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.data[i][j] = (float) Math.sqrt(this.data[i][j]);
            }
        }
        return this;
    }

    /**
     * 求正切
     * @return 矩阵
     */
    public Matrix tan() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.data[i][j] = (float) Math.tan(this.data[i][j]);
            }
        }
        return this;
    }

    /**
     * 求正弦
     * @return 矩阵
     */
    public Matrix sin() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.data[i][j] = (float) Math.sin(this.data[i][j]);
            }
        }
        return this;
    }

    /**
     * 求余弦
     * @return 矩阵
     */
    public Matrix cos() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.data[i][j] = (float) Math.cos(this.data[i][j]);
            }
        }
        return this;
    }

    /**
     * 返回矩阵的e指数
     * @return 矩阵
     */
    public Matrix exp() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.data[i][j] = (float) Math.exp(this.data[i][j]);
            }
        }
        return this;
    }
    /**
     * 返回矩阵的log自然对数
     * @return 矩阵
     */
    public Matrix log() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.data[i][j] = (float) Math.log(this.data[i][j]);
            }
        }
        return this;
    }

    /**
     * 两个矩阵相加
     * @param B 指定矩阵
     * @return 原矩阵与指定矩阵的相加
     */
    public Matrix plus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) {
            throw new RuntimeException("两矩阵维度必须一致");
        }
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = A.data[i][j] + B.data[i][j];
            }
        }
        return C;
    }

    /**
     * 两个矩阵相减
     * @param B 指定矩阵
     * @return 原矩阵与指定矩阵的相减
     */
    public Matrix minus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) {
            throw new RuntimeException("两矩阵维度必须一致");
        }
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = A.data[i][j] - B.data[i][j];
            }
        }
        return C;
    }

    /**
     * 两个矩阵相乘
     * @param B 指定矩阵
     * @return 原矩阵与指定矩阵的相乘
     */
    public Matrix multiply(Matrix B) {
        Matrix A = this;
        if (A.N != B.M) {
            throw new RuntimeException("第一个矩阵的列数必须等于第二个矩阵的行数");
        }
        Matrix C = new Matrix(A.M, B.N);
        for (int i = 0; i < C.M; i++) {
            for (int j = 0; j < C.N; j++) {
                for (int k = 0; k < A.N; k++) {
                    C.data[i][j] = C.data[i][j] + A.data[i][k] * B.data[k][j];
                }
            }
        }
        return C;
    }

    /**
     * 两个矩阵相除
     * @param B 指定矩阵
     * @return 原矩阵与指定矩阵的相除
     */
    public Matrix divide(Matrix B) {
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (B.data[i][j]==0) {
                    throw new RuntimeException("被除数不能为0");
                }
                C.data[i][j] = data[i][j] / B.data[i][j];
            }
        }
        return C;
    }

    /**
     * 两个矩阵点乘
     * @param B 指定矩阵
     * @return 原矩阵与指定矩阵的点乘
     */
    public Matrix dot(Matrix B) {
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < C.M; i++) {
            for (int j = 0; j < C.N; j++) {
                C.data[i][j] = data[i][j] * B.data[i][j];
            }
        }
        return C;
    }

    /**
     * 矩阵自身点乘
     * @return 原矩阵与自己进行点乘
     */
    public Matrix dotBySelf() {
        return this.dot(this);
    }

    /**
     * 矩阵加上一个数
     * @param e 数
     * @return 结果矩阵
     */
    public Matrix plusNumber(float e) {
        Matrix C = new Matrix(M, N,e);
        return this.plus(C);
    }

    /**
     * 矩阵减去一个数
     * @param e 数
     * @return 结果矩阵
     */
    public Matrix minusNumber(float e) {
        Matrix C = new Matrix(M, N,e);
        return this.minus(C);
    }

    /**
     * 矩阵乘以一个数
     * @param e 数
     * @return 结果矩阵
     */
    public Matrix multiplyNumber(float e) {
        Matrix C = new Matrix(M, N,e);
        return this.multiply(C);
    }

    /**
     * 矩阵除以一个数
     * @param e 数
     * @return 结果矩阵
     */
    public Matrix divideNumber(float e) {
        Matrix C = new Matrix(M, N,e);
        return this.divide(C);
    }

    /**
     * 判断两个矩阵是否相等
     * @param t 待比较矩阵
     * @return 布尔矩阵
     */
    public boolean equalTo(Matrix t) {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (data[i][j] != t.data[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 打印到控制台
     */
    public void showToConsole() {
        for (float[] a : data) {
            for (float b : a) {
                System.out.printf("%9.4f ", b);
            }
            System.out.println();
        }
    }

    /**
     * 求出矩阵的秩(3*3或2*2)
     * @return 秩
     */
    public float det() {
        if ((data.length == 3 && data[0].length == 3)||(data.length == 2&& data[0].length == 2)) {
            if(data.length == 3 && data[0].length == 3) {
                float r = 0;
                r = data[0][0]*data[1][1]*data[2][2]+data[1][0]*data[2][1]*data[0][2]+data[0][1]*data[1][2]*data[2][0];
                r = r-data[0][2]*data[1][1]*data[2][0]-data[1][0]*data[2][2]*data[0][1]-data[0][0]*data[2][1]*data[1][2];
                return r;
            } else {
                float r = 0;
                r = data[0][0]*data[1][1]-data[0][1]*data[1][0];
                return r;
            }
        } else {
            throw new RuntimeException("矩阵的维度必须为3*3的或者为2*2的");
        }
    }

    /**
     * 对矩阵每个数取绝对值
     * @return 矩阵
     */
    public Matrix abs() {
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = Math.abs(data[i][j]) ;
            }
        }
        return C;
    }

    /**
     * 对矩阵的每个数向上取整
     * @return 矩阵
     */
    public Matrix ceil() {
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = (float) Math.ceil(data[i][j]) ;
            }
        }
        return C;
    }

    /**
     * 对矩阵的每个数四舍五入
     * @return 矩阵
     */
    public Matrix round() {
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = Math.round(data[i][j]) ;
            }
        }
        return C;
    }

    /**
     * 向下取整
     * @return 矩阵
     */
    public Matrix floor() {
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = (float) Math.floor(data[i][j]) ;
            }
        }
        return C;
    }

    /**
     * 化成数组
     * @return 二维数组
     */
    public float[][] toArray() {
        return this.data;
    }

    /**
     * 返回矩阵的每个元素的sigmoid()值，sigmoid(x)=1/(1+exp(-x))
     * @return sigmoid矩阵
     */
    public Matrix sigmoid() {
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = (float) (1/(1+Math.exp(-data[i][j])));
            }
        }
        return C;
    }

    /**
     * 随机打乱矩阵的元素位置
     * @return 随机矩阵
     */
    public Matrix shuffle() {
        Matrix C = new Matrix(M,N);
        Random random = new Random();
        boolean[][] flag = new boolean[M][N];
        int i=0;
        int j=0;
        while(true) {
            int row = random.nextInt(M);
            int col = random.nextInt(N);
            if(flag[row][col]==false) {
                C.data[i][j] = this.data[row][col];
                j++;
                flag[row][col]=true;
                if(j==N) {
                    j=0;
                    i++;
                }
                if(i==N&&j==0) {
                    break;
                }
            }
        }
        return C;
    }

    /**
     * 找出矩阵的最大值
     * @return 最大值
     */
    public float max() {
        float max = data[0][0];
        for(int i=0;i<M;i++) {
            for(int j=0;j<N;j++) {
                if(max<data[i][j]) {
                    max = data[i][j];
                }
            }
        }
        return max;
    }

    /**
     * 找出矩阵的最小值
     * @return 最小值
     */
    public float min() {
        float min = data[0][0];
        for(int i=0;i<M;i++) {
            for(int j=0;j<N;j++) {
                if(min>data[i][j]) {
                    min = data[i][j];
                }
            }
        }
        return min;
    }

    /**
     * 返回矩阵每一行的最大值
     * @return 最大值数组
     */
    public float[] maxOfEachRow() {
        float[] result = new float[M];
        float[][] tmp = data;
        for(int i=0;i<M;i++) {
            Arrays.parallelSort(tmp[i]);
            result[i] = tmp[i][N-1];
        }
        return result;
    }

    /**
     * 返回矩阵每一列的最大值
     * @return 最大值数组
     */
    public float[] maxOfEachColoum() {
        float[] result = new float[N];
        float[] col = new float[M];
        for(int i=0;i<N;i++) {
            for(int j=0;j<M;j++) {
                col[j] = data[j][i];
            }
            Arrays.parallelSort(col);
            result[i] = col[M-1];
        }
        return result;
    }

    /**
     * 返回矩阵每一行的最小值
     * @return 最小值数组
     */
    public float[] minOfEachRow() {
        float[] result = new float[M];
        float[][] tmp = data;
        for(int i=0;i<M;i++) {
            Arrays.parallelSort(tmp[i]);
            result[i] = tmp[i][0];
        }
        return result;
    }

    /**
     * 返回矩阵每一列的最小值
     * @return 最小值数组
     */
    public float[] minOfEachColoum() {
        float[] result = new float[N];
        float[] col = new float[M];
        for(int i=0;i<N;i++) {
            for(int j=0;j<M;j++) {
                col[j] = data[j][i];
            }
            Arrays.parallelSort(col);
            result[i] = col[0];
        }
        return result;
    }

    /**
     * 返回行向量的求和值
     * @return 和值
     */
    public float sum() {
        if(M!=1) {
            throw new RuntimeException("矩阵的维度必须为3*3的或者为2*2的");
        }
        float sum=0;
        for(int i=0;i<N;i++) {
            sum+=data[0][i];
        }
        return sum;
    }

    /**
     * 返回每一行的和
     * @return 和数组
     */
    public float[] sumOfEachRow() {
        if(M==1) {
            throw new RuntimeException("矩阵行数必须大于1");
        }
        float[] sum=new float[M];
        for(int i=0;i<M;i++) {
            float s=0;
            for(int j=0;j<N;j++) {
                s+=data[i][j];
            }
            sum[i]=s;
        }
        return sum;
    }

    /**
     * 返回每一列的和
     * @return 和数组
     */
    public float[] sumOfEachColoum() {
        if(N==1) {
            throw new RuntimeException("矩阵列数必须大于1");
        }
        float[] sum=new float[N];
        for(int i=0;i<N;i++) {
            float s=0;
            for(int j=0;j<M;j++) {
                s+=data[j][i];
            }
            sum[i]=s;
        }
        return sum;
    }
}
