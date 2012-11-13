package com.taobao.common.jmx;

import javax.management.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TaoabaoJvmMXBeans {

    protected MBeanServerConnection mbsc;
    private final static Logger LOGGER = Logger.getLogger(TaoabaoJvmMXBeans.class.getName());

    public TaoabaoJvmMXBeans(MBeanServerConnection mbsc) {
        this.mbsc = mbsc;
    }

    public <T> T getTaobaoMXBean(String objectNameStr, Class<T> interfaceClass) {
        if (mbsc != null) {
            try {
                final ObjectName objName = new ObjectName(objectNameStr);
                if (!mbsc.isInstanceOf(objName, interfaceClass.getName())) {
                    throw new IllegalArgumentException(objectNameStr
                            + " is not an instance of " + interfaceClass);
                }
                // check if the registered MBean is a notification emitter
                boolean emitter = mbsc.isInstanceOf(objName, NOTIF_EMITTER);

                // create an MXBean proxy
                return JMX.newMXBeanProxy(mbsc, objName, interfaceClass,
                        emitter);
            } catch (InstanceNotFoundException e) {
                LOGGER.throwing(objectNameStr, " not found in the connection.", e); // NOI18N
            } catch (MalformedObjectNameException e) {
                LOGGER.throwing(objectNameStr, " is not a valid ObjectName format.", e); // NOI18N
            } catch (IOException e) {
                LOGGER.throwing(TaoabaoJvmMXBeans.class.getName(), "getMXBean", e); // NOI18N
            } catch (IllegalArgumentException iae) {
                LOGGER.log(Level.INFO, TaoabaoJvmMXBeans.class.getName() + ".getMXBean()", iae); // NOI18N                    
            }
        }
        return null;
    }
    private static final String NOTIF_EMITTER = "javax.management.NotificationEmitter";
}
