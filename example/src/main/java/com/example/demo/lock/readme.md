6.锁升级过程，轻量锁可以变成偏向锁么，偏向锁可以变成无锁么，自旋锁，对象头结构，锁状态变化过程


### ReentrantLock
而 ReentrantLock 就是一个普通的类，
它是基于 AQS(AbstractQueuedSynchronizer)来实现的。


#### AQS


##### CAS


