// 
// Decompiled by Procyon v0.6.0
// 

package org.slf4j.spi;

import org.slf4j.ILoggerFactory;

public interface LoggerFactoryBinder
{
    ILoggerFactory getLoggerFactory();
    
    String getLoggerFactoryClassStr();
}
