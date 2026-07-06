// 
// Decompiled by Procyon v0.6.0
// 

package org.slf4j;

import org.slf4j.helpers.Util;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.impl.StaticMarkerBinder;

public class MarkerFactory
{
    static IMarkerFactory MARKER_FACTORY;
    
    private MarkerFactory() {
    }
    
    private static IMarkerFactory bwCompatibleGetMarkerFactoryFromBinder() throws NoClassDefFoundError {
        try {
            return StaticMarkerBinder.getSingleton().getMarkerFactory();
        }
        catch (final NoSuchMethodError nsme) {
            return StaticMarkerBinder.SINGLETON.getMarkerFactory();
        }
    }
    
    public static Marker getMarker(final String name) {
        return MarkerFactory.MARKER_FACTORY.getMarker(name);
    }
    
    public static Marker getDetachedMarker(final String name) {
        return MarkerFactory.MARKER_FACTORY.getDetachedMarker(name);
    }
    
    public static IMarkerFactory getIMarkerFactory() {
        return MarkerFactory.MARKER_FACTORY;
    }
    
    static {
        try {
            MarkerFactory.MARKER_FACTORY = bwCompatibleGetMarkerFactoryFromBinder();
        }
        catch (final NoClassDefFoundError e) {
            MarkerFactory.MARKER_FACTORY = new BasicMarkerFactory();
        }
        catch (final Exception e2) {
            Util.report("Unexpected failure while binding MarkerFactory", e2);
        }
    }
}
