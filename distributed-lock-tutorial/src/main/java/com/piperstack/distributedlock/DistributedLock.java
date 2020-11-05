package com.piperstack.distributedlock;

public interface DistributedLock {
    boolean tryLock();

    void lock();

    void unlock();
}
