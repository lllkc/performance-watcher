# performance-watcher
一个spring小组件，根据配置动态添加aop，记录方法出入参、执行耗时

# usage

1.在配置文件中配置目标package

watch.packages=com.abc.pac1,com.abc.pac2

watch.threshold=10（方法执行耗时的阈值，低于此阈值打印debug级别日志，否则打印info级别日志）

2.在目标bean上添加注解

@Watch(threshold = 10)

3.在目标方法处添加注解

@Watch(threshold = 10)

__优先级：配置3>配置2>配置1__
---
## 其他配置
全局开关：watch.enabled=true/false

logger设置：watch.useDynamicLogger=true/false
