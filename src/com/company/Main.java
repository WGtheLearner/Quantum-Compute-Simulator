package com.company;
import java.io.*;
import java.util.Arrays;

public class Main {

    /**
     * 矩阵克罗内克积
     * @param A 第一个矩阵
     * @param B 第二个矩阵
     */
    public static double[][] Kronecker(Matrix A, Matrix B)
    {
        int rowa = A.getRowNumber();
        int cola = A.getColoumNumber();
        int rowb = B.getRowNumber();
        int colb = B.getColoumNumber();
        double[][] C= new double[rowa * rowb][cola * colb];
        // i loops till rowa
        for (int i = 0; i < rowa; i++)
        {
            // k loops till cola
            for (int k = 0; k < cola; k++)
            {
                // j loops till rowb
                for (int j = 0; j < rowb; j++) {
                    // l loops till colb
                    for (int l = 0; l < colb; l++) {
                        // Each element of matrix A is
                        // multiplied by whole Matrix B
                        // resp and stored as Matrix C
                        C[rowb*i+j][colb*k+l] = A.data[i][k] * B.data[j][l];
                        //System.out.print(String.format("%.5f",C[rowb*i+j][colb*k+l])+" ");
                    }
                }
                //System.out.println();
            }
        }
        return C;
    }

    /**
     * 将一个int数字转换为二进制的字符串形式。
     * @param num 需要转换的int类型数据
     * @param digits 要转换的二进制位数，位数不足则在前面补0
     * @return 二进制的字符串形式
     */
    public static String toBinary(int num, int digits) {
        int value = 1 << digits | num;
        String bs = Integer.toBinaryString(value); //0x20 | 这个是为了保证这个string长度是6位数
        return bs.substring(1);
    }

    public static void main(String[] args) {
        String s = " ";
        String[] qubit_n = new String[64];
        double[] Simulator_State = new double[1024];    //模拟器内部状态
        Simulator_State[0] = 1;

        double[][] phase = new double[64][3];
        int qubit_counts = 0;
        Complex[][] phase1 = new Complex[64][3];

        int circuit_state = 1;
        int partial = 0;
        int Y_counts = 0;   //the number of Y-Gates

        double[][] Pauli_H = {{0.70711,0.70711},{0.70711,-0.70711}};
        double[][] Pauli_X = {{0,1},{1,0}};
        double[][] Pauli_Y = {{0,-1},{1,0}};
        double[][] Pauli_Z = {{1,0},{0,-1}};
        double[][] Identity = {{1,0},{0,1}};
        double[][] CNOT = {{1,0,0,0},{0,1,0,0},{0,0,0,1},{0,0,1,0}};
        double[][] SWAP = {{1,0,0,0},{0,0,1,0},{0,1,0,1},{0,0,0,1}};
        double[][] qubit_state_0 = {{1,0},{0,0}};
        double[][] qubit_state_1 = {{0,0},{0,1}};

        Matrix h = new Matrix(Pauli_H);
        Matrix x = new Matrix(Pauli_X);
        Matrix y = new Matrix(Pauli_Y);
        Matrix z = new Matrix(Pauli_Z);
        Matrix I = new Matrix(Identity);
        Matrix cnot = new Matrix(CNOT);
        Matrix swap = new Matrix(SWAP);
        Matrix Qstate_0 = new Matrix(qubit_state_0);
        Matrix Qstate_1 = new Matrix(qubit_state_1);
        //Kronecker(h, z);
        //phase1[0][0] = new Complex(1,0);
        //phase1[0][0].print();

        File file =new File("Instructions.txt");
        try
        {
            //if file doesnt exists, then create it
            if(!file.exists()){
                file.createNewFile();
            }

            //true = append file
            FileWriter fileWritter = new FileWriter(file);
            fileWritter.write("");
            fileWritter.flush();
            fileWritter.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Welcome to Quantum Computing Simulator");
        System.out.print("qcs>");

        while(!s.equals("exit"))
        {
            try
            {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
                s = buffer.readLine();
            }

            catch(Exception e)
            {
                e.printStackTrace();
            }

            if (s.startsWith("H"))      //Hamard Gate
            {
                int a = s.indexOf("(");
                int b = s.indexOf(")");
                String target = " ";
                target = s.substring(a+1, b);
                int size1 = 0;
                int size2 = 0;
                for (int i = 0; i < 64; i++)
                {
                    if (target.equals(qubit_n[i]))
                    {
                        size1 = (int) Math.pow(2, (i));
                        size2 = (int) Math.pow(2, (qubit_counts - i - 1));
                        Matrix I_1 = new Matrix(size1);
                        Matrix I_2 = new Matrix(size2);

                        double[][] Temp_1 = Kronecker(I_1, h);
                        Matrix CNOT_1 = new Matrix(Temp_1);
                        double[][] Temp_2 = Kronecker(CNOT_1, I_2);
                        Matrix Y = new Matrix(Temp_2);

                        //对模拟器量子空间构成的列向量，进行已有量子门的矩阵等效运算
                        double[][] simulator_M = new double[Temp_2.length][1];
                        for (int k = 0; k < Temp_2.length; k++) {
                            simulator_M[k][0] = Simulator_State[k];
                        }
                        Matrix Global_Simulator = new Matrix(simulator_M);
                        Global_Simulator = Y.multiply(Global_Simulator);
                        //Complex[] Y_temp = new Complex[Temp_2.length];

                        for (int k = 0; k < Temp_2.length; k++) {     //更新模拟器量子空间状态
                            Simulator_State[k] = Global_Simulator.data[k][0];
                        }
                        System.out.print("simulate result: ");
                        for (int l = 0; l < Temp_2.length - 1; l++) {
                            System.out.print(String.format("%.5f", Global_Simulator.data[l][0]) + "|" + toBinary(l, qubit_counts) + "> + ");
                        }
                        System.out.println(String.format("%.5f", Global_Simulator.data[Temp_2.length - 1][0]) + "|" + toBinary(Temp_2.length - 1, qubit_counts) + ">");

                        double temp = phase[i][1];
                        phase[i][1] = Math.sqrt(2)/2 * (phase[i][1] + phase[i][2]);
                        phase[i][2] = Math.sqrt(2)/2 * (temp - phase[i][2]);

                        try
                        {
                            String data = "H " + i + "\n";
                            //true = append file
                            FileWriter fileWritter = new FileWriter(file.getName(),true);
                            fileWritter.write(data);
                            fileWritter.close();
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        if (circuit_state == 1)
                        {
                            String[] para = new String[]{"python", "D:\\Java\\QC_Simulator\\src\\com\\company\\quantum_circuit.py",
                                    String.valueOf(qubit_counts)};
                            PythonInvoke.invokePy(para);
                        }
                        break;
                    }
                    else if(target != qubit_n[i] && qubit_n[i] == null)
                    {
                        System.out.println("Error: unknown variable or constant "+target);
                        break;
                    }
                }
            }

            else if (s.startsWith("Cnot"))
            {
                int Con_Q = -1, underCon_Q = -1;
                int flag = 2;
                int a = s.indexOf("(");
                int b = s.indexOf(")");
                String target = " ";
                target = s.substring(a+1, b);
                String[] token_set = target.split(",");
                int size1 = 0;
                int size2 = 0;
                int size3 = 0;
                int size4 = 0;
                for (int i = 0, j = 0; i < 64 && j < 64; i++, j++)
                {
                    if (flag == 0)
                    {
                        if (Con_Q < underCon_Q)        //将CNOT门拆成Con_Q = 0的矩阵和Con_Q = 1的矩阵叠加
                        {
                            size1 = (int) Math.pow(2, (Con_Q));
                            size2 = (int) Math.pow(2, (underCon_Q - Con_Q - 1));
                            size3 = (int) Math.pow(2, (qubit_counts - underCon_Q - 1));
                            size4 = (int) Math.pow(2, (qubit_counts - Con_Q - 1));
                            Matrix I_1 = new Matrix(size1);
                            Matrix I_2 = new Matrix(size2);
                            Matrix I_3 = new Matrix(size3);
                            Matrix I_4 = new Matrix(size4);

                            double[][] Temp_1 = Kronecker(I_1, Qstate_0);
                            Matrix CNOT_1 = new Matrix(Temp_1);
                            double[][] Temp_2 = Kronecker(CNOT_1, I_4);
                            Matrix CNOT_I = new Matrix(Temp_2);

                            Temp_1 = Kronecker(I_1, Qstate_1);
                            CNOT_1 = CNOT_1.assign(Temp_1);
                            double[][] Temp_3 = Kronecker(CNOT_1, I_2);
                            Matrix CNOT_2 = new Matrix(Temp_3);
                            double[][] Temp_4 = Kronecker(CNOT_2, x);
                            Matrix CNOT_3 = new Matrix(Temp_4);
                            double[][] Temp_5 = Kronecker(CNOT_3, I_3);
                            Matrix CNOT_X = new Matrix(Temp_5);
                            //for (int m=0;m<4;m++)
                            //    System.out.println (Arrays.toString (Temp_5[m]));
                            CNOT_X = CNOT_I.plus(CNOT_X);

                            //对模拟器量子空间构成的列向量，进行已有量子门的矩阵等效运算
                            double[][] simulator_M = new double[Temp_2.length][1];
                            for (int k = 0; k < Temp_2.length; k++) {
                                simulator_M[k][0] = Simulator_State[k];
                            }
                            Matrix Global_Simulator = new Matrix(simulator_M);
                            Global_Simulator = CNOT_X.multiply(Global_Simulator);
                            for (int k = 0; k < Temp_2.length; k++) {     //更新模拟器量子空间状态
                                Simulator_State[k] = Global_Simulator.data[k][0];
                            }
                            System.out.print("simulate result: ");
                            for (int l = 0; l < Temp_2.length - 1; l++) {
                                System.out.print(String.format("%.5f", Global_Simulator.data[j][0]) + "|" + toBinary(l, qubit_counts) + "> + ");
                            }
                            System.out.println(String.format("%.5f", Global_Simulator.data[Temp_2.length - 1][0]) + "|" + toBinary(Temp_2.length - 1, qubit_counts) + ">");
                        }
                        else if (Con_Q > underCon_Q)        //将CNOT门拆成Con_Q = 0的矩阵和Con_Q = 1的矩阵叠加
                        {
                            size1 = (int) Math.pow(2, (Con_Q));     // I*Con_Q*I
                            size2 = (int) Math.pow(2, (Con_Q - underCon_Q - 1));
                            size3 = (int) Math.pow(2, (underCon_Q));     //
                            size4 = (int) Math.pow(2, (qubit_counts - Con_Q - 1));
                            Matrix I_1 = new Matrix(size1);
                            Matrix I_2 = new Matrix(size2);
                            Matrix I_3 = new Matrix(size3);
                            Matrix I_4 = new Matrix(size4);

                            double[][] Temp_1 = Kronecker(I_1, Qstate_0);
                            Matrix CNOT_1 = new Matrix(Temp_1);
                            double[][] Temp_2 = Kronecker(CNOT_1, I_4);
                            Matrix CNOT_I = new Matrix(Temp_2);

                            Temp_1 = Kronecker(I_3, x);
                            CNOT_1 = CNOT_1.assign(Temp_1);
                            double[][] Temp_3 = Kronecker(CNOT_1, I_2);
                            Matrix CNOT_2 = new Matrix(Temp_3);
                            double[][] Temp_4 = Kronecker(CNOT_2, Qstate_1);
                            Matrix CNOT_3 = new Matrix(Temp_4);
                            double[][] Temp_5 = Kronecker(CNOT_3, I_3);
                            Matrix CNOT_X = new Matrix(Temp_5);
                            CNOT_X = CNOT_I.plus(CNOT_X);

                            //对模拟器量子空间构成的列向量，进行已有量子门的矩阵等效运算
                            double[][] simulator_M = new double[Temp_2.length][1];
                            for (int k = 0; k < Temp_2.length; k++) {
                                simulator_M[k][0] = Simulator_State[k];
                            }
                            Matrix Global_Simulator = new Matrix(simulator_M);
                            Global_Simulator = CNOT_X.multiply(Global_Simulator);
                            for (int k = 0; k < Temp_2.length; k++) {     //更新模拟器量子空间状态
                                Simulator_State[k] = Global_Simulator.data[k][0];
                            }
                            System.out.print("simulate result: ");
                            for (int l = 0; l < Temp_2.length - 1; l++) {
                                System.out.print(String.format("%.5f", Global_Simulator.data[j][0]) + "|" + toBinary(l, qubit_counts) + "> + ");
                            }
                            System.out.println(String.format("%.5f", Global_Simulator.data[Temp_2.length - 1][0]) + "|" + toBinary(Temp_2.length - 1, qubit_counts) + ">");
                        }

                        try
                        {
                            String data = "CX " + Con_Q + " " + underCon_Q + "\n";
                            //true = append file
                            FileWriter fileWritter = new FileWriter(file.getName(),true);
                            fileWritter.write(data);
                            fileWritter.close();

                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        if (circuit_state == 1) {
                            String[] para = new String[]{"python", "D:\\Java\\QC_Simulator\\src\\com\\company\\quantum_circuit.py",
                                    String.valueOf(qubit_counts)};
                            PythonInvoke.invokePy(para);
                        }
                        break;
                    }

                    if (token_set[0].equals(qubit_n[i]))    //controlling qubit
                    {
                        flag--;
                        Con_Q = i;
                    }
                    else if (token_set[1].equals(qubit_n[j]))   //under-controlled qubit
                    {
                        flag--;
                        underCon_Q = j;
                    }
                    else if (flag == 1 && Con_Q == -1 && qubit_n[i] == null)
                    {
                        System.out.println("Error: unknown variable or constant "+token_set[0]);
                        break;
                    }
                    else if (flag == 1 && underCon_Q == -1 && qubit_n[j] == null)
                    {
                        System.out.println("Error: unknown variable or constant "+token_set[1]);
                        break;
                    }
                }
            }

            else if (s.startsWith("X"))     //Pauli-X Gate
            {
                int a = s.indexOf("(");
                int b = s.indexOf(")");
                String target = " ";
                target = s.substring(a+1, b);
                int size1 = 0;
                int size2 = 0;

                for (int i = 0; i < 64; i++)
                {
                    if (target.equals(qubit_n[i]))
                    {
                        size1 = (int) Math.pow(2, (i));
                        size2 = (int) Math.pow(2, (qubit_counts - i - 1));
                        Matrix I_1 = new Matrix(size1);
                        Matrix I_2 = new Matrix(size2);

                        double[][] Temp_1 = Kronecker(I_1, x);
                        Matrix CNOT_1 = new Matrix(Temp_1);
                        double[][] Temp_2 = Kronecker(CNOT_1, I_2);
                        Matrix Y = new Matrix(Temp_2);

                        //对模拟器量子空间构成的列向量，进行已有量子门的矩阵等效运算
                        double[][] simulator_M = new double[Temp_2.length][1];
                        for (int k = 0; k < Temp_2.length; k++) {
                            simulator_M[k][0] = Simulator_State[k];
                        }
                        Matrix Global_Simulator = new Matrix(simulator_M);
                        Global_Simulator = Y.multiply(Global_Simulator);
                        //Complex[] Y_temp = new Complex[Temp_2.length];

                        for (int k = 0; k < Temp_2.length; k++) {     //更新模拟器量子空间状态
                            Simulator_State[k] = Global_Simulator.data[k][0];
                        }
                        System.out.print("simulate result: ");
                        for (int l = 0; l < Temp_2.length - 1; l++) {
                            System.out.print(String.format("%.5f", Global_Simulator.data[l][0]) + "|" + toBinary(l, qubit_counts) + "> + ");
                        }
                        System.out.println(String.format("%.5f", Global_Simulator.data[Temp_2.length - 1][0]) + "|" + toBinary(Temp_2.length - 1, qubit_counts) + ">");

                        double temp;
                        temp = phase[i][1];
                        phase[i][1] = phase[i][2];
                        phase[i][2] = temp;

                        if (partial == 1) {
                            System.out.println("simulate result: " + String.format("%.5f", phase[i][1]) + " |0> + "
                                    + String.format("%.5f", phase[i][2]) + " |1> ");
                        }
                        try
                        {
                            String data = "X " + i + "\n";
                            //true = append file
                            FileWriter fileWritter = new FileWriter(file.getName(),true);
                            fileWritter.write(data);
                            fileWritter.close();
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        if (circuit_state == 1) {
                            String[] para = new String[]{"python", "D:\\Java\\QC_Simulator\\src\\com\\company\\quantum_circuit.py",
                                    String.valueOf(qubit_counts)};
                            PythonInvoke.invokePy(para);
                        }
                        break;
                    }
                    else if(target != qubit_n[i] && qubit_n[i] == null)
                    {
                        System.out.println("Error: unknown variable or constant "+target);
                        break;
                    }
                }
            }
            else if (s.startsWith("Y"))     //Pauli-Y Gate
            {
                int a = s.indexOf("(");
                int b = s.indexOf(")");
                String target = " ";
                target = s.substring(a+1, b);
                Y_counts = Y_counts + 1;
                int size1 = 0;
                int size2 = 0;
                for (int i = 0; i < 64; i++)
                {
                    if (target.equals(qubit_n[i]))
                    {
                        size1 = (int) Math.pow(2, (i));
                        size2 = (int) Math.pow(2, (qubit_counts - i - 1));
                        Matrix I_1 = new Matrix(size1);
                        Matrix I_2 = new Matrix(size2);

                        double[][] Temp_1 = Kronecker(I_1, y);
                        Matrix CNOT_1 = new Matrix(Temp_1);
                        double[][] Temp_2 = Kronecker(CNOT_1, I_2);
                        Matrix Y = new Matrix(Temp_2);

                        //对模拟器量子空间构成的列向量，进行已有量子门的矩阵等效运算
                        double[][] simulator_M = new double[Temp_2.length][1];
                        for (int k = 0; k < Temp_2.length; k++) {
                            simulator_M[k][0] = Simulator_State[k];
                        }
                        Matrix Global_Simulator = new Matrix(simulator_M);
                        Global_Simulator = Y.multiply(Global_Simulator);
                        //Complex[] Y_temp = new Complex[Temp_2.length];

                        for (int k = 0; k < Temp_2.length; k++) {     //更新模拟器量子空间状态
                            if (Y_counts%2 == 1)
                                Simulator_State[k] = Global_Simulator.data[k][0];
                            else
                                Simulator_State[k] = -Global_Simulator.data[k][0];
                        }
                        System.out.print("simulate result: ");
                        for (int l = 0; l < Temp_2.length - 1; l++) {
                            if (Y_counts%2 == 1)
                                System.out.print(String.format("%.5f", Global_Simulator.data[l][0]) + "i|" + toBinary(l, qubit_counts) + "> + ");
                            else
                                System.out.print(String.format("%.5f", Global_Simulator.data[l][0]) + "|" + toBinary(l, qubit_counts) + "> + ");
                        }
                        if (Y_counts%2 == 1)
                            System.out.println(String.format("%.5f", Global_Simulator.data[Temp_2.length - 1][0]) + "i|" + toBinary(Temp_2.length - 1, qubit_counts) + ">");
                        else
                            System.out.println(String.format("%.5f", Global_Simulator.data[Temp_2.length - 1][0]) + "|" + toBinary(Temp_2.length - 1, qubit_counts) + ">");

                        //double temp;
                        /*
                        Complex Y_1 = new Complex(0,-1);
                        Complex Y_2 = new Complex(0,1);
                        Complex q_1 = new Complex(phase[i][1],0);
                        Complex q_2 = new Complex(phase[i][2],0);
                        Complex q1 = Y_1.mul(q_2);
                        Complex q2 = Y_2.mul(q_1);
                        temp = phase[i][1];
                        phase[i][1] = -phase[i][2];
                        phase[i][2] = temp;
                        System.out.print("simulate result: ");
                        q1.print();
                        System.out.print(" |0> + ");
                        q2.print();
                        System.out.print(" |1> \n");
                        */
                        try
                        {
                            String data = "Y " + i + "\n";
                            //true = append file
                            FileWriter fileWritter = new FileWriter(file.getName(),true);
                            fileWritter.write(data);
                            fileWritter.close();
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        if (circuit_state == 1) {
                            String[] para = new String[]{"python", "D:\\Java\\QC_Simulator\\src\\com\\company\\quantum_circuit.py",
                                    String.valueOf(qubit_counts)};
                            PythonInvoke.invokePy(para);
                        }
                        break;
                    }
                    else if(target != qubit_n[i] && qubit_n[i] == null)
                    {
                        System.out.println("Error: unknown variable or constant "+target);
                        break;
                    }
                }
            }
            else if (s.startsWith("Z"))     //Pauli-Z Gate
            {
                int a = s.indexOf("(");
                int b = s.indexOf(")");
                String target = " ";
                target = s.substring(a+1, b);
                int size1 = 0;
                int size2 = 0;

                for (int i = 0; i < 64; i++)
                {
                    if (target.equals(qubit_n[i]))
                    {
                        size1 = (int) Math.pow(2, (i));
                        size2 = (int) Math.pow(2, (qubit_counts - i - 1));
                        Matrix I_1 = new Matrix(size1);
                        Matrix I_2 = new Matrix(size2);

                        double[][] Temp_1 = Kronecker(I_1, z);
                        Matrix CNOT_1 = new Matrix(Temp_1);
                        double[][] Temp_2 = Kronecker(CNOT_1, I_2);
                        Matrix Y = new Matrix(Temp_2);

                        //对模拟器量子空间构成的列向量，进行已有量子门的矩阵等效运算
                        double[][] simulator_M = new double[Temp_2.length][1];
                        for (int k = 0; k < Temp_2.length; k++) {
                            simulator_M[k][0] = Simulator_State[k];
                        }
                        Matrix Global_Simulator = new Matrix(simulator_M);
                        Global_Simulator = Y.multiply(Global_Simulator);
                        //Complex[] Y_temp = new Complex[Temp_2.length];

                        for (int k = 0; k < Temp_2.length; k++) {     //更新模拟器量子空间状态
                            Simulator_State[k] = Global_Simulator.data[k][0];
                        }
                        System.out.print("simulate result: ");
                        for (int l = 0; l < Temp_2.length - 1; l++) {
                            System.out.print(String.format("%.5f", Global_Simulator.data[l][0]) + "|" + toBinary(l, qubit_counts) + "> + ");
                        }
                        System.out.println(String.format("%.5f", Global_Simulator.data[Temp_2.length - 1][0]) + "|" + toBinary(Temp_2.length - 1, qubit_counts) + ">");


                        phase[i][2] = -phase[i][2];
                        if (partial == 1) {
                            System.out.println("simulate result: " + String.format("%.5f", phase[i][1]) + " |0> + "
                                    + String.format("%.5f", phase[i][2]) + " |1> ");
                        }
                        try
                        {
                            String data = "Z " + i + "\n";
                            //true = append file
                            FileWriter fileWritter = new FileWriter(file.getName(),true);
                            fileWritter.write(data);
                            fileWritter.close();

                            //System.out.println("Done");

                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        if (circuit_state == 1) {
                            String[] para = new String[]{"python", "D:\\Java\\QC_Simulator\\src\\com\\company\\quantum_circuit.py",
                                    String.valueOf(qubit_counts)};
                            PythonInvoke.invokePy(para);
                        }
                        break;
                    }
                    else if(target != qubit_n[i] && qubit_n[i] == null)
                    {
                        System.out.println("Error: unknown variable or constant "+target);
                        break;
                    }
                }
            }

            else if (s.startsWith("SDG"))     //Pauli-Z Gate
            {
                int a = s.indexOf("(");
                int b = s.indexOf(")");
                String target = " ";
                target = s.substring(a+1, b);
                int size1 = 0;
                int size2 = 0;

                for (int i = 0; i < 64; i++)
                {
                    if (target.equals(qubit_n[i]))
                    {

                        try
                        {
                            String data = "SDG " + i + "\n";
                            //true = append file
                            FileWriter fileWritter = new FileWriter(file.getName(),true);
                            fileWritter.write(data);
                            fileWritter.close();

                            //System.out.println("Done");

                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        if (circuit_state == 1) {
                            String[] para = new String[]{"python", "D:\\Java\\QC_Simulator\\src\\com\\company\\quantum_circuit.py",
                                    String.valueOf(qubit_counts)};
                            PythonInvoke.invokePy(para);
                        }
                        break;
                    }
                    else if(target != qubit_n[i] && qubit_n[i] == null)
                    {
                        System.out.println("Error: unknown variable or constant "+target);
                        break;
                    }
                }
            }

            else if (s.startsWith("swap"))     //SWAP gate
            {
                int Con_Q = -1, underCon_Q = -1;
                int flag = 2;
                int a = s.indexOf("(");
                int b = s.indexOf(")");
                String target = " ";
                target = s.substring(a+1, b);
                String[] token_set = target.split(",");
                int size1 = 0;
                int size2 = 0;
                int size3 = 0;
                int size4 = 0;
                for (int i = 0, j = 0; i < 64 && j < 64; i++, j++)
                {
                    if (flag == 0)
                    {
                        try
                        {
                            String data = "SWAP " + Con_Q + " " + underCon_Q + "\n";
                            //true = append file
                            FileWriter fileWritter = new FileWriter(file.getName(),true);
                            fileWritter.write(data);
                            fileWritter.close();

                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        if (circuit_state == 1) {
                            String[] para = new String[]{"python", "D:\\Java\\QC_Simulator\\src\\com\\company\\quantum_circuit.py",
                                    String.valueOf(qubit_counts)};
                            PythonInvoke.invokePy(para);
                        }
                        break;

                    }

                    if (token_set[0].equals(qubit_n[i]))    //controlling qubit
                    {
                        flag--;
                        Con_Q = i;
                    }
                    else if (token_set[1].equals(qubit_n[j]))   //under-controlled qubit
                    {
                        flag--;
                        underCon_Q = j;
                    }
                    else if (flag == 1 && Con_Q == -1)
                    {
                        System.out.println("Error: unknown variable or constant "+token_set[0]);
                        break;
                    }
                    else if (flag == 1 && underCon_Q == -1)
                    {
                        System.out.println("Error: unknown variable or constant "+token_set[1]);
                        break;
                    }
                }
            }

            else if (s.startsWith("CZ"))     //Controlled Z gate
            {
                int Con_Q = -1, underCon_Q = -1;
                int flag = 2;
                int a = s.indexOf("(");
                int b = s.indexOf(")");
                String target = " ";
                target = s.substring(a+1, b);
                String[] token_set = target.split(",");
                int size1 = 0;
                int size2 = 0;
                int size3 = 0;
                int size4 = 0;
                for (int i = 0, j = 0; i < 64 && j < 64; i++, j++)
                {
                    if (flag == 0)
                    {
                        if (Con_Q < underCon_Q)        //将Controlled-Z门拆成Con_Q = 0的矩阵和Con_Q = 1的矩阵叠加
                        {
                            size1 = (int) Math.pow(2, (Con_Q));
                            size2 = (int) Math.pow(2, (underCon_Q - Con_Q - 1));
                            size3 = (int) Math.pow(2, (qubit_counts - underCon_Q - 1));
                            size4 = (int) Math.pow(2, (qubit_counts - Con_Q - 1));
                            Matrix I_1 = new Matrix(size1);
                            Matrix I_2 = new Matrix(size2);
                            Matrix I_3 = new Matrix(size3);
                            Matrix I_4 = new Matrix(size4);

                            double[][] Temp_1 = Kronecker(I_1, Qstate_0);
                            Matrix CNOT_1 = new Matrix(Temp_1);
                            double[][] Temp_2 = Kronecker(CNOT_1, I_4);
                            Matrix CNOT_I = new Matrix(Temp_2);

                            Temp_1 = Kronecker(I_1, Qstate_1);
                            CNOT_1 = CNOT_1.assign(Temp_1);
                            double[][] Temp_3 = Kronecker(CNOT_1, I_2);
                            Matrix CNOT_2 = new Matrix(Temp_3);
                            double[][] Temp_4 = Kronecker(CNOT_2, z);
                            Matrix CNOT_3 = new Matrix(Temp_4);
                            double[][] Temp_5 = Kronecker(CNOT_3, I_3);
                            Matrix CNOT_X = new Matrix(Temp_5);
                            //for (int m=0;m<4;m++)
                            //    System.out.println (Arrays.toString (Temp_5[m]));
                            CNOT_X = CNOT_I.plus(CNOT_X);

                            //对模拟器量子空间构成的列向量，进行已有量子门的矩阵等效运算
                            double[][] simulator_M = new double[Temp_2.length][1];
                            for (int k = 0; k < Temp_2.length; k++) {
                                simulator_M[k][0] = Simulator_State[k];
                            }
                            Matrix Global_Simulator = new Matrix(simulator_M);
                            Global_Simulator = CNOT_X.multiply(Global_Simulator);
                            for (int k = 0; k < Temp_2.length; k++) {     //更新模拟器量子空间状态
                                Simulator_State[k] = Global_Simulator.data[k][0];
                            }
                            System.out.print("simulate result: ");
                            for (int l = 0; l < Temp_2.length - 1; l++) {
                                System.out.print(String.format("%.5f", Global_Simulator.data[j][0]) + "|" + toBinary(l, qubit_counts) + "> + ");
                            }
                            System.out.println(String.format("%.5f", Global_Simulator.data[Temp_2.length - 1][0]) + "|" + toBinary(Temp_2.length - 1, qubit_counts) + ">");
                        }
                        else if (Con_Q > underCon_Q)        //将CNOT门拆成Con_Q = 0的矩阵和Con_Q = 1的矩阵叠加
                        {
                            size1 = (int) Math.pow(2, (Con_Q));     // I*Con_Q*I
                            size2 = (int) Math.pow(2, (Con_Q - underCon_Q - 1));
                            size3 = (int) Math.pow(2, (underCon_Q));     //
                            size4 = (int) Math.pow(2, (qubit_counts - Con_Q - 1));
                            Matrix I_1 = new Matrix(size1);
                            Matrix I_2 = new Matrix(size2);
                            Matrix I_3 = new Matrix(size3);
                            Matrix I_4 = new Matrix(size4);

                            double[][] Temp_1 = Kronecker(I_1, Qstate_0);
                            Matrix CNOT_1 = new Matrix(Temp_1);
                            double[][] Temp_2 = Kronecker(CNOT_1, I_4);
                            Matrix CNOT_I = new Matrix(Temp_2);

                            Temp_1 = Kronecker(I_3, z);
                            CNOT_1 = CNOT_1.assign(Temp_1);
                            double[][] Temp_3 = Kronecker(CNOT_1, I_2);
                            Matrix CNOT_2 = new Matrix(Temp_3);
                            double[][] Temp_4 = Kronecker(CNOT_2, Qstate_1);
                            Matrix CNOT_3 = new Matrix(Temp_4);
                            double[][] Temp_5 = Kronecker(CNOT_3, I_3);
                            Matrix CNOT_X = new Matrix(Temp_5);
                            CNOT_X = CNOT_I.plus(CNOT_X);

                            //对模拟器量子空间构成的列向量，进行已有量子门的矩阵等效运算
                            double[][] simulator_M = new double[Temp_2.length][1];
                            for (int k = 0; k < Temp_2.length; k++) {
                                simulator_M[k][0] = Simulator_State[k];
                            }
                            Matrix Global_Simulator = new Matrix(simulator_M);
                            Global_Simulator = CNOT_X.multiply(Global_Simulator);
                            for (int k = 0; k < Temp_2.length; k++) {     //更新模拟器量子空间状态
                                Simulator_State[k] = Global_Simulator.data[k][0];
                            }
                            System.out.print("simulate result: ");
                            for (int l = 0; l < Temp_2.length - 1; l++) {
                                System.out.print(String.format("%.5f", Global_Simulator.data[j][0]) + "|" + toBinary(l, qubit_counts) + "> + ");
                            }
                            System.out.println(String.format("%.5f", Global_Simulator.data[Temp_2.length - 1][0]) + "|" + toBinary(Temp_2.length - 1, qubit_counts) + ">");
                        }

                        try
                        {
                            String data = "CZ " + Con_Q + " " + underCon_Q + "\n";
                            //true = append file
                            FileWriter fileWritter = new FileWriter(file.getName(),true);
                            fileWritter.write(data);
                            fileWritter.close();

                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        if (circuit_state == 1) {
                            String[] para = new String[]{"python", "D:\\Java\\QC_Simulator\\src\\com\\company\\quantum_circuit.py",
                                    String.valueOf(qubit_counts)};
                            PythonInvoke.invokePy(para);
                        }
                        break;

                    }

                    if (token_set[0].equals(qubit_n[i]))    //controlling qubit
                    {
                        flag--;
                        Con_Q = i;
                    }
                    else if (token_set[1].equals(qubit_n[j]))   //under-controlled qubit
                    {
                        flag--;
                        underCon_Q = j;
                    }
                    else if (flag == 1 && Con_Q == -1)
                    {
                        System.out.println("Error: unknown variable or constant "+token_set[0]);
                        break;
                    }
                    else if (flag == 1 && underCon_Q == -1)
                    {
                        System.out.println("Error: unknown variable or constant "+token_set[1]);
                        break;
                    }
                }
            }

            else if (s.startsWith("measure"))     //measure a single qubit
            {
                String[] token_set = s.split(" ");
                double Measure = Math.random();
                if (token_set.length == 1)
                {
                    for (int i = 0; i < (Math.pow(2, qubit_counts) - 1); i++)
                    {
                        Measure = Measure - Simulator_State[i]*Simulator_State[i];
                        if (Measure <= 0)
                            System.out.println(toBinary(i, qubit_counts));
                    }
                }
                else {
                    for (int i = 0; i < 64; i++)
                    {
                        if (token_set[1].equals(qubit_n[i]))
                        {
                            int measurement = Math.random() > (phase[i][1]*phase[i][1]) ? 1: 0;
                            try
                            {
                                String data = "M " + i + "\n";
                                //true = append file
                                FileWriter fileWritter = new FileWriter(file.getName(),true);
                                fileWritter.write(data);
                                fileWritter.close();

                            }
                            catch(IOException e)
                            {
                                e.printStackTrace();
                            }
                            if (circuit_state == 1)
                            {
                                String []para=new String[] {"python","D:\\Java\\QC_Simulator\\src\\com\\company\\quantum_circuit.py",
                                        String.valueOf(qubit_counts)};
                                PythonInvoke.invokePy(para);
                            }
                            System.out.println("Measurement: " + token_set[1] + " = " + measurement);
                            break;
                        }
                        else if(token_set[1] != qubit_n[i] && qubit_n[i] == null)
                        {
                            System.out.println("Error: unknown variable or constant "+token_set[1]);
                            break;
                        }
                    }
                }

            }

            else if (s.startsWith("qubit"))
            {
                String[] token_set = s.split(" ");
                for (int i = 0; i < 64; i++)
                {
                    if (qubit_n[i] == null)
                    {
                        qubit_n[i] = token_set[1];
                        phase[i][1] = 1;
                        phase[i][2] = 0;
                        qubit_counts += 1;
                        /*if (circuit_state == 1) {
                            String[] para = new String[]{"python", "D:\\Java\\QC_Simulator\\src\\com\\company\\quantum_circuit.py", String.valueOf(qubit_counts)};
                            PythonInvoke.invokePy(para);
                        }*/
                        break;
                    }
                }
            }

            else if (s.startsWith("dump"))
            {
                String[] token_set = s.split(" ");
                if (token_set.length == 1)
                {

                    System.out.println("STATE: " + qubit_counts + " / 64 qubits allocated " + (64 - qubit_counts) + " / 64 qubits free");
                    for (int i = 0; i < (Math.pow(2, qubit_counts) - 1); i++)
                    {
                        System.out.print(String.format("%.5f", Simulator_State[i]) + " |"+toBinary(i, qubit_counts)+"> + ");
                    }
                    System.out.println(String.format("%.5f", Simulator_State[(2^qubit_counts - 1)]) + " |"+toBinary((int)(Math.pow(2, qubit_counts) - 1), qubit_counts)+"> ");
                }
                else
                {
                    for (int i = 0; i < 64; i++)
                    {
                        if (token_set[1].equals(qubit_n[i]))
                        {
                            System.out.println("simulate result: "+ String.format("%.5f",phase[i][1]) + " |0> + "
                                    + String.format("%.5f",phase[i][2]) + " |1> ");
                            break;
                        }
                        else if(token_set[1] != qubit_n[i] && qubit_n[i] == null)
                        {
                            System.out.println("Error: unknown variable or constant "+token_set[1]);
                            break;
                        }
                    }
                }
            }

            else if (s.startsWith("refresh"))
            {
                try
                {
                    //if file doesnt exists, then create it
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    //true = append file
                    FileWriter fileWritter = new FileWriter(file);
                    fileWritter.write("");
                    fileWritter.flush();
                    fileWritter.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                qubit_counts = 0;
                Y_counts = 0;
                for (int i = 0; i < 64; i++)
                {
                    Simulator_State[i] = 0;
                    if (qubit_n[i] != null)
                    {
                        qubit_n[i] = null;
                    }
                }
                Simulator_State[0] = 1;
            }

            else if (s.startsWith("view"))
            {
                String []para=new String[] {"python","D:\\Java\\QC_Simulator\\src\\com\\company\\quantum_circuit.py",
                        String.valueOf(qubit_counts)};
                PythonInvoke.invokePy(para);
            }

            else if (s.startsWith("circuit_off"))
            {
                circuit_state = 0;
            }

            else if (s.startsWith("circuit_on"))
            {
                circuit_state = 1;
            }

            else if (s.startsWith("partial"))
            {
                partial = 1;
            }

            else if (s.startsWith("global"))
            {
                partial = 0;
            }

            else
            {
                System.out.println("undefined procedure or operator " + s);
            }

            if(s.equals("exit"))
            {
                System.out.println("Simulator Ends!");
                System.exit(0);
            }
            //System.out.println("output: "+s);
            System.out.print("qcs>");
        }
    }
}
