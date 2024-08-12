# `Synchronized` Keyword on Block/Method

## Synchronization on Method

When a method is declared as `synchronized`, it ensures that only one thread can execute the method at a time for a particular instance of the class. The thread must acquire the intrinsic lock of the object before entering the method and releases it upon exiting the method.

### Example:

```java
public class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
```

In the above example, the `increment` method is synchronized, which means that if multiple threads try to call this method on the same `Counter` instance, only one thread will be allowed to execute it at a time.

## Synchronization on Block

Instead of synchronizing the entire method, you can synchronize only a block of code within a method. This is useful when you want to limit the scope of synchronization to the critical section, potentially improving performance by reducing contention.

### Example:

```java
public class Counter {
    private int count = 0;

    public void increment() {
        synchronized (this) {
            count++;
        }
    }

    public int getCount() {
        return count;
    }
}
```

In this example, the critical section where `count` is incremented is synchronized. The intrinsic lock of the object (`this`) is acquired before entering the synchronized block and released after exiting it.

## Synchronization `Object` Level vs `Class` Level

### Object-Level Synchronization

Object-level synchronization ensures that the synchronized method or block is accessed by only one thread at a time for a particular instance of the class. This is useful when you want to protect instance variables from concurrent access.

### Example:

```java
public class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized void decrement() {
        count--;
    }

    public int getCount() {
        return count;
    }
}
```

Here, the `increment` and `decrement` methods are synchronized on the instance of the `Counter` class. If multiple threads are operating on different instances of `Counter`, they will not interfere with each other.

### Class-Level Synchronization

Class-level synchronization is used to control access to static methods or blocks, ensuring that only one thread can execute the synchronized static method or block for the entire class, regardless of the number of instances.

### Example:

```java
public class Counter {
    private static int count = 0;

    public static synchronized void increment() {
        count++;
    }

    public static synchronized void decrement() {
        count--;
    }

    public static int getCount() {
        return count;
    }
}
```

In this example, the `increment` and `decrement` methods are synchronized at the class level. The intrinsic lock of the `Counter` class is used, ensuring that only one thread can execute these methods for all instances of the class.

### Comparison:

- **Object-Level Synchronization**:
    - Uses the intrinsic lock of the object (`this`).
    - Applies to instance methods and instance-level synchronized blocks.
    - Useful for protecting instance variables.

- **Class-Level Synchronization**:
    - Uses the intrinsic lock of the `Class` object (e.g., `Counter.class`).
    - Applies to static methods and class-level synchronized blocks.
    - Useful for protecting static variables and ensuring class-level invariants.

