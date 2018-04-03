package com.example.hasee.sae;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.ujmp.core.Matrix;
import org.ujmp.jmatio.ImportMatrixMAT;

public class MMatrix implements Parcelable {
    private int m_cols;
    private int m_rows;

    public int getM_cols() {
        return m_cols;
    }

    public int getM_rows() {
        return m_rows;
    }
    private double m_data[][];

    public double[][] getM_data() {
        return m_data;
    }

    private int mData;
    private MMatrix(Parcel in) {
        mData = in.readInt();
    }
    public static final Parcelable.Creator<MMatrix> CREATOR
            = new Parcelable.Creator<MMatrix>() {
        public MMatrix createFromParcel(Parcel in) {
            return new MMatrix(in);
        }

        public MMatrix[] newArray(int size) {
            return new MMatrix[size];
        }
    };

    @Override
    public int describeContents() {
                 // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(mData);
    }

    MMatrix(int r, int c) {
        m_cols = c;
        m_rows = r;
        m_data = new double[m_rows][m_cols];
        for (int i = 0; i < m_rows; i++) {
            for (int j = 0; j < m_cols; j++) {
                m_data[i][j] = Math.random();
            }
        }
    }
     
    MMatrix(int r, int c,double tmp) {
        m_cols = c;
        m_rows = r;
        m_data = new double[m_rows][m_cols];
        for (int i = 0; i < m_rows; i++) {
            for (int j = 0; j < m_cols; j++) {
                m_data[i][j] = tmp;
            }
        }
    }
    
    MMatrix(double ar[][]) {
        m_rows=ar.length;
        m_cols=ar[0].length;
        m_data = ar;
        double s = 0.25;
        byte a = (byte)s;
    }
    
    MMatrix(String path) throws IOException {
        ImportMatrixMAT test=new ImportMatrixMAT();
        File file=new File(path);
        Matrix testMatrix=test.fromFile(file);
        double[][] dtmp = testMatrix.toDoubleArray();
        this.m_data = dtmp;
        m_rows = dtmp.length;
        m_cols = dtmp[0].length;
    }
    public double getElement(int r, int c) {
        return m_data[r][c];
    }
    public void setElement(int r, int c, double e) {
        m_data[r][c] = e;
    }
    
    public double matrixFindEr(MMatrix m) {
        double count=0;
        for (int i = 0; i < m_rows; i++) {
            for (int j = 0; j < m_cols; j++) {
                if(this.m_data[i][j]!=m.getElement(i, j))count++;
            }
        }
        return count;
        
    }
    
    public MMatrix bsxfun_minus(MMatrix m) {
        int count=0;
        for (int i = 0; i < m_cols; i++) {
            for (int j = 0; j < m_rows; j++) {
                this.m_data[j][i] -= m.getElement(j, 1);
            }
        }
        return this;
        
    }
    
    public double matrixCompare(double a) {
        MMatrix temp = new MMatrix(m_rows, m_cols);
        for (int i = 0; i < m_rows; i++) {
            for (int j = 0; j < m_cols; j++) {
                if(this.getElement(i, j)<=a) return 0;
            }
        }
        return 1;
    }
    
    public MMatrix bsxfun_rdivide(MMatrix m) {
        int count=0;
        for (int i = 0; i < m_cols; i++) {
            for (int j = 0; j < m_rows; j++) {
                this.m_data[j][i] /= m.getElement(j, 1);
            }
        }
        return this;
        
    }
    public MMatrix matrixAdd(MMatrix m) {
        if(this.m_cols==m.getM_cols()&&this.m_rows==m.getM_rows()){
            MMatrix temp = new MMatrix(m_rows, m_cols);
            for (int i = 0; i < m_rows; i++) {
                for (int j = 0; j < m_cols; j++) {
                temp.setElement(i, j, this.getElement(i, j) + m.getElement(i, j));
                }
            }
            return temp;
        }
        else if(this.m_cols==m.getM_cols()){
            MMatrix temp = new MMatrix(m_rows, m_cols);
            for (int i = 0; i < m_rows; i++) {
                for (int j = 0; j < m_cols; j++) {
                temp.setElement(i, j, this.getElement(i, j) + m.getElement(0, j));
                }
            }
            return temp;
        }
        else{
            MMatrix temp = new MMatrix(m_rows, m_cols);
            for (int i = 0; i < m_rows; i++) {
                for (int j = 0; j < m_cols; j++) {
                temp.setElement(i, j, this.getElement(i, j) + m.getElement(i, 0));
                }
            }
            return temp;
        }
    }
    
    public MMatrix matrixmix(MMatrix b){
        MMatrix temp = new MMatrix(this.getM_rows(), this.getM_cols()+b.getM_cols(),0);
        for(int i =0;i<temp.getM_rows();i++){
            int count=0;
            for(int j=0;j<this.m_cols;j++){
                temp.setElement(i, count++, this.getElement(i, j));
            }
            for(int j=0;j<b.m_cols;j++){
                temp.setElement(i, count++, b.getElement(i, j));
            }
        }
        return temp;
    }
    
    public static MMatrix matrixmixByrow(List<MMatrix> m){
        MMatrix temp = new MMatrix(m.size(), m.get(0).getM_cols(),0);
        for(int i =0;i<temp.getM_rows();i++){
            for(int j = 0;j<temp.m_cols;j++){
                temp.setElement(i, j, m.get(i).getElement(0, j));
            }
        }
        return temp;
    }
    
    public MMatrix matrixAdd(double m) {
        for (int i = 0; i < m_rows; i++) {
            for (int j = 0; j < m_cols; j++) {
                this.m_data[i][j] += m;
            }
        }
        return this;
    }
    
    
    
    public MMatrix matrixExp() {
        for (int i = 0; i < m_rows; i++) {
            for (int j = 0; j < m_cols; j++) {
                this.m_data[i][j] = Math.pow(Math.E, this.m_data[i][j]);
            }
        }
        return this;
    }
    
    public MMatrix matrixPow(int p) {
        for (int i = 0; i < m_rows; i++) {
            for (int j = 0; j < m_cols; j++) {
                this.m_data[i][j] = Math.pow(m_data[i][j],p);
            }
        }
        return this;
    }
    public MMatrix matrixSum(int mode) {
        if(mode == 1){
            MMatrix temp = new MMatrix(1, m_cols);
            for (int i = 0; i < m_cols; i++) {
                double sum=0;
                for (int j = 0; j < m_rows; j++) {
                    sum+= m_data[j][i];
                }
                temp.m_data[0][i]=sum;
            }
            return temp;
        }
        else if(mode==2){
            MMatrix temp = new MMatrix(m_rows, 1);
            for (int i = 0; i < m_rows; i++) {
                double sum=0;
                for (int j = 0; j < m_cols; j++) {
                    sum+= m_data[i][j];
                }
                temp.m_data[i][0]=sum;
            }
            return temp;
        }
        return null;
        
    }
    public MMatrix matrixMax(int mode) {
        if(mode==1){
            MMatrix temp = new MMatrix(1, m_cols);
            for (int i = 0; i < m_cols; i++) {
                double max=Double.MIN_VALUE;
                for (int j = 0; j < m_rows; j++) {
                    if(this.m_data[j][i]>max) max = this.m_data[j][i];
                }
                temp.m_data[0][i]=max;
            }
            return temp;
        }
        else if(mode==2){
            MMatrix temp = new MMatrix(m_rows, 1);
            for (int i = 0; i < m_rows; i++) {
                double max=this.m_data[i][0];
                for (int j = 1; j < m_cols; j++) {
                    if(this.m_data[i][j]>max) max = this.m_data[i][j];
                }
                temp.m_data[i][0]=max;
            }
            return temp;
        }
        return null;
    }
    
    public MMatrix matrixAvg(int mode) {
        if(mode==1){
            MMatrix temp = new MMatrix(1, m_cols);
            for (int i = 0; i < m_cols; i++) {
                double sum=0;
                for (int j = 0; j < m_rows; j++) {
                    
                }
                sum/=this.getM_rows();
                temp.setElement(0, i, sum);
            }
            return temp;
        }
        else if(mode==2){
            MMatrix temp = new MMatrix(m_rows, 1);
            for (int i = 0; i < m_rows; i++) {
                double sum=0;
                for (int j = 1; j < m_cols; j++) {
                    sum+=this.getElement(j, i);
                }
                sum/=this.getM_cols();
                temp.setElement(i, 0, sum);
            }
            return temp;
        }
        return null;
    }
    
    public MMatrix matrixMaxIndex(int mode) {
        if(mode==1){
            MMatrix temp = new MMatrix(1, m_cols);
            for (int i = 0; i < m_cols; i++) {
                double max=Double.MIN_VALUE;
                int index=0;
                for (int j = 0; j < m_rows; j++) {
                    if(this.m_data[j][i]>max){
                        max = this.m_data[j][i];
                        index = j;
                    }
                }
                temp.m_data[0][i]=index;
            }
            return temp;
        }
        else if(mode==2){
            MMatrix temp = new MMatrix(m_rows, 1);
            for (int i = 0; i < m_rows; i++) {
                double max=this.m_data[i][0];
                int index=0;
                for (int j = 1; j < m_cols; j++) {
                    if(this.m_data[i][j]>max) {
                        max = this.m_data[i][j];
                        index = j;
                    }
                }
                temp.m_data[i][0]=index;
            }
            return temp;
        }
        return null;
    }
    
    public MMatrix matrixProduct(MMatrix m) {
        MMatrix temp = new MMatrix(m_rows, m.m_cols);
        for (int i = 0; i < m_rows; i++) {
            for (int j = 0; j < m.m_cols; j++) {
                double sum = 0;
                for (int k = 0; k < m_cols; k++) {
                    sum += m_data[i][k] * m.m_data[k][j];
                }
                temp.m_data[i][j] = sum;
            }
        }
        return temp;
    }
    public MMatrix matrixProduct(double m) {
        for(int i = 0;i<m_rows;i++){
            for(int j = 0;j<m_cols;j++){
                this.m_data[i][j] *= m;
            }
        }
        return this;
    }
    
    public MMatrix matrixPreDiv(double m) {
        for(int i = 0;i<m_rows;i++){
            for(int j = 0;j<m_cols;j++){
                this.m_data[i][j] = m/this.m_data[i][j];
            }
        }
        return this;
    }
    public MMatrix matrixDiv(double m) {
        for(int i = 0;i<m_rows;i++){
            for(int j = 0;j<m_cols;j++){
                this.m_data[i][j] = this.m_data[i][j]/m;
            }
        }
        return this;
    }
    
    public MMatrix matrixLog() {
        for(int i = 0;i<m_rows;i++){
            for(int j = 0;j<m_cols;j++){
                this.m_data[i][j] = Math.log(m_data[i][j]);
            }
        }
        return this;
    }
    
    public MMatrix matrixSqrt(double m) {
        for(int i = 0;i<m_rows;i++){
            for(int j = 0;j<m_cols;j++){
                this.m_data[i][j] = Math.sqrt(this.m_data[i][j]);
            }
        }
        return this;
    }
    
    
    public MMatrix matrixTrans() {
        MMatrix temp = new MMatrix(m_cols, m_rows);
        for (int i = 0; i < m_rows; i++) {
            for (int j = 0; j < m_cols; j++) {
                temp.m_data[j][i] = m_data[i][j];
            }
        }
        return temp;
    }
    
    /*
     * 求(h,v)坐标的位置的余子式
     */
    public double[][] getConfactor(double[][] data, int h, int v) {
        int H = data.length;
        int V = data[0].length;
        double[][] newdata = new double[H-1][V-1];
        for(int i=0; i<newdata.length; i++) {
            if(i < h-1) {
                for(int j=0; j<newdata[i].length; j++) {
                    if(j < v-1) {
                        newdata[i][j] = data[i][j];
                    }else {
                        newdata[i][j] = data[i][j+1];
                    }
                }
            }else {
                for(int j=0; j<newdata[i].length; j++) {
                    if(j < v-1) {
                        newdata[i][j] = data[i+1][j];
                    }else {
                        newdata[i][j] = data[i+1][j+1];
                    }
                }
            }
        }

//      for(int i=0; i<newdata.length; i ++)
//          for(int j=0; j<newdata[i].length; j++) {
//              System.out.println(newdata[i][j]);
//          }
        return newdata;
    }
    
        /*
     * 计算行列式的值
     */
    public double getMartrixResult(double[][] data) {
        /*
         * 二维矩阵计算
         */
        if(data.length == 2) {
            return data[0][0]*data[1][1] - data[0][1]*data[1][0];
        }
        /*
         * 二维以上的矩阵计算
         */
        double result = 0;
        int num = data.length;
        double[] nums = new double[num];
        for(int i=0; i<data.length; i++) {
            if(i%2 == 0) {
                nums[i] = data[0][i] * getMartrixResult(getConfactor(data, 1, i+1));
            }else {
                nums[i] = -data[0][i] * getMartrixResult(getConfactor(data, 1, i+1));
            }
        }
        for(int i=0; i<data.length; i++) {
            result += nums[i];
        }

//      System.out.println(result);
        return result;
    }
    
    public double[][] getReverseMartrix(double[][] data) {
        double[][] newdata = new double[data.length][data[0].length];
        double A = getMartrixResult(data);
//      System.out.println(A);
        for(int i=0; i<data.length; i++) {
            for(int j=0; j<data[0].length; j++) {
                if((i+j)%2 == 0) {
                    newdata[i][j] = getMartrixResult(getConfactor(data, i+1, j+1)) / A;
                }else {
                    newdata[i][j] = -getMartrixResult(getConfactor(data, i+1, j+1)) / A;
                }

            }
        }
        newdata = trans(newdata);

        for(int i=0;i<newdata.length; i++) {
            for(int j=0; j<newdata[0].length; j++) {
                System.out.print(newdata[i][j]+ "   ");
            }
            System.out.println();
        }
        return newdata;
    }

    private double[][] trans(double[][] newdata) {
        // TODO Auto-generated method stub
        double[][] newdata2 = new double[newdata[0].length][newdata.length];
        for(int i=0; i<newdata.length; i++) 
            for(int j=0; j<newdata[0].length; j++) {
                newdata2[j][i] = newdata[i][j];
            }
        return newdata2;
    }
    
    public MMatrix matrixInv(){
        MMatrix tmp =new MMatrix(trans(this.m_data));
        return tmp;
    }

    public static MMatrix getMySpecialMM(int n,int k){
        int m =(n-1)*k+1 ;
        MMatrix SpecialMM = new MMatrix(n,m,0);
        SpecialMM.setElement(0,0,1);
        for(int i =0;i<n;i++){
            SpecialMM.setElement(i,0,1);
        }

        for(int i =1;i<n;i++){
            SpecialMM.setElement(i,i*k,-1);
        }
        return SpecialMM;
    }
    
}
