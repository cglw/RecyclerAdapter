# RecyclerAdapter

极简的代码构建多样式类型的RecyclerView。将RecyclerView的列表样式实现极致解耦，可以轻松的增加新的列表样式，不用去修改其他额外的代码

## 基本原理

使用APT技术在编译期间生成代码模版，屏蔽创建类的过程和一些通用的代码逻辑。


注：APT(Annotation Processing Tool)，即 注解处理器，是javac中提供的编译时扫描和处理注解的工具




## 使用指南

### 配置

在项目的build.gradle配置
```
dependencies {
    annotationProcessor 'com.github.cglw.RecyclerAdapter:compiler:v1.0.0'
    implementation 'com.github.cglw.RecyclerAdapter:lib_adapter:v1.0.0'
    implementation 'com.github.cglw.RecyclerAdapter:annotations:v1.0.0'
}

android {

    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                //"ModuleApp"为自己定义的模块名称
                arguments = [moduleName: "ModuleApp"]
            }
        }
    }
}
```


### 使用
1. 创建模型类

```
public class TestBean {}
public class Test1Bean {}
```
2. 创建holder，添加AdapterCreate注解


```
//添加AdapterCreate注解用来自动生成adapter 类
@AdapterCreate
public class TestHolder extends BaseViewHolder<TestBean> {

    public TestHolder(@NonNull ViewGroup parent) {
        super(parent);
    }

    @Override
    public void bindData(TestBean model, int position) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_test;
    }
}

@AdapterCreate
public class Test2Holder extends BaseViewHolder<Test2Bean> {
    public Test2Holder(@NonNull ViewGroup parent) {
        super(parent);
        setOnClickListener(R.id.test, v -> {
            Toast.makeText(v.getContext(), "test", Toast.LENGTH_SHORT).show();
        });
    }
    @Override
    public void bindData(Test2Bean model, int position) {
        
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_test2;
    }
}

```
3. build一下项目，会自动生成适配器和工厂类ModuleAppAdapterHelper（生成的adapter名字取自上面配置的ModuleName，生成的类在build/generated/ap_generated_source），直接使用


```
 MultiTypeAdapter defaultAdapter = ModuleAppAdapterHelper.getDefaultAdapter(this);
 recyclerView.setAdapter(defaultAdapter);
 for (int i = 0; i < 10; i++) {
    datas.add(new TestBean());
 }
 for (int i = 0; i < 10; i++) {
    datas.add(new Test2Bean());
 }
 defaultAdapter.addAll(datas);

```


## 高级使用
1. 一个模型对应多个类型显示样式

```
public class Test2Bean {
    public int type;
}

@OneToManyFactoryCreate
public class TestOneToManyCreate extends OneToManyHolderCreate<Test2Bean> {
    @Override
    public Class<? extends RecyclerView.ViewHolder> getHolderClass(Test2Bean model) {
        if(model.type==0){
            return Test2Holder.class;
        }
        return Test2OtherHolder.class;
    }
}
```

2. 分组概念

将holder分组，指定同组的holder注册到同组adapter,
，excludeDefault代表去掉默认分组

```
//默认带有default分组，这个会生成 ModuleAppDefaultAdapter,ModuleAppBadAdapter,注册Test4Holder

@AdapterCreate(group ={"bad"})
public class Tes4Holder extends BaseViewHolder<Test4Bean> {}



//这个会生成 ModuleAppGoodAdapter，注册Test3Holder

@AdapterCreate(group ={"good"},excludeDefault = true)
public class Test3Holder extends BaseViewHolder<Test3Bean> {}



//这个会生成 ModuleAppTestAdapter，注册TestOneToManyCreate

@OneToManyFactoryCreate(group = "test",excludeDefault = true)
public class TestOneToManyCreate extends OneToManyHolderCreate<Test2Bean> {}

```
3.adapter传参


```
//adapter 设置一个object
defaultAdapter.setAdapterParams(new SimpleParams(100));

//holder可以直接拿到adapter的参数对象
public Test2Holder(@NonNull ViewGroup parent) {
        super(parent);
        setOnClickListener(R.id.test, v -> {
            SimpleParams adapterParams = getAdapterParams();
            Toast.makeText(v.getContext(), "test"+adapterParams.index, Toast.LENGTH_SHORT).show();
        });
    }

```
