package org.moshe.arad.lockers;

import org.springframework.stereotype.Component;

@Component
public class SimpleLock {

	private Object objLock = new Object();
}
