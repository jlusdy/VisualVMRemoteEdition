/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taobao.common.jmx;

import com.sun.tools.visualvm.application.jvm.HeapHistogram;

import java.util.*;

/**
 *
 * @author shutong.dy
 */
public class TaobaoHeapHistogramImpl extends HeapHistogram {
    private static final String BOOLEAN_TEXT = "boolean"; // NOI18N
    private static final String CHAR_TEXT = "char"; // NOI18N
    private static final String BYTE_TEXT = "byte"; // NOI18N
    private static final String SHORT_TEXT = "short"; // NOI18N
    private static final String INT_TEXT = "int"; // NOI18N
    private static final String LONG_TEXT = "long"; // NOI18N
    private static final String FLOAT_TEXT = "float"; // NOI18N
    private static final String DOUBLE_TEXT = "double"; // NOI18N
    private static final String VOID_TEXT = "void"; // NOI18N
    private static final char BOOLEAN_CODE = 'Z'; // NOI18N
    private static final char CHAR_CODE = 'C'; // NOI18N
    private static final char BYTE_CODE = 'B'; // NOI18N
    private static final char SHORT_CODE = 'S'; // NOI18N
    private static final char INT_CODE = 'I'; // NOI18N
    private static final char LONG_CODE = 'J'; // NOI18N
    private static final char FLOAT_CODE = 'F'; // NOI18N
    private static final char DOUBLE_CODE = 'D'; // NOI18N
    private static final char OBJECT_CODE = 'L'; // NOI18N
    private static final Map<String,String> permGenNames = new HashMap();
    static {
        permGenNames.put("<methodKlass>","Read-Write Method Metadata");
        permGenNames.put("<constMethodKlass>","Read-Only Method Metadata");
        permGenNames.put("<methodDataKlass>","Method Profiling Information");
        permGenNames.put("<constantPoolKlass>","Constant Pool Metadata");
        permGenNames.put("<constantPoolCacheKlass>","Class Resolution Optimization Metadata");
        permGenNames.put("<symbolKlass>","VM Symbol Metadata");
        permGenNames.put("<compiledICHolderKlass>","Inline Cache Metadata");
        permGenNames.put("<instanceKlassKlass>","Instance Class Metadata");
        permGenNames.put("<objArrayKlassKlass>","Object Array Class Metadata");
        permGenNames.put("<typeArrayKlassKlass>","Scalar Array Class Metadata");
        permGenNames.put("<klassKlass>","Base Class Metadata");
        permGenNames.put("<arrayKlassKlass>","Base Array Class Metadata");
    }
    Set<ClassInfo> classes;
    Set<ClassInfo> permGenClasses;
    Date time;
    long totalBytes;
    long totalInstances;
    long totalHeapBytes;
    long totalHeapInstances;
    long totalPermGenBytes;
    long totalPermgenInstances;
    
    TaobaoHeapHistogramImpl() {
    }
    
    public TaobaoHeapHistogramImpl(String in) {
        Map<String,ClassInfoImpl> classesMap = new HashMap(1024);
        Map<String,ClassInfoImpl> permGenMap = new HashMap(1024);
        time = new Date();
        Scanner sc = new Scanner(in);  // NOI18N
        sc.useRadix(10);
        sc.nextLine();
        sc.nextLine();
        sc.skip("-+");
        sc.nextLine();

        while(sc.hasNext("[0-9]+:")) {  // NOI18N
            ClassInfoImpl newClInfo = new ClassInfoImpl(sc);
            if (newClInfo.isPermGen()) {
                storeClassInfo(newClInfo, permGenMap);
                totalPermGenBytes += newClInfo.getBytes();
                totalPermgenInstances += newClInfo.getInstancesCount();
            } else {
                storeClassInfo(newClInfo, classesMap);
                totalHeapBytes += newClInfo.getBytes();
                totalHeapInstances += newClInfo.getInstancesCount();                
            }
        }
        sc.next("Total");   // NOI18N
        totalInstances = sc.nextLong();
        totalBytes = sc.nextLong();
        classes = new HashSet(classesMap.values());
        permGenClasses = new HashSet(permGenMap.values());
    }

    void storeClassInfo(final ClassInfoImpl newClInfo, final Map<String, ClassInfoImpl> map) {
        ClassInfoImpl oldClInfo = map.get(newClInfo.getName());
        if (oldClInfo == null) {
            map.put(newClInfo.getName(),newClInfo);
        } else {
            oldClInfo.bytes += newClInfo.getBytes();
            oldClInfo.instances += newClInfo.getInstancesCount();               
        }
    }
    
    public Date getTime() {
        return (Date) time.clone();
    }
    
    public Set<ClassInfo> getHeapHistogram() {
        return classes;
    }
    
    public long getTotalInstances() {
        return totalInstances;
    }
    
    public long getTotalBytes() {
        return totalBytes;
    }

    public long getTotalHeapInstances() {
        return totalHeapInstances;
    }

    public long getTotalHeapBytes() {
        return totalHeapBytes;
    }

    public Set<ClassInfo> getPermGenHistogram() {
        return permGenClasses;
    }

    public long getTotalPerGenInstances() {
        return totalPermgenInstances;
    }

    public long getTotalPermGenHeapBytes() {
        return totalPermGenBytes;
    }
    
    static class ClassInfoImpl extends ClassInfo {
        long instances;
        long bytes;
        String name;
        boolean permGen;
        
        ClassInfoImpl() {
        }
        
        ClassInfoImpl(Scanner sc) {
            String jvmName;
            
            sc.next();
            instances = sc.nextLong();
            bytes = sc.nextLong();
            jvmName = sc.next();
            permGen = jvmName.charAt(0) == '<';
            name = convertJVMName(jvmName);
        }
        
        public String getName() {
            return name;
        }
        
        public long getInstancesCount() {
            return instances;
        }
        
        public long getBytes() {
            return bytes;
        }


        public int hashCode() {
            return getName().hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof ClassInfoImpl) {
                return getName().equals(((ClassInfoImpl)obj).getName());
            }
            return false;
        }


        private boolean isPermGen() {
            return permGen;
        }
        
        String convertJVMName(String jvmName) {
            String name = null;
            int index = jvmName.lastIndexOf('[');
            
            if (index != -1) {
                switch(jvmName.charAt(index+1)) {
                    case BOOLEAN_CODE:
                        name=BOOLEAN_TEXT;
                        break;
                    case CHAR_CODE:
                        name=CHAR_TEXT;
                        break;
                    case BYTE_CODE:
                        name=BYTE_TEXT;
                        break;
                    case SHORT_CODE:
                        name=SHORT_TEXT;
                        break;
                    case INT_CODE:
                        name=INT_TEXT;
                        break;
                    case LONG_CODE:
                        name=LONG_TEXT;
                        break;
                    case FLOAT_CODE:
                        name=FLOAT_TEXT;
                        break;
                    case DOUBLE_CODE:
                        name=DOUBLE_TEXT;
                        break;
                    case OBJECT_CODE:
                        name=jvmName.substring(index+2,jvmName.length()-1);
                        break;
                    default:
                        System.err.println("Uknown name "+jvmName);
                        name = jvmName;
                }
                for (int i=0;i<=index;i++) {
                    name+="[]";
                }
            } else if (isPermGen()) {
                name = permGenNames.get(jvmName);
            }
            if (name == null) {
                name = jvmName;
            }
            return name.intern();
        }
    }
    
}
