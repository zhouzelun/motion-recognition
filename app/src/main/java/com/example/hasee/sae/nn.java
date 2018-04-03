package com.example.hasee.sae;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class nn {
    private int[] size={173,179,185,191,197,7};
    private int n=size.length;
    private String activation_function = "sigm";   
    private int learningRate = 1;            
    private double momentum = 0.5000;          
    private int scaling_learningRate = 1;           
    private int weightPenaltyL2 = 0;            
    private int nonSparsityPenalty = 0;           
    private double sparsityTarget = 0.05;         
    private int inputZeroMaskedFraction = 0;            
    private int dropoutFraction = 0;            
    private int testing = 1;
    private String output = "sigm";
    MMatrix w[]=new MMatrix[n+1],vw[]=new MMatrix[n+1],p[]=new MMatrix[n+1],a[]=new MMatrix[n+1],e,L,dw[] = new MMatrix[n+1];
    MMatrix[] expected,predict;
    double dropOutMask[];
    
    public nn()throws IOException{
        double[][] dtmp;
        w = new MMatrix[n+1];
        vw = new MMatrix[n+1];
        p = new MMatrix[n+1];
        for(int i=2;i<=this.n-1;i++){
            int rand = size[i]-size[i-1];
            MMatrix temp1 = new MMatrix(size[i],size[i-1]);
            temp1 = temp1.matrixAdd(-0.5);
            temp1 = temp1.matrixProduct(2);
            temp1 = temp1.matrixProduct(4);
            temp1 = temp1.matrixProduct(Math.sqrt(6/(size[i] + size[i - 1])));
            w[i-1]=temp1;
            MMatrix temp2 = new MMatrix(w[i-1].getM_rows(),w[i-1].getM_cols(),0);
            vw[i-1]=temp2;
            p[i]=new MMatrix(1,size[i],0);
        }
        File sdkRoot = Environment.getExternalStorageDirectory();
        String ping_path = sdkRoot.getAbsolutePath() ;
        loadData(ping_path);
    }
    
    public double nntest(MMatrix test_x,MMatrix test_y){

        expected = new MMatrix[2];
        expected[0] = test_y.matrixMax(2);
        expected[1] = test_y.matrixMaxIndex(2);
        predict = nnpredict(test_x);
        return predict[1].matrixFindEr(expected[1])/predict[1].getM_rows();
    }

    public MMatrix nntest(MMatrix test_x){
        predict = nnpredict(test_x);
        return predict[1];
    }
    
    public MMatrix[] nnpredict(MMatrix train_x){
        MMatrix[] labels = new MMatrix[2];
        this.testing=1;
        nnff(train_x,new MMatrix(train_x.getM_rows(),size[size.length-1],0));
        this.testing=0;
        labels[0]= a[a.length-1].matrixMax(2);
        labels[1]= a[a.length-1].matrixMaxIndex(2);
        return labels;
    }
    
    private MMatrix sigm(MMatrix temp2){
        temp2 = temp2.matrixProduct(-1);
        temp2 = temp2.matrixExp();
        temp2 = temp2.matrixAdd(1);
        temp2 = temp2.matrixPreDiv(1.0);
        return temp2;
    }
    
    public void nnff(MMatrix train_x,MMatrix train_y){
        int _n = n;
        int _m = train_x.getM_rows();
        MMatrix temp1 = new MMatrix(_m,1,1);
        train_x = temp1.matrixmix(train_x);
        this.a[1]=train_x;
        for(int i =2;i<=n-1;i++){
            if(activation_function.equals("sigm")){
                
                MMatrix temp2 = a[i-1].matrixProduct(w[i-1].matrixTrans());
                a[i]=sigm(temp2);
            }
            else if(activation_function.equals("tanh_opt")){
                MMatrix temp2 = a[i-1].matrixProduct(w[i-1].matrixTrans());
                temp2 = temp2.matrixProduct(2.0/3.0);
                MMatrix A = temp2.matrixExp();
                MMatrix B = A.matrixProduct(-1);
                B = B.matrixExp();
                MMatrix temp3 = A.matrixAdd(B.matrixProduct(-1));
                MMatrix temp4 = A.matrixAdd(B);
                temp4 = temp4.matrixInv();
                temp2 = temp3.matrixProduct(temp4);
                temp2 = temp2.matrixProduct(1.7159);
                a[i]=temp2;
            }
            
            if(dropoutFraction>0){
                if(testing==1){
                    a[i] = a[i].matrixProduct(1 - dropoutFraction);
                }
                else{
                    MMatrix tmp = new MMatrix(a[i].getM_rows(),a[i].getM_cols());
                    this.dropOutMask[i]=tmp.matrixCompare(this.dropoutFraction);
                    this.a[i] = this.a[i].matrixProduct(this.dropOutMask[i]);
                }
            }
            if(this.nonSparsityPenalty>0){
                p[i]=p[i].matrixProduct(0.99);
                MMatrix tmp = a[i].matrixAvg(1).matrixProduct(0.01);
                p[i]=p[i].matrixAdd(tmp);
            }
            MMatrix tmp1= new MMatrix(_m,1,1);
            a[i]=tmp1.matrixmix(a[i]);
        }
        
        if(output.equals("sigm")){
            a[_n]=sigm(a[_n-1].matrixProduct(w[_n-1].matrixTrans()));
        }
        else if(output.equals("sigm")){
            a[_n]= a[_n-1].matrixProduct(w[_n-1].matrixTrans());
        }
        else if(output.equals("softmax")){
            a[_n]= a[_n-1].matrixProduct(w[_n-1].matrixTrans());
            MMatrix temp2=a[_n];
            MMatrix temp3=a[_n].matrixMax(2);
            a[_n]=temp2.bsxfun_minus(temp3);
            a[_n]=a[_n].matrixExp();
            temp2=a[_n];
            temp3=a[_n].matrixSum(2);
            a[_n]=temp2.bsxfun_rdivide(temp3);
            
        }
        e=train_y.matrixAdd(a[_n].matrixProduct(-1));
        a[_n].matrixProduct(-1);
        if(output.equals("sigm")||output.equals("linear")){
            L=e.matrixPow(2);
            L = L.matrixSum(1);
            L = L.matrixSum(2);
            L = L.matrixProduct(0.5);
            L = L.matrixDiv(_m);
        }
        else if(output.equals("softmax")){
            L = train_y.matrixProduct(a[_n].matrixLog());
        }
    }
    public MMatrix[] getExpected() {
        return expected;
    }

    public MMatrix[] getPredict() {
        return predict;
    }

    private void loadData(String ping_path) {
        try {
            List<MMatrix> tmp = new ArrayList<MMatrix>();

            for (int i = 1; i <= 179; i++) {
                tmp.add(new MMatrix(ping_path + "/data/dW/dW1n" + i + ".mat"));
            }
            dw[1] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 185; i++) {
                tmp.add(new MMatrix(ping_path + "/data/dW/dW2n" + i + ".mat"));
            }
            dw[2] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 191; i++) {
                tmp.add(new MMatrix(ping_path + "/data/dW/dW3n" + i + ".mat"));
            }
            dw[3] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 197; i++) {
                tmp.add(new MMatrix(ping_path + "/data/dW/dW4n" + i + ".mat"));
            }
            dw[4] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 7; i++) {
                tmp.add(new MMatrix(ping_path + "/data/dW/dW5n" + i + ".mat"));
            }
            dw[5] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 179; i++) {
                tmp.add(new MMatrix(ping_path + "/data/vW/vW1n" + i + ".mat"));
            }
            vw[1] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 185; i++) {
                tmp.add(new MMatrix(ping_path + "/data/vW/vW2n" + i + ".mat"));
            }
            vw[2] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 191; i++) {
                tmp.add(new MMatrix(ping_path + "/data/vW/vW3n" + i + ".mat"));
            }
            vw[3] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 197; i++) {
                tmp.add(new MMatrix(ping_path + "/data/vW/vW4n" + i + ".mat"));
            }
            vw[4] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 7; i++) {
                tmp.add(new MMatrix(ping_path + "/data/vW/vW5n" + i + ".mat"));
            }
            vw[5] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 179; i++) {
                tmp.add(new MMatrix(ping_path + "/data/W/W1n" + i + ".mat"));
            }
            w[1] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 185; i++) {
                tmp.add(new MMatrix(ping_path + "/data/W/W2n" + i + ".mat"));
            }
            w[2] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 191; i++) {
                tmp.add(new MMatrix(ping_path + "/data/W/W3n" + i + ".mat"));
            }
            w[3] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 197; i++) {
                tmp.add(new MMatrix(ping_path + "/data/W/W4n" + i + ".mat"));
            }
            w[4] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();

            for (int i = 1; i <= 7; i++) {
                tmp.add(new MMatrix(ping_path + "/data/W/W5n" + i + ".mat"));
            }
            w[5] = MMatrix.matrixmixByrow(tmp);
            tmp.clear();
        } catch (Exception e) {
        }
    }
}