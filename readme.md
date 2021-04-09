quick-tools
---

jar工具包集合

### 1. quick-beans

基于Cglib的bean拷贝，更简单的使用姿势，更高效的拷贝性能

使用姿势如下

**基本转换**

```java
// 源 与 目标类 属性名，类型完全一致
Source source = genSource();
SameTarget target1_1 = BeanUtils.copy(source, SameTarget.class);
```

**驼峰下划线互转**

```java
CamelTarget target2_1 = BeanUtils.copy(source, CamelTarget.class, true);
```

**类型适配**

```java
AtomicTarget atomicTarget = BeanUtils.copy(source, AtomicTarget.class, true,
                new ComposeConverter().addConverter(new AtomicIntegerConverter()).camelEnable(true));
```

性能测试对比如下 `com.github.liuyueyi.tools.bean.test.PerformanceTest.testPerformance`

```bash

1w -------- cost: StopWatch '': running time = 535799400 ns
---------------------------------------------
ns         %     Task name
---------------------------------------------
497867300  093%  apacheCopy
028106700  005%  huToolCopy
004424500  001%  pureCglibCopy
005400900  001%  quickCopy

10w -------- cost: StopWatch '': running time = 4713797900 ns
---------------------------------------------
ns         %     Task name
---------------------------------------------
4406643200  093%  apacheCopy
232082600  005%  huToolCopy
038151700  001%  pureCglibCopy
036920400  001%  quickCopy

20w -------- cost: StopWatch '': running time = 9443726900 ns
---------------------------------------------
ns         %     Task name
---------------------------------------------
8827939100  093%  apacheCopy
464801400  005%  huToolCopy
076975000  001%  pureCglibCopy
074011400  001%  quickCopy

50w -------- cost: StopWatch '': running time = 23557446800 ns
---------------------------------------------
ns         %     Task name
---------------------------------------------
22024798800  093%  apacheCopy
1161052200  005%  huToolCopy
189941900  001%  pureCglibCopy
181653900  001%  quickCopy

100w -------- cost: StopWatch '': running time = 47018109400 ns
---------------------------------------------
ns         %     Task name
---------------------------------------------
43928376500  093%  apacheCopy
2323689500  005%  huToolCopy
390549600  001%  pureCglibCopy
375493800  001%  quickCopy
```