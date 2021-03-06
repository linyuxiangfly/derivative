# 自动求偏导梯度、BP神经网络
JAVA语言实现了BP神经网络、基本函数和复杂函数的自动求偏导的梯度

# 1偏导梯度使用教程
## 1.1接口

>所有操作符都继承***Function***接口，该接口主要有以下方法  
>***isDx***方法是返回该操作符是否存在需要求偏导的对象  
>***prtGrad***方法是返回该对象与指定的对象的偏导梯度  
>***calc***方法是返回该对象的计算结果  
## 1.2 基础操作符

### 1.2.1 常量
>***Const***类是定义一个常量，代码如下：
```
Const c=new Const(5);
```

### 1.2.2 变量
>***Var***类是定义一个变量，变量可以修改值，代码如下：
```
Var v=new Var(5);
v.setVal(10);
```

### 1.2.3 基础双目操作符
>Add类就是实现了加法的计算以及求偏导梯度，代码如下：
```
//定义变量
Var a=new Var(5);
Var b=new Var(4);
//两个数相加
Add y=new Add(a,b);

//计算结果
System.out.println("y:"+y.calc());

//求偏导值
System.out.println("y/a:"+y.prtGrad(a));
System.out.println("y/b:"+y.prtGrad(b));

//结果
y:9.0
y/a:1.0
y/b:1.0
```
>本模块还提供减法(Sub)、乘法(Mcl)、除法(Div)的计算以及偏导梯度，用法跟加法(Add)类似。

### 1.2.4 多参数操作符
>AddMult类就是实现了多个变量进行相加以及偏导梯度，代码如下：
```
Var a=new Var(4);
Var b=new Var(5);

//定义变量
Function[] params=new Function[]{
        new Var(1),
        new Mcl(a,b),
        new Div(a,b),
        new Var(4),
        new Var(5),
};

//两个数相加
AddMult y=new AddMult(params);

//计算结果
System.out.println("y:"+y.calc());

//求偏导值
for(int i=0;i<params.length;i++){
    System.out.println("y/param["+i+"]:"+y.prtGrad(params[i]));
}
System.out.println("y/a:"+y.prtGrad(a));
System.out.println("y/b:"+y.prtGrad(b));

//结果
y:30.8
y/param[0]:1.0
y/param[1]:1.0
y/param[2]:1.0
y/param[3]:1.0
y/param[4]:1.0
y/a:5.2
y/b:3.84
```
>本模块还提供多参数的减法(SubMult)、多参数的乘法(MclMult)、多参数的除法(DivMult)的计算以及偏导梯度，用法跟加法(AddMult)类似。
>

### 1.2.5 指数、幂函数
>Power类就是实现了指数、幂函数的计算以及偏导梯度，代码如下：
```
//定义变量
Var a=new Var(5);
Var b=new Var(3);
//指数、幂函数
Power y=new Power(a,b);

//计算结果
System.out.println("y:"+y.calc());

//求偏导值
System.out.println("y/a:"+y.prtGrad(a));
System.out.println("y/b:"+y.prtGrad(b));

//结果
y:125.0
y/a:75.0
y/b:201.17973905426254
```

### 1.2.6 对数、自然对数函数
>Log类就是实现了对数函数的计算以及偏导梯度，代码如下：
```
//定义变量
Var a=new Var(5);
Var b=new Var(3);
//对数函数
Log y=new Log(a,b);

//计算结果
System.out.println("y:"+y.calc());

//求偏导值
System.out.println("y/a:"+y.prtGrad(a));
System.out.println("y/b:"+y.prtGrad(b));

//结果
y:125.0
y/a:75.0
y/b:201.17973905426254
```

>Ln类就是实现了自然对数函数的计算以及偏导梯度，代码如下：
```
//定义变量
Var a=new Var(5);
//自然对数函数
Ln y=new Ln(a);

//计算结果
System.out.println("y:"+y.calc());

//求偏导值
System.out.println("y/a:"+y.prtGrad(a));

//结果
y:1.6094379124341003
y/a:0.2
```

### 1.2.7 三角函数
>Sin类就是实现了三角函数sin的计算以及偏导梯度，代码如下：
```
//定义变量
Var a=new Var(5);
//sin函数
Sin y=new Sin(a);

//计算结果
System.out.println("y:"+y.calc());

//求偏导值
System.out.println("y/a:"+y.prtGrad(a));

//结果
y:-0.9589242746631385
y/a:0.28366218546322625
```
>本模块还提供Cot、Sec、Csc、ArcSin、ArcCos、ArcTan、ArcCot三角函数的计算以及偏导梯度，用法跟Sin类似。

### 1.2.8 复合函数
>Sigmoid类就是实现sigmoid函数的计算以及偏导梯度，代码如下：
```
//定义变量
Var a=new Var(5);
//sigmoid函数
Sigmoid y=new Sigmoid(a);

//计算结果
System.out.println("y:"+y.calc());

//求偏导值
System.out.println("y/a:"+y.prtGrad(a));

//结果
y:0.9933071490757153
y/a:0.006648056670790033
```
>本模块还提供Tanh函数的计算以及偏导梯度，用法跟Sigmoid类似。

## 1.3 自定义复合函数

### 1.3.1 自定义复合函数
>自定义复合函数并计算y对于每个a、b、c、d等的偏导结果，代码如下：
```
//定义变量
Var a=new Var(2);
Var b=new Var(3);
Var c=new Var(4);
Var d=new Var(5);

//自定义复杂函数
//u=a*b
//v=c/d;
//w=u-v;
//y=sin(w)
Mcl u=new Mcl(a,b);
Div v=new Div(c,d);
Sub w=new Sub(u,v);
Sin y=new Sin(w);

//计算结果
System.out.println("y:"+y.calc());

//求偏导梯度值
System.out.println("u/a:"+u.prtGrad(a));
System.out.println("u/b:"+u.prtGrad(b));
System.out.println("v/c:"+v.prtGrad(c));
System.out.println("v/d:"+v.prtGrad(d));
System.out.println("y/u:"+y.prtGrad(u));
System.out.println("y/v:"+y.prtGrad(v));
System.out.println("y/a:"+y.prtGrad(a));
System.out.println("y/b:"+y.prtGrad(b));
System.out.println("y/c:"+y.prtGrad(c));
System.out.println("y/d:"+y.prtGrad(d));

//结果
y:-0.8834546557201531
u/a:3.0
u/b:2.0
v/c:0.2
v/d:-0.16
y/u:0.4685166713003771
y/v:-0.4685166713003771
y/a:1.4055500139011312
y/b:0.9370333426007542
y/c:-0.09370333426007543
y/d:0.07496266740806033
```

## 1.4 例子

### 1.4.1 多项式拟合
>定义多项式y=ax^2+bx+c，传入一组数据让程序进行计算多项式的常量a、b、c，代码如下：

#### 定义需要训练的数组
```
//二维数组，第1列是x,第二列是y
double[][] datas=new double[][]{
        {32.3787591,32},
        {32.8252527,32.5},
        {33.3753966,33},
        {33.6624282,33.5},
        {33.9414867,34},
        {34.4039265,34.5},
        {34.7308236,35},
        {35.2331289,35.5},
        {35.5121874,36},
        {35.9188155,36.5},
        {36.5088249,37},
        {36.8038296,37.5},
        {36.9393723,38},
        {37.3539735,38.5},
        {38.0077677,39},
        {38.2629069,39.5},
        {38.749266,40},
        {39.1957596,40.5},
        {39.6342801,41},
        {40.0409082,41.5},
        {40.343886,42},
        {40.8701106,42.5},
        {41.2926849,43}
};
```

#### 定义变量
```
//将数据转成小数再进行计算
one(datas);

//更新率
double rate=0.05;

//x、y变量
Var x=new Var(0);
Var y=new Var(0);

//多项式a、b、c 3个参数
Var a=new Var(0.01);
Var b=new Var(0.01);
Var c=new Var(0.01);

//生成多项式函数
Function f=fx(x,a,b,c);

//生成损失函数
Function lf=lostFunc(f,y);
```
#### 循环更新a、b、c变量
```
//定义a、b、c 3个参数待更新的临时变量
double ta,tb,tc;

for(int i=0;i<10000;i++){
    //分别求lf/a、lf/b、lf/c的偏导函数在x和y处的值
    ta=rate*ds(datas,lf,y,x,a);
    tb=rate*ds(datas,lf,y,x,b);
    tc=rate*ds(datas,lf,y,x,c);

    //修改a、b、c参数的值
    a.setVal(a.getVal()-ta);
    b.setVal(b.getVal()-tb);
    c.setVal(c.getVal()-tc);
}
```
#### 输出结果
```
//输出a、b、c参数值
System.out.println("a:"+a.getVal()+" b:"+b.getVal()+" c:"+c.getVal()+"\n");

//使用a、b、c多项式参数对数据进行拟合
for(int i=0;i<datas.length;i++){
    x.setVal(datas[i][0]);
    System.out.println("y:"+datas[i][1]*100+"   y':"+f.calc()*100);
}
```
#### 将数据归一化，统一将数据除以100
```
public static void one(double[][] datas){
    for(int x=0;x<datas.length;x++){
        for(int y=0;y<datas[x].length;y++){
            datas[x][y]=datas[x][y]/100;
        }
    }
}
```
#### 生成多项式函数 y=ax^2+bx+c
```
/**
 * 生成函数 y=ax^2+bx+c
 * @param x
 * @param a
 * @param b
 * @param c
 * @return
 */
public static Function fx(Var x, Var a, Var b, Var c){
    Const two=new Const(2);
    Power x2=new Power(x,two);
    Mcl ax2=new Mcl(a,x2);
    Mcl bx=new Mcl(b,x);
    Add ax2_bx=new Add(ax2,bx);
    Add ax2_bx_c=new Add(ax2_bx,c);
    return ax2_bx_c;
}
```

#### 生成误差函数 lost=(1/2)*(y-fx)^2
```
/**
 * 生成误差函数 lost=(1/2)*(y-fx)^2
 * @param fx
 * @param y
 * @return
 */
public static Function lostFunc(Function fx,Var y){
    Const zeroPointFive=new Const(0.5);
    Const two=new Const(2);
    Sub diff=new Sub(y,fx);
    Power diff2=new Power(diff,two);
    Mcl ret=new Mcl(zeroPointFive,diff2);
    return ret;
}
```

#### 计算函数/变量的偏导
```
/**
 * 求偏导
 * @param datas
 * @param lf
 * @param y
 * @param x
 * @param dx
 * @return
 */
public static double ds(double[][] datas,Function lf,Var y,Var x,Var dx){
    double ret=0;
    for(double[] item:datas){
        x.setVal(item[0]);
        y.setVal(item[1]);
        //偏导梯度
        ret+=lf.prtGrad(dx);
    }
    return ret;
}
```

#### 输出结果
```
a:0.5957337603638155 b:0.8116429361248995 c:-0.004554090630554207

y:32.0   y':32.07015971059396
y:32.5   y':32.60599009307012
y:33.0   y':33.26947604418736
y:33.5   y':33.61707415072278
y:34.0   y':33.955957848348994
y:34.5   y':34.51957930836145
y:35.0   y':34.919538558322145
y:35.5   y':35.53659139587882
y:36.0   y':35.88069750274082
y:36.5   y':36.38377027998471
y:37.0   y':37.117222081977154
y:37.5   y':37.48550334444772
y:38.0   y':37.65506130990073
y:38.5   y':38.17506801527452
y:39.0   y':38.99923985328713
y:39.5   y':39.32224942127326
y:40.0   y':39.9401348313922
y:40.5   y':40.50985520354871
y:41.0   y':41.07171402144422
y:41.5   y':41.59475768546402
y:42.0   y':41.98575729076121
y:42.5   y':42.66746133299428
y:43.0   y':43.217278859814535
```

# 2 BP神经网络教程
## 2.1接口

>***Model***是模型接口
>
>***Layer***是层接口，层与层进行连接，每个层都有输入与输出，上一层的输出与下一层的输入进行连接
>
>***Loss***是损失函数接口，模型最后一层与损失函数进行连接

## 2.2 Model接口
>神经网络的模型统一操作接口

>模型实现类有：
>Sequential（序贯模型）

## 2.3 Layer接口
>神经网络的层统一操作接口

>层实现类有：
>Dense（全连接层）

## 2.4 Loss接口
>神经网络的损失统一操作接口

>损失函数实现类有：
>Mse（均方误差（MSE，mean squared error））
>Cel（交叉熵损失函数）

## 2.5 BP神经网络例子

#### 定义需要训练的数组
```
//请参数例子 test.TestNNRelu

//二维数组，第1列是x,第2列是y
double[][] datas=new double[][]{
        {32.3787591,32},
        {32.8252527,32.5},
        {33.3753966,33},
        {33.6624282,33.5},
        {33.9414867,34},
        {34.4039265,34.5},
        {34.7308236,35},
        {35.2331289,35.5},
        {35.5121874,36},
        {35.9188155,36.5},
        {36.5088249,37},
        {36.8038296,37.5},
        {36.9393723,38},
        {37.3539735,38.5},
        {38.0077677,39},
        {38.2629069,39.5},
        {38.749266,40},
        {39.1957596,40.5},
        {39.6342801,41},
        {40.0409082,41.5},
        {40.343886,42},
        {40.8701106,42.5},
        {41.2926849,43}
};
double[][] xx=new double[datas.length][1];
double[][] yy=new double[datas.length][1];
for(int i=0;i<datas.length;i++){
    xx[i][0]=datas[i][0]/100.0;
    yy[i][0]=datas[i][1]/100.0;
}
MultiDim[] x=arr2MultDim(xx);
MultiDim[] y=arr2MultDim(yy);

```

#### 定义模型
```
Model model=new Sequential();
model.add(new Dense(1,1,new Sgd(0.04), ()->new Relu()));
model.add(new Dropout(0.5f));
//识差函数
model.setLossCls(Mse.class);
model.init();

```
#### 训练
```
//训练模型
model.fit(x, y, 10000, 20, new LossCallBackListener() {
    @Override
    public void onLoss(double val) {
        System.out.println(String.format("%.10f", val));
    }
});
```

#### 预测
```
for(int i=0;i<x.length;i++){
    MultiDim py=model.predict(x[i]);

    ShapeIndex j=new ShapeIndex(py.getShape());
    do{
        System.out.print(String.format("%.10f   ", (double)py.getVal(j)*100));
    }while(j.next());

    j=new ShapeIndex(y[i].getShape());
    do{
        System.out.print(String.format("%.10f   ", (double)y[i].getVal(j)*100));
    }while(j.next());

    System.out.println();
}
```
### 输出
```
32.00   32.00   
32.56   32.50   
33.24   33.00   
33.60   33.50   
33.95   34.00   
34.53   34.50   
34.94   35.00   
35.56   35.50   
35.91   36.00   
36.42   36.50   
37.16   37.00   
37.53   37.50   
37.70   38.00   
38.21   38.50   
39.03   39.00   
39.35   39.50   
39.96   40.00   
40.51   40.50   
41.06   41.00   
41.57   41.50   
41.95   42.00   
42.60   42.50   
43.13   43.00  
```

## 2.6 卷积神经网络训练MNIST例子

#### 加载并加工MNIST数据
```
//请参数例子 test.mnist.MnistConvFit 以及 test.mnist.MnistConvPredict

double[][] train_images = MnistRead.getImages(MnistRead.TRAIN_IMAGES_FILE);
double[][] train_labels = one_hot(10,MnistRead.getLabels(MnistRead.TRAIN_LABELS_FILE));

double[][] test_images = MnistRead.getImages(MnistRead.TEST_IMAGES_FILE,100);
double[][] testlabels = one_hot(10,MnistRead.getLabels(MnistRead.TEST_LABELS_FILE,100));

MultiDim[] train_x=arr2MultDimThreeDim(train_images,new ThreeDimShape(28,28,1));
MultiDim[] train_y=arr2MultDim(train_labels);
MultiDim[] test_x=arr2MultDimThreeDim(test_images,new ThreeDimShape(28,28,1));
MultiDim[] test_y=arr2MultDim(testlabels);

```

#### 定义模型
```
Model model=new Sequential();
model.add(new Conv(
        (ThreeDimShape)train_x[0].getShape(),
        128,
        5,
        1,
        Padding.same,
        new Adam(0.001,0.9,0.999),
        () -> new LRelu(0.01),
        new InitParamsRandomGaussian()));

model.add(new Pooling(PollingType.max,2));

model.add(new Flatten());

model.add(new Dense(
        10,
        new Adam(0.001,0.9,0.999),
        () -> new NoneActivation()));

model.add(new Softmax());
//识差函数
model.setLossCls(Cel.class);
model.init();

```
#### 训练
```
model.fit(train_x, train_y, 1, 1,
    new LossCallBackListener() {
        @Override
        public void onLoss(double val) {
    //                        System.out.println(String.format("%.10f", val));
        }
    },
    new FitControl() {
    
        @Override
        public void onProcess(int process, int epoch, double currentProgress, double loss,long takeUpTime) {
            String lastProcessStr=process+"/"+epoch+
                    "    进度:"+String.format("%.2f %%", currentProgress*100)+
                    "    误差:"+String.format("%.10f", loss)
            ;
            System.out.print(lastProcessStr);
        }
    
        @Override
        public boolean onIsStop(int process,int epoch,double loss,long takeUpTime) {
            return false;
        }
    }
);
```

#### 预测
```
int errorNum=0;
for(int i=0;i<test_x.length;i++){
    MultiDim py=model.predict(test_x[i]);
    int pi=maxIndex((double[])py.getData());
    int yi=maxIndex((double[])test_y[i].getData());
    if(pi==yi){

    }else{
        errorNum++;
    }
}
double rate=((double)(test_x.length-errorNum))/test_x.length;
System.out.println(String.format("准确率：%.4f",rate));
```
### 输出
```
准确率：0.9812
```
