package com.piperstack.distributedlock.mysql;

import com.piperstack.distributedlock.DistributedLock;

public class MySQLDistributedLock implements DistributedLock {
    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public void lock() {

    }

    @Override
    public void unlock() {

    }
}
