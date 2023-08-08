package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.common.net.NetResult;
import com.common.uitl.Constant;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.http.message.TokenParser;

/* loaded from: classes.dex */
public class TypeUtils {
    public static boolean compatibleWithJavaBean = false;
    private static volatile Map<Class, String[]> kotlinIgnores = null;
    private static volatile boolean kotlinIgnores_error = false;
    private static volatile boolean kotlin_class_klass_error = false;
    private static volatile boolean kotlin_error = false;
    private static volatile Constructor kotlin_kclass_constructor = null;
    private static volatile Method kotlin_kclass_getConstructors = null;
    private static volatile Method kotlin_kfunction_getParameters = null;
    private static volatile Method kotlin_kparameter_getName = null;
    private static volatile Class kotlin_metadata = null;
    private static volatile boolean kotlin_metadata_error = false;
    private static final ConcurrentMap<String, Class<?>> mappings;
    private static boolean setAccessibleEnable = true;

    public static boolean isKotlin(Class cls) {
        if (kotlin_metadata == null && !kotlin_metadata_error) {
            try {
                kotlin_metadata = Class.forName("kotlin.Metadata");
            } catch (Throwable unused) {
                kotlin_metadata_error = true;
            }
        }
        if (kotlin_metadata == null) {
            return false;
        }
        return cls.isAnnotationPresent(kotlin_metadata);
    }

    private static boolean isKotlinIgnore(Class cls, String str) {
        String[] strArr;
        if (kotlinIgnores == null && !kotlinIgnores_error) {
            try {
                HashMap hashMap = new HashMap();
                hashMap.put(Class.forName("kotlin.ranges.CharRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.IntRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.LongRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.ClosedFloatRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.ClosedDoubleRange"), new String[]{"getEndInclusive", "isEmpty"});
                kotlinIgnores = hashMap;
            } catch (Throwable unused) {
                kotlinIgnores_error = true;
            }
        }
        return (kotlinIgnores == null || (strArr = kotlinIgnores.get(cls)) == null || Arrays.binarySearch(strArr, str) < 0) ? false : true;
    }

    public static String[] getKoltinConstructorParameters(Class cls) {
        if (kotlin_kclass_constructor == null && !kotlin_class_klass_error) {
            try {
                Class<?> cls2 = Class.forName("kotlin.reflect.jvm.internal.KClassImpl");
                kotlin_kclass_constructor = cls2.getConstructor(Class.class);
                kotlin_kclass_getConstructors = cls2.getMethod("getConstructors", new Class[0]);
                kotlin_kfunction_getParameters = Class.forName("kotlin.reflect.KFunction").getMethod("getParameters", new Class[0]);
                kotlin_kparameter_getName = Class.forName("kotlin.reflect.KParameter").getMethod("getName", new Class[0]);
            } catch (Throwable unused) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kclass_constructor == null || kotlin_error) {
            return null;
        }
        try {
            Iterator it = ((Iterable) kotlin_kclass_getConstructors.invoke(kotlin_kclass_constructor.newInstance(cls), new Object[0])).iterator();
            Object obj = null;
            while (it.hasNext()) {
                Object next = it.next();
                List list = (List) kotlin_kfunction_getParameters.invoke(next, new Object[0]);
                if (obj == null || list.size() != 0) {
                    obj = next;
                }
                it.hasNext();
            }
            List list2 = (List) kotlin_kfunction_getParameters.invoke(obj, new Object[0]);
            String[] strArr = new String[list2.size()];
            for (int i = 0; i < list2.size(); i++) {
                strArr[i] = (String) kotlin_kparameter_getName.invoke(list2.get(i), new Object[0]);
            }
            return strArr;
        } catch (Throwable unused2) {
            kotlin_error = true;
            return null;
        }
    }

    public static final String castToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static final Byte castToByte(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Byte.valueOf(((Number) obj).byteValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str)) {
                return null;
            }
            return Byte.valueOf(Byte.parseByte(str));
        }
        throw new JSONException("can not cast to byte, value : " + obj);
    }

    public static final Character castToChar(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Character) {
            return (Character) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0) {
                return null;
            }
            if (str.length() != 1) {
                throw new JSONException("can not cast to byte, value : " + obj);
            }
            return Character.valueOf(str.charAt(0));
        }
        throw new JSONException("can not cast to byte, value : " + obj);
    }

    public static final Short castToShort(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Short.valueOf(((Number) obj).shortValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str)) {
                return null;
            }
            return Short.valueOf(Short.parseShort(str));
        }
        throw new JSONException("can not cast to short, value : " + obj);
    }

    public static final BigDecimal castToBigDecimal(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof BigInteger) {
            return new BigDecimal((BigInteger) obj);
        }
        String obj2 = obj.toString();
        if (obj2.length() == 0 || "null".equals(obj2)) {
            return null;
        }
        return new BigDecimal(obj2);
    }

    public static final BigInteger castToBigInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigInteger) {
            return (BigInteger) obj;
        }
        if ((obj instanceof Float) || (obj instanceof Double)) {
            return BigInteger.valueOf(((Number) obj).longValue());
        }
        String obj2 = obj.toString();
        if (obj2.length() == 0 || "null".equals(obj2)) {
            return null;
        }
        return new BigInteger(obj2);
    }

    public static final Float castToFloat(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Float.valueOf(((Number) obj).floatValue());
        }
        if (obj instanceof String) {
            String obj2 = obj.toString();
            if (obj2.length() == 0 || "null".equals(obj2)) {
                return null;
            }
            return Float.valueOf(Float.parseFloat(obj2));
        }
        throw new JSONException("can not cast to float, value : " + obj);
    }

    public static final Double castToDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Double.valueOf(((Number) obj).doubleValue());
        }
        if (obj instanceof String) {
            String obj2 = obj.toString();
            if (obj2.length() == 0 || "null".equals(obj2)) {
                return null;
            }
            return Double.valueOf(Double.parseDouble(obj2));
        }
        throw new JSONException("can not cast to double, value : " + obj);
    }

    public static final Date castToDate(Object obj) {
        String str;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Calendar) {
            return ((Calendar) obj).getTime();
        }
        if (obj instanceof Date) {
            return (Date) obj;
        }
        long longValue = obj instanceof Number ? ((Number) obj).longValue() : -1L;
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.indexOf(45) != -1) {
                if (str2.length() == JSON.DEFFAULT_DATE_FORMAT.length()) {
                    str = JSON.DEFFAULT_DATE_FORMAT;
                } else if (str2.length() == 10) {
                    str = Constant.STRING_DAY_FORMAT2;
                } else if (str2.length() == 19) {
                    str = Constant.STRING_DAY_FORMAT4;
                } else {
                    str = (str2.length() == 29 && str2.charAt(26) == ':' && str2.charAt(28) == '0') ? "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" : "yyyy-MM-dd HH:mm:ss.SSS";
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, JSON.defaultLocale);
                simpleDateFormat.setTimeZone(JSON.defaultTimeZone);
                try {
                    return simpleDateFormat.parse(str2);
                } catch (ParseException unused) {
                    throw new JSONException("can not cast to Date, value : " + str2);
                }
            } else if (str2.length() == 0 || "null".equals(str2)) {
                return null;
            } else {
                longValue = Long.parseLong(str2);
            }
        }
        if (longValue < 0) {
            throw new JSONException("can not cast to Date, value : " + obj);
        }
        return new Date(longValue);
    }

    public static final Long castToLong(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Long.valueOf(((Number) obj).longValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str)) {
                return null;
            }
            try {
                return Long.valueOf(Long.parseLong(str));
            } catch (NumberFormatException unused) {
                JSONLexer jSONLexer = new JSONLexer(str);
                Calendar calendar = jSONLexer.scanISO8601DateIfMatch(false) ? jSONLexer.calendar : null;
                jSONLexer.close();
                if (calendar != null) {
                    return Long.valueOf(calendar.getTimeInMillis());
                }
            }
        }
        throw new JSONException("can not cast to long, value : " + obj);
    }

    public static final Integer castToInt(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof Number) {
            return Integer.valueOf(((Number) obj).intValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str)) {
                return null;
            }
            return Integer.valueOf(Integer.parseInt(str));
        }
        throw new JSONException("can not cast to int, value : " + obj);
    }

    public static final byte[] castToBytes(Object obj) {
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            return JSONLexer.decodeFast(str, 0, str.length());
        }
        throw new JSONException("can not cast to int, value : " + obj);
    }

    public static final Boolean castToBoolean(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof Number) {
            return Boolean.valueOf(((Number) obj).intValue() == 1);
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str)) {
                return null;
            }
            if ("true".equalsIgnoreCase(str) || "1".equals(str)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(str) || NetResult.CODE_OK.equals(str)) {
                return Boolean.FALSE;
            }
        }
        throw new JSONException("can not cast to int, value : " + obj);
    }

    public static final <T> T castToJavaBean(Object obj, Class<T> cls) {
        return (T) cast(obj, (Class<Object>) cls, ParserConfig.global);
    }

    public static final <T> T cast(Object obj, Class<T> cls, ParserConfig parserConfig) {
        return (T) cast(obj, cls, parserConfig, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> T cast(Object obj, Class<T> cls, ParserConfig parserConfig, int i) {
        T t;
        if (obj == 0) {
            return null;
        }
        if (cls == null) {
            throw new IllegalArgumentException("clazz is null");
        }
        if (cls == obj.getClass()) {
            return obj;
        }
        if (obj instanceof Map) {
            if (cls == Map.class) {
                return obj;
            }
            Map map = (Map) obj;
            return (cls != Object.class || map.containsKey(JSON.DEFAULT_TYPE_KEY)) ? (T) castToJavaBean(map, cls, parserConfig, i) : obj;
        }
        int i2 = 0;
        if (cls.isArray()) {
            if (obj instanceof Collection) {
                Collection<Object> collection = (Collection) obj;
                T t2 = (T) Array.newInstance(cls.getComponentType(), collection.size());
                for (Object obj2 : collection) {
                    Array.set(t2, i2, cast(obj2, (Class<Object>) cls.getComponentType(), parserConfig));
                    i2++;
                }
                return t2;
            } else if (cls == byte[].class) {
                return (T) castToBytes(obj);
            }
        }
        if (cls.isAssignableFrom(obj.getClass())) {
            return obj;
        }
        if (cls == Boolean.TYPE || cls == Boolean.class) {
            return (T) castToBoolean(obj);
        }
        if (cls == Byte.TYPE || cls == Byte.class) {
            return (T) castToByte(obj);
        }
        if ((cls == Character.TYPE || cls == Character.class) && (obj instanceof String)) {
            String str = (String) obj;
            if (str.length() == 1) {
                return (T) Character.valueOf(str.charAt(0));
            }
        }
        if (cls == Short.TYPE || cls == Short.class) {
            return (T) castToShort(obj);
        }
        if (cls == Integer.TYPE || cls == Integer.class) {
            return (T) castToInt(obj);
        }
        if (cls == Long.TYPE || cls == Long.class) {
            return (T) castToLong(obj);
        }
        if (cls == Float.TYPE || cls == Float.class) {
            return (T) castToFloat(obj);
        }
        if (cls == Double.TYPE || cls == Double.class) {
            return (T) castToDouble(obj);
        }
        if (cls == String.class) {
            return (T) castToString(obj);
        }
        if (cls == BigDecimal.class) {
            return (T) castToBigDecimal(obj);
        }
        if (cls == BigInteger.class) {
            return (T) castToBigInteger(obj);
        }
        if (cls == Date.class) {
            return (T) castToDate(obj);
        }
        if (cls.isEnum()) {
            return (T) castToEnum(obj, cls, parserConfig);
        }
        if (Calendar.class.isAssignableFrom(cls)) {
            Date castToDate = castToDate(obj);
            if (cls == Calendar.class) {
                t = (T) Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
            } else {
                try {
                    t = (T) ((Calendar) cls.newInstance());
                } catch (Exception e) {
                    throw new JSONException("can not cast to : " + cls.getName(), e);
                }
            }
            ((Calendar) t).setTime(castToDate);
            return t;
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.length() == 0 || "null".equals(str2)) {
                return null;
            }
            if (cls == Currency.class) {
                return (T) Currency.getInstance(str2);
            }
        }
        throw new JSONException("can not cast to : " + cls.getName());
    }

    public static final <T> T castToEnum(Object obj, Class<T> cls, ParserConfig parserConfig) {
        try {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0) {
                    return null;
                }
                return (T) Enum.valueOf(cls, str);
            }
            if (obj instanceof Number) {
                int intValue = ((Number) obj).intValue();
                T[] enumConstants = cls.getEnumConstants();
                if (intValue < enumConstants.length) {
                    return enumConstants[intValue];
                }
            }
            throw new JSONException("can not cast to : " + cls.getName());
        } catch (Exception e) {
            throw new JSONException("can not cast to : " + cls.getName(), e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> T cast(Object obj, Type type, ParserConfig parserConfig) {
        if (obj == 0) {
            return null;
        }
        if (type instanceof Class) {
            return (T) cast(obj, (Class<Object>) type, parserConfig);
        }
        if (type instanceof ParameterizedType) {
            return (T) cast(obj, (ParameterizedType) type, parserConfig);
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str)) {
                return null;
            }
        }
        if (type instanceof TypeVariable) {
            return obj;
        }
        throw new JSONException("can not cast to : " + type);
    }

    /* JADX WARN: Type inference failed for: r7v8, types: [T, java.util.Map, java.util.HashMap] */
    public static final <T> T cast(Object obj, ParameterizedType parameterizedType, ParserConfig parserConfig) {
        T t;
        Type rawType = parameterizedType.getRawType();
        if (rawType == Set.class || rawType == HashSet.class || rawType == TreeSet.class || rawType == List.class || rawType == ArrayList.class) {
            Type type = parameterizedType.getActualTypeArguments()[0];
            if (obj instanceof Iterable) {
                if (rawType == Set.class || rawType == HashSet.class) {
                    t = (T) new HashSet();
                } else if (rawType == TreeSet.class) {
                    t = (T) new TreeSet();
                } else {
                    t = (T) new ArrayList();
                }
                for (T t2 : (Iterable) obj) {
                    ((Collection) t).add(cast(t2, type, parserConfig));
                }
                return t;
            }
        }
        if (rawType == Map.class || rawType == HashMap.class) {
            Type type2 = parameterizedType.getActualTypeArguments()[0];
            Type type3 = parameterizedType.getActualTypeArguments()[1];
            if (obj instanceof Map) {
                ?? r7 = (T) new HashMap();
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    r7.put(cast(entry.getKey(), type2, parserConfig), cast(entry.getValue(), type3, parserConfig));
                }
                return r7;
            }
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str)) {
                return null;
            }
        }
        if (parameterizedType.getActualTypeArguments().length == 1 && (parameterizedType.getActualTypeArguments()[0] instanceof WildcardType)) {
            return (T) cast(obj, rawType, parserConfig);
        }
        throw new JSONException("can not cast to : " + parameterizedType);
    }

    public static final <T> T castToJavaBean(Map<String, Object> map, Class<T> cls, ParserConfig parserConfig) {
        return (T) castToJavaBean(map, cls, parserConfig, 0);
    }

    public static final <T> T castToJavaBean(Map<String, Object> map, Class<T> cls, ParserConfig parserConfig, int i) {
        JSONObject jSONObject;
        int i2 = 0;
        try {
            if (cls == StackTraceElement.class) {
                String str = (String) map.get("className");
                String str2 = (String) map.get("methodName");
                String str3 = (String) map.get("fileName");
                Number number = (Number) map.get("lineNumber");
                if (number != null) {
                    i2 = number.intValue();
                }
                return (T) new StackTraceElement(str, str2, str3, i2);
            }
            Object obj = map.get(JSON.DEFAULT_TYPE_KEY);
            if (obj instanceof String) {
                String str4 = (String) obj;
                if (parserConfig == null) {
                    parserConfig = ParserConfig.global;
                }
                Class<?> checkAutoType = parserConfig.checkAutoType(str4, null, i);
                if (checkAutoType == null) {
                    throw new ClassNotFoundException(str4 + " not found");
                } else if (!checkAutoType.equals(cls)) {
                    return (T) castToJavaBean(map, checkAutoType, parserConfig, i);
                }
            }
            if (cls.isInterface()) {
                if (map instanceof JSONObject) {
                    jSONObject = (JSONObject) map;
                } else {
                    jSONObject = new JSONObject(map);
                }
                if (parserConfig == null) {
                    parserConfig = ParserConfig.getGlobalInstance();
                }
                return parserConfig.getDeserializer(cls) != null ? (T) JSON.parseObject(JSON.toJSONString(jSONObject), cls) : (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{cls}, jSONObject);
            } else if (cls == String.class && (map instanceof JSONObject)) {
                return (T) map.toString();
            } else {
                if (parserConfig == null) {
                    parserConfig = ParserConfig.global;
                }
                ObjectDeserializer deserializer = parserConfig.getDeserializer(cls);
                JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
                if (javaBeanDeserializer == null) {
                    throw new JSONException("can not get javaBeanDeserializer");
                }
                return (T) javaBeanDeserializer.createInstance(map, parserConfig);
            }
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    static {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(36, 0.75f, 1);
        mappings = concurrentHashMap;
        concurrentHashMap.put("byte", Byte.TYPE);
        concurrentHashMap.put("short", Short.TYPE);
        concurrentHashMap.put("int", Integer.TYPE);
        concurrentHashMap.put("long", Long.TYPE);
        concurrentHashMap.put("float", Float.TYPE);
        concurrentHashMap.put("double", Double.TYPE);
        concurrentHashMap.put("boolean", Boolean.TYPE);
        concurrentHashMap.put("char", Character.TYPE);
        concurrentHashMap.put("[byte", byte[].class);
        concurrentHashMap.put("[short", short[].class);
        concurrentHashMap.put("[int", int[].class);
        concurrentHashMap.put("[long", long[].class);
        concurrentHashMap.put("[float", float[].class);
        concurrentHashMap.put("[double", double[].class);
        concurrentHashMap.put("[boolean", boolean[].class);
        concurrentHashMap.put("[char", char[].class);
        concurrentHashMap.put("[B", byte[].class);
        concurrentHashMap.put("[S", short[].class);
        concurrentHashMap.put("[I", int[].class);
        concurrentHashMap.put("[J", long[].class);
        concurrentHashMap.put("[F", float[].class);
        concurrentHashMap.put("[D", double[].class);
        concurrentHashMap.put("[C", char[].class);
        concurrentHashMap.put("[Z", boolean[].class);
        concurrentHashMap.put("java.util.HashMap", HashMap.class);
        concurrentHashMap.put("java.util.TreeMap", TreeMap.class);
        concurrentHashMap.put("java.util.Date", Date.class);
        concurrentHashMap.put("com.alibaba.fastjson.JSONObject", JSONObject.class);
        concurrentHashMap.put("java.util.concurrent.ConcurrentHashMap", ConcurrentHashMap.class);
        concurrentHashMap.put("java.text.SimpleDateFormat", SimpleDateFormat.class);
        concurrentHashMap.put("java.lang.StackTraceElement", StackTraceElement.class);
        concurrentHashMap.put("java.lang.RuntimeException", RuntimeException.class);
    }

    public static Class<?> getClassFromMapping(String str) {
        return mappings.get(str);
    }

    public static Class<?> loadClass(String str, ClassLoader classLoader) {
        return loadClass(str, classLoader, true);
    }

    public static Class<?> loadClass(String str, ClassLoader classLoader, boolean z) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (str.length() >= 256) {
            throw new JSONException("className too long. " + str);
        }
        ConcurrentMap<String, Class<?>> concurrentMap = mappings;
        Class<?> cls = concurrentMap.get(str);
        if (cls != null) {
            return cls;
        }
        if (str.charAt(0) == '[') {
            return Array.newInstance(loadClass(str.substring(1), classLoader), 0).getClass();
        }
        if (str.startsWith("L") && str.endsWith(";")) {
            return loadClass(str.substring(1, str.length() - 1), classLoader);
        }
        if (classLoader != null) {
            try {
                cls = classLoader.loadClass(str);
                if (z) {
                    concurrentMap.put(str, cls);
                }
                return cls;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null && contextClassLoader != classLoader) {
                Class<?> loadClass = contextClassLoader.loadClass(str);
                if (z) {
                    try {
                        mappings.put(str, loadClass);
                    } catch (Exception e2) {
                        e = e2;
                        cls = loadClass;
                        e.printStackTrace();
                        cls = Class.forName(str);
                        mappings.put(str, cls);
                        return cls;
                    }
                }
                return loadClass;
            }
        } catch (Exception e3) {
            e = e3;
        }
        try {
            cls = Class.forName(str);
            mappings.put(str, cls);
            return cls;
        } catch (Exception e4) {
            e4.printStackTrace();
            return cls;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:146:0x02df, code lost:
        if (r0 == null) goto L132;
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x0407, code lost:
        if (r0 == null) goto L174;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:282:0x059f  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x05b1  */
    /* JADX WARN: Type inference failed for: r1v53, types: [java.lang.annotation.Annotation[][]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static List<FieldInfo> computeGetters(Class<?> cls, int i, boolean z, JSONType jSONType, Map<String, String> map, boolean z2, boolean z3, boolean z4, PropertyNamingStrategy propertyNamingStrategy) {
        String[] strArr;
        boolean z5;
        boolean z6;
        int i2;
        int i3;
        Iterator it;
        LinkedHashMap linkedHashMap;
        Field[] declaredFields;
        String[] strArr2;
        JSONField[][] jSONFieldArr;
        JSONField jSONField;
        Constructor<?>[] constructorArr;
        short[] sArr;
        Field[] fieldArr;
        HashMap hashMap;
        LinkedHashMap linkedHashMap2;
        int i4;
        int i5;
        String str;
        Method method;
        LinkedHashMap linkedHashMap3;
        Field[] fieldArr2;
        int i6;
        int i7;
        Field[] fieldArr3;
        String substring;
        HashMap hashMap2;
        PropertyNamingStrategy propertyNamingStrategy2;
        int i8;
        JSONField jSONField2;
        int i9;
        String decapitalize;
        PropertyNamingStrategy propertyNamingStrategy3;
        Field[] fieldArr4;
        int i10;
        JSONField jSONField3;
        JSONField jSONField4;
        JSONField[] jSONFieldArr2;
        Method[] declaredMethods;
        Class<?> cls2 = cls;
        int i11 = i;
        JSONType jSONType2 = jSONType;
        Map<String, String> map2 = map;
        LinkedHashMap linkedHashMap4 = new LinkedHashMap();
        HashMap hashMap3 = new HashMap();
        Field[] declaredFields2 = cls.getDeclaredFields();
        if (!z) {
            boolean isKotlin = isKotlin(cls);
            ArrayList<Method> arrayList = new ArrayList();
            for (Class<?> cls3 = cls2; cls3 != null && cls3 != Object.class; cls3 = cls3.getSuperclass()) {
                for (Method method2 : cls3.getDeclaredMethods()) {
                    int modifiers = method2.getModifiers();
                    if ((modifiers & 8) == 0 && (modifiers & 2) == 0 && (modifiers & 256) == 0 && (modifiers & 4) == 0 && !method2.getReturnType().equals(Void.TYPE) && method2.getParameterTypes().length == 0 && method2.getReturnType() != ClassLoader.class && method2.getDeclaringClass() != Object.class) {
                        arrayList.add(method2);
                    }
                }
            }
            Constructor<?>[] constructorArr2 = null;
            String[] strArr3 = null;
            short[] sArr2 = null;
            JSONField[][] jSONFieldArr3 = null;
            for (Method method3 : arrayList) {
                String name = method3.getName();
                if (!name.equals("getMetaClass") || !method3.getReturnType().getName().equals("groovy.lang.MetaClass")) {
                    JSONField jSONField5 = z3 ? (JSONField) method3.getAnnotation(JSONField.class) : null;
                    if (jSONField5 == null && z3) {
                        jSONField5 = getSupperMethodAnnotation(cls2, method3);
                    }
                    if (!isKotlin || !isKotlinIgnore(cls2, name)) {
                        LinkedHashMap linkedHashMap5 = linkedHashMap4;
                        if (jSONField5 == null && isKotlin) {
                            if (constructorArr2 == null) {
                                constructorArr2 = cls.getDeclaredConstructors();
                                JSONField[][] jSONFieldArr4 = jSONFieldArr3;
                                if (constructorArr2.length == 1) {
                                    ?? parameterAnnotations = constructorArr2[0].getParameterAnnotations();
                                    String[] koltinConstructorParameters = getKoltinConstructorParameters(cls);
                                    if (koltinConstructorParameters != null) {
                                        String[] strArr4 = new String[koltinConstructorParameters.length];
                                        System.arraycopy(koltinConstructorParameters, 0, strArr4, 0, koltinConstructorParameters.length);
                                        Arrays.sort(strArr4);
                                        short[] sArr3 = new short[koltinConstructorParameters.length];
                                        for (short s = 0; s < koltinConstructorParameters.length; s = (short) (s + 1)) {
                                            sArr3[Arrays.binarySearch(strArr4, koltinConstructorParameters[s])] = s;
                                        }
                                        strArr3 = strArr4;
                                        jSONFieldArr3 = parameterAnnotations;
                                        sArr2 = sArr3;
                                        constructorArr2 = constructorArr2;
                                    } else {
                                        jSONFieldArr4 = parameterAnnotations;
                                        strArr3 = koltinConstructorParameters;
                                    }
                                }
                                jSONFieldArr3 = jSONFieldArr4;
                            }
                            if (strArr3 == null || sArr2 == null || !name.startsWith("get")) {
                                constructorArr = constructorArr2;
                                strArr2 = strArr3;
                                jSONField4 = jSONField5;
                            } else {
                                String decapitalize2 = decapitalize(name.substring(3));
                                int binarySearch = Arrays.binarySearch(strArr3, decapitalize2);
                                constructorArr = constructorArr2;
                                jSONField4 = jSONField5;
                                if (binarySearch < 0) {
                                    int i12 = 0;
                                    while (true) {
                                        if (i12 >= strArr3.length) {
                                            break;
                                        } else if (decapitalize2.equalsIgnoreCase(strArr3[i12])) {
                                            binarySearch = i12;
                                            break;
                                        } else {
                                            i12++;
                                        }
                                    }
                                }
                                if (binarySearch >= 0 && (jSONFieldArr2 = jSONFieldArr3[sArr2[binarySearch]]) != null) {
                                    int length = jSONFieldArr2.length;
                                    int i13 = 0;
                                    while (i13 < length) {
                                        strArr2 = strArr3;
                                        JSONField jSONField6 = jSONFieldArr2[i13];
                                        JSONField[] jSONFieldArr5 = jSONFieldArr2;
                                        if (jSONField6 instanceof JSONField) {
                                            jSONField = jSONField6;
                                            sArr = sArr2;
                                            jSONFieldArr = jSONFieldArr3;
                                            break;
                                        }
                                        i13++;
                                        strArr3 = strArr2;
                                        jSONFieldArr2 = jSONFieldArr5;
                                    }
                                }
                                strArr2 = strArr3;
                            }
                            jSONFieldArr = jSONFieldArr3;
                            jSONField = jSONField4;
                        } else {
                            strArr2 = strArr3;
                            jSONFieldArr = jSONFieldArr3;
                            jSONField = jSONField5;
                            constructorArr = constructorArr2;
                        }
                        sArr = sArr2;
                        if (jSONField != null) {
                            if (jSONField.serialize()) {
                                i4 = jSONField.ordinal();
                                i5 = SerializerFeature.of(jSONField.serialzeFeatures());
                                if (jSONField.name().length() != 0) {
                                    String name2 = jSONField.name();
                                    if (map2 == null || (name2 = map2.get(name2)) != null) {
                                        String str2 = name2;
                                        setAccessible(cls2, method3, i11);
                                        hashMap = hashMap3;
                                        fieldArr = declaredFields2;
                                        linkedHashMap2 = linkedHashMap5;
                                        linkedHashMap2.put(str2, new FieldInfo(str2, method3, null, cls, null, i4, i5, jSONField, null, true));
                                        i7 = i;
                                        linkedHashMap4 = linkedHashMap2;
                                        fieldArr3 = fieldArr;
                                        cls2 = cls;
                                        declaredFields2 = fieldArr3;
                                        i11 = i7;
                                        strArr3 = strArr2;
                                        constructorArr2 = constructorArr;
                                        sArr2 = sArr;
                                        jSONFieldArr3 = jSONFieldArr;
                                        hashMap3 = hashMap;
                                        jSONType2 = jSONType;
                                    }
                                } else {
                                    fieldArr = declaredFields2;
                                    hashMap = hashMap3;
                                    linkedHashMap2 = linkedHashMap5;
                                }
                            }
                            hashMap = hashMap3;
                            i7 = i11;
                            linkedHashMap4 = linkedHashMap5;
                            fieldArr3 = declaredFields2;
                            cls2 = cls;
                            declaredFields2 = fieldArr3;
                            i11 = i7;
                            strArr3 = strArr2;
                            constructorArr2 = constructorArr;
                            sArr2 = sArr;
                            jSONFieldArr3 = jSONFieldArr;
                            hashMap3 = hashMap;
                            jSONType2 = jSONType;
                        } else {
                            fieldArr = declaredFields2;
                            hashMap = hashMap3;
                            linkedHashMap2 = linkedHashMap5;
                            i4 = 0;
                            i5 = 0;
                        }
                        if (name.startsWith("get")) {
                            if (name.length() >= 4) {
                                if (!name.equals("getClass")) {
                                    char charAt = name.charAt(3);
                                    if (Character.isUpperCase(charAt)) {
                                        decapitalize = compatibleWithJavaBean ? decapitalize(name.substring(3)) : Character.toLowerCase(name.charAt(3)) + name.substring(4);
                                    } else if (charAt == '_') {
                                        decapitalize = name.substring(4);
                                    } else if (charAt == 'f') {
                                        decapitalize = name.substring(3);
                                    } else if (name.length() >= 5 && Character.isUpperCase(name.charAt(4))) {
                                        decapitalize = decapitalize(name.substring(3));
                                    }
                                    if (!isJSONTypeIgnore(cls2, jSONType2, decapitalize)) {
                                        HashMap hashMap4 = hashMap;
                                        Field field = getField(cls2, decapitalize, fieldArr, hashMap4);
                                        if (field != null) {
                                            JSONField jSONField7 = z3 ? (JSONField) field.getAnnotation(JSONField.class) : null;
                                            if (jSONField7 != null) {
                                                if (jSONField7.serialize()) {
                                                    i4 = jSONField7.ordinal();
                                                    i5 = SerializerFeature.of(jSONField7.serialzeFeatures());
                                                    if (jSONField7.name().length() != 0) {
                                                        decapitalize = jSONField7.name();
                                                        if (map2 != null) {
                                                            decapitalize = map2.get(decapitalize);
                                                        }
                                                    }
                                                }
                                                i7 = i;
                                                hashMap = hashMap4;
                                                linkedHashMap4 = linkedHashMap2;
                                                fieldArr3 = fieldArr;
                                                cls2 = cls;
                                                declaredFields2 = fieldArr3;
                                                i11 = i7;
                                                strArr3 = strArr2;
                                                constructorArr2 = constructorArr;
                                                sArr2 = sArr;
                                                jSONFieldArr3 = jSONFieldArr;
                                                hashMap3 = hashMap;
                                                jSONType2 = jSONType;
                                            }
                                            propertyNamingStrategy3 = propertyNamingStrategy;
                                            int i14 = i4;
                                            jSONField3 = jSONField7;
                                            fieldArr4 = fieldArr;
                                            i10 = i14;
                                        } else {
                                            propertyNamingStrategy3 = propertyNamingStrategy;
                                            fieldArr4 = fieldArr;
                                            i10 = i4;
                                            jSONField3 = null;
                                        }
                                        if (propertyNamingStrategy3 != null) {
                                            decapitalize = propertyNamingStrategy3.translate(decapitalize);
                                        }
                                        if (map2 == null || (decapitalize = map2.get(decapitalize)) != null) {
                                            LinkedHashMap linkedHashMap6 = linkedHashMap2;
                                            String str3 = decapitalize;
                                            setAccessible(cls2, method3, i);
                                            fieldArr2 = fieldArr4;
                                            hashMap = hashMap4;
                                            str = name;
                                            method = method3;
                                            int i15 = i10;
                                            i6 = 3;
                                            linkedHashMap3 = linkedHashMap6;
                                            linkedHashMap3.put(str3, new FieldInfo(str3, method3, field, cls, null, i10, i5, jSONField, jSONField3, z4));
                                            i4 = i15;
                                        } else {
                                            i7 = i;
                                            hashMap = hashMap4;
                                            linkedHashMap4 = linkedHashMap2;
                                            fieldArr3 = fieldArr4;
                                            cls2 = cls;
                                            declaredFields2 = fieldArr3;
                                            i11 = i7;
                                            strArr3 = strArr2;
                                            constructorArr2 = constructorArr;
                                            sArr2 = sArr;
                                            jSONFieldArr3 = jSONFieldArr;
                                            hashMap3 = hashMap;
                                            jSONType2 = jSONType;
                                        }
                                    }
                                }
                                i7 = i;
                                linkedHashMap4 = linkedHashMap2;
                                fieldArr3 = fieldArr;
                                cls2 = cls;
                                declaredFields2 = fieldArr3;
                                i11 = i7;
                                strArr3 = strArr2;
                                constructorArr2 = constructorArr;
                                sArr2 = sArr;
                                jSONFieldArr3 = jSONFieldArr;
                                hashMap3 = hashMap;
                                jSONType2 = jSONType;
                            }
                            i7 = i;
                            map2 = map;
                            linkedHashMap4 = linkedHashMap2;
                            fieldArr3 = fieldArr;
                            cls2 = cls;
                            declaredFields2 = fieldArr3;
                            i11 = i7;
                            strArr3 = strArr2;
                            constructorArr2 = constructorArr;
                            sArr2 = sArr;
                            jSONFieldArr3 = jSONFieldArr;
                            hashMap3 = hashMap;
                            jSONType2 = jSONType;
                        } else {
                            str = name;
                            method = method3;
                            linkedHashMap3 = linkedHashMap2;
                            fieldArr2 = fieldArr;
                            i6 = 3;
                        }
                        String str4 = str;
                        if (str4.startsWith("is") && str4.length() >= i6) {
                            char charAt2 = str4.charAt(2);
                            if (Character.isUpperCase(charAt2)) {
                                substring = compatibleWithJavaBean ? decapitalize(str4.substring(2)) : Character.toLowerCase(str4.charAt(2)) + str4.substring(i6);
                            } else if (charAt2 == '_') {
                                substring = str4.substring(i6);
                            } else if (charAt2 == 'f') {
                                substring = str4.substring(2);
                            }
                            if (!isJSONTypeIgnore(cls2, jSONType2, substring)) {
                                HashMap hashMap5 = hashMap;
                                fieldArr3 = fieldArr2;
                                Field field2 = getField(cls2, substring, fieldArr3, hashMap5);
                                if (field2 == null) {
                                    field2 = getField(cls2, str4, fieldArr3, hashMap5);
                                }
                                if (field2 != null) {
                                    JSONField jSONField8 = z3 ? (JSONField) field2.getAnnotation(JSONField.class) : null;
                                    if (jSONField8 == null) {
                                        map2 = map;
                                        hashMap2 = hashMap5;
                                        propertyNamingStrategy2 = propertyNamingStrategy;
                                        i8 = i4;
                                        i9 = i5;
                                        jSONField2 = jSONField8;
                                    } else if (jSONField8.serialize()) {
                                        int ordinal = jSONField8.ordinal();
                                        int of = SerializerFeature.of(jSONField8.serialzeFeatures());
                                        if (jSONField8.name().length() != 0) {
                                            substring = jSONField8.name();
                                            map2 = map;
                                            hashMap2 = hashMap5;
                                            if (map2 != null) {
                                                substring = map2.get(substring);
                                            }
                                        } else {
                                            map2 = map;
                                            hashMap2 = hashMap5;
                                        }
                                        propertyNamingStrategy2 = propertyNamingStrategy;
                                        jSONField2 = jSONField8;
                                        i8 = ordinal;
                                        i9 = of;
                                    } else {
                                        i7 = i;
                                        map2 = map;
                                        hashMap = hashMap5;
                                        linkedHashMap4 = linkedHashMap3;
                                        cls2 = cls;
                                        declaredFields2 = fieldArr3;
                                        i11 = i7;
                                        strArr3 = strArr2;
                                        constructorArr2 = constructorArr;
                                        sArr2 = sArr;
                                        jSONFieldArr3 = jSONFieldArr;
                                        hashMap3 = hashMap;
                                        jSONType2 = jSONType;
                                    }
                                } else {
                                    map2 = map;
                                    hashMap2 = hashMap5;
                                    propertyNamingStrategy2 = propertyNamingStrategy;
                                    i8 = i4;
                                    jSONField2 = null;
                                    i9 = i5;
                                }
                                if (propertyNamingStrategy2 != null) {
                                    substring = propertyNamingStrategy2.translate(substring);
                                }
                                if (map2 == null || (substring = map2.get(substring)) != null) {
                                    String str5 = substring;
                                    setAccessible(cls2, field2, i);
                                    Method method4 = method;
                                    setAccessible(cls2, method4, i);
                                    hashMap = hashMap2;
                                    i7 = i;
                                    linkedHashMap4 = linkedHashMap3;
                                    linkedHashMap4.put(str5, new FieldInfo(str5, method4, field2, cls, null, i8, i9, jSONField, jSONField2, z4));
                                    cls2 = cls;
                                    declaredFields2 = fieldArr3;
                                    i11 = i7;
                                    strArr3 = strArr2;
                                    constructorArr2 = constructorArr;
                                    sArr2 = sArr;
                                    jSONFieldArr3 = jSONFieldArr;
                                    hashMap3 = hashMap;
                                    jSONType2 = jSONType;
                                }
                                i7 = i;
                                linkedHashMap4 = linkedHashMap3;
                                hashMap = hashMap2;
                                cls2 = cls;
                                declaredFields2 = fieldArr3;
                                i11 = i7;
                                strArr3 = strArr2;
                                constructorArr2 = constructorArr;
                                sArr2 = sArr;
                                jSONFieldArr3 = jSONFieldArr;
                                hashMap3 = hashMap;
                                jSONType2 = jSONType;
                            }
                        }
                        i7 = i;
                        map2 = map;
                        linkedHashMap4 = linkedHashMap3;
                        fieldArr3 = fieldArr2;
                        cls2 = cls;
                        declaredFields2 = fieldArr3;
                        i11 = i7;
                        strArr3 = strArr2;
                        constructorArr2 = constructorArr;
                        sArr2 = sArr;
                        jSONFieldArr3 = jSONFieldArr;
                        hashMap3 = hashMap;
                        jSONType2 = jSONType;
                    }
                }
            }
        }
        int i16 = i11;
        Field[] fieldArr5 = declaredFields2;
        ArrayList arrayList2 = new ArrayList(fieldArr5.length);
        for (Field field3 : fieldArr5) {
            if ((field3.getModifiers() & 8) == 0 && !field3.getName().equals("this$0") && (field3.getModifiers() & 1) != 0) {
                arrayList2.add(field3);
            }
        }
        for (Class<? super Object> superclass = cls.getSuperclass(); superclass != null && superclass != Object.class; superclass = superclass.getSuperclass()) {
            for (Field field4 : superclass.getDeclaredFields()) {
                if ((field4.getModifiers() & 8) == 0 && (field4.getModifiers() & 1) != 0) {
                    arrayList2.add(field4);
                }
            }
        }
        Iterator it2 = arrayList2.iterator();
        while (it2.hasNext()) {
            Field field5 = (Field) it2.next();
            JSONField jSONField9 = z3 ? (JSONField) field5.getAnnotation(JSONField.class) : null;
            String name3 = field5.getName();
            if (jSONField9 == null) {
                i2 = 0;
                i3 = 0;
            } else if (jSONField9.serialize()) {
                int ordinal2 = jSONField9.ordinal();
                int of2 = SerializerFeature.of(jSONField9.serialzeFeatures());
                if (jSONField9.name().length() != 0) {
                    name3 = jSONField9.name();
                }
                i2 = ordinal2;
                i3 = of2;
            }
            if (map2 == null || (name3 = map2.get(name3)) != null) {
                if (propertyNamingStrategy != null) {
                    name3 = propertyNamingStrategy.translate(name3);
                }
                String str6 = name3;
                if (linkedHashMap4.containsKey(str6)) {
                    it = it2;
                    linkedHashMap = linkedHashMap4;
                } else {
                    setAccessible(cls, field5, i16);
                    it = it2;
                    linkedHashMap = linkedHashMap4;
                    linkedHashMap.put(str6, new FieldInfo(str6, null, field5, cls, null, i2, i3, null, jSONField9, z4));
                }
                linkedHashMap4 = linkedHashMap;
                it2 = it;
            }
        }
        LinkedHashMap linkedHashMap7 = linkedHashMap4;
        ArrayList arrayList3 = new ArrayList();
        if (jSONType != null) {
            strArr = jSONType.orders();
            if (strArr != null && strArr.length == linkedHashMap7.size()) {
                int length2 = strArr.length;
                int i17 = 0;
                while (true) {
                    if (i17 >= length2) {
                        z6 = true;
                        break;
                    } else if (!linkedHashMap7.containsKey(strArr[i17])) {
                        z6 = false;
                        break;
                    } else {
                        i17++;
                    }
                }
                z5 = z6;
                if (z5) {
                    for (FieldInfo fieldInfo : linkedHashMap7.values()) {
                        arrayList3.add(fieldInfo);
                    }
                    if (z2) {
                        Collections.sort(arrayList3);
                    }
                } else {
                    for (String str7 : strArr) {
                        arrayList3.add((FieldInfo) linkedHashMap7.get(str7));
                    }
                }
                return arrayList3;
            }
        } else {
            strArr = null;
        }
        z5 = false;
        if (z5) {
        }
        return arrayList3;
    }

    public static JSONField getSupperMethodAnnotation(Class<?> cls, Method method) {
        Method[] methods;
        boolean z;
        JSONField jSONField;
        Method[] methods2;
        boolean z2;
        JSONField jSONField2;
        for (Class<?> cls2 : cls.getInterfaces()) {
            for (Method method2 : cls2.getMethods()) {
                if (method2.getName().equals(method.getName())) {
                    Class<?>[] parameterTypes = method2.getParameterTypes();
                    Class<?>[] parameterTypes2 = method.getParameterTypes();
                    if (parameterTypes.length != parameterTypes2.length) {
                        continue;
                    } else {
                        int i = 0;
                        while (true) {
                            if (i >= parameterTypes.length) {
                                z2 = true;
                                break;
                            } else if (!parameterTypes[i].equals(parameterTypes2[i])) {
                                z2 = false;
                                break;
                            } else {
                                i++;
                            }
                        }
                        if (z2 && (jSONField2 = (JSONField) method2.getAnnotation(JSONField.class)) != null) {
                            return jSONField2;
                        }
                    }
                }
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass != null && Modifier.isAbstract(superclass.getModifiers())) {
            Class<?>[] parameterTypes3 = method.getParameterTypes();
            for (Method method3 : superclass.getMethods()) {
                Class<?>[] parameterTypes4 = method3.getParameterTypes();
                if (parameterTypes4.length == parameterTypes3.length && method3.getName().equals(method.getName())) {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= parameterTypes3.length) {
                            z = true;
                            break;
                        } else if (!parameterTypes4[i2].equals(parameterTypes3[i2])) {
                            z = false;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (z && (jSONField = (JSONField) method3.getAnnotation(JSONField.class)) != null) {
                        return jSONField;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isJSONTypeIgnore(Class<?> cls, JSONType jSONType, String str) {
        if (jSONType != null && jSONType.ignores() != null) {
            for (String str2 : jSONType.ignores()) {
                if (str.equalsIgnoreCase(str2)) {
                    return true;
                }
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        return (superclass == Object.class || superclass == null || !isJSONTypeIgnore(superclass, (JSONType) superclass.getAnnotation(JSONType.class), str)) ? false : true;
    }

    public static boolean isGenericParamType(Type type) {
        Type genericSuperclass;
        if (type instanceof ParameterizedType) {
            return true;
        }
        return (type instanceof Class) && (genericSuperclass = ((Class) type).getGenericSuperclass()) != Object.class && isGenericParamType(genericSuperclass);
    }

    public static Type getGenericParamType(Type type) {
        return type instanceof Class ? getGenericParamType(((Class) type).getGenericSuperclass()) : type;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }
        if (type instanceof TypeVariable) {
            return (Class) ((TypeVariable) type).getBounds()[0];
        }
        if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length == 1) {
                return getClass(upperBounds[0]);
            }
            return Object.class;
        }
        return Object.class;
    }

    public static String decapitalize(String str) {
        if (str == null || str.length() == 0 || (str.length() > 1 && Character.isUpperCase(str.charAt(1)) && Character.isUpperCase(str.charAt(0)))) {
            return str;
        }
        char[] charArray = str.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        return new String(charArray);
    }

    public static boolean setAccessible(Class<?> cls, Member member, int i) {
        if (member != null && setAccessibleEnable) {
            Class<? super Object> superclass = cls.getSuperclass();
            if ((superclass == null || superclass == Object.class) && (member.getModifiers() & 1) != 0 && (i & 1) != 0) {
                return false;
            }
            try {
                ((AccessibleObject) member).setAccessible(true);
                return true;
            } catch (AccessControlException unused) {
                setAccessibleEnable = false;
            }
        }
        return false;
    }

    public static Field getField(Class<?> cls, String str, Field[] fieldArr) {
        return getField(cls, str, fieldArr, null);
    }

    public static Field getField(Class<?> cls, String str, Field[] fieldArr, Map<Class<?>, Field[]> map) {
        Field field0 = getField0(cls, str, fieldArr, map);
        if (field0 == null) {
            field0 = getField0(cls, "_" + str, fieldArr, map);
        }
        if (field0 == null) {
            field0 = getField0(cls, "m_" + str, fieldArr, map);
        }
        if (field0 == null) {
            return getField0(cls, "m" + str.substring(0, 1).toUpperCase() + str.substring(1), fieldArr, map);
        }
        return field0;
    }

    private static Field getField0(Class<?> cls, String str, Field[] fieldArr, Map<Class<?>, Field[]> map) {
        char charAt;
        char charAt2;
        for (Field field : fieldArr) {
            String name = field.getName();
            if (str.equals(name)) {
                return field;
            }
            if (str.length() > 2 && (charAt = str.charAt(0)) >= 'a' && charAt <= 'z' && (charAt2 = str.charAt(1)) >= 'A' && charAt2 <= 'Z' && str.equalsIgnoreCase(name)) {
                return field;
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        Field[] fieldArr2 = map != null ? map.get(superclass) : null;
        if (fieldArr2 == null) {
            fieldArr2 = superclass.getDeclaredFields();
            if (map != null) {
                map.put(superclass, fieldArr2);
            }
        }
        return getField(superclass, str, fieldArr2, map);
    }

    public static Type getCollectionItemType(Type type) {
        Type type2;
        if (type instanceof ParameterizedType) {
            type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (type2 instanceof WildcardType) {
                Type[] upperBounds = ((WildcardType) type2).getUpperBounds();
                if (upperBounds.length == 1) {
                    type2 = upperBounds[0];
                }
            }
        } else {
            if (type instanceof Class) {
                Class cls = (Class) type;
                if (!cls.getName().startsWith("java.")) {
                    type2 = getCollectionItemType(cls.getGenericSuperclass());
                }
            }
            type2 = null;
        }
        return type2 == null ? Object.class : type2;
    }

    public static Object defaultValue(Class<?> cls) {
        if (cls == Byte.TYPE) {
            return (byte) 0;
        }
        if (cls == Short.TYPE) {
            return (short) 0;
        }
        if (cls == Integer.TYPE) {
            return 0;
        }
        if (cls == Long.TYPE) {
            return 0L;
        }
        if (cls == Float.TYPE) {
            return Float.valueOf(0.0f);
        }
        if (cls == Double.TYPE) {
            return Double.valueOf(0.0d);
        }
        if (cls == Boolean.TYPE) {
            return Boolean.FALSE;
        }
        return cls == Character.TYPE ? '0' : null;
    }

    public static boolean getArgument(Type[] typeArr, TypeVariable[] typeVariableArr, Type[] typeArr2) {
        if (typeArr2 == null || typeVariableArr.length == 0) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < typeArr.length; i++) {
            Type type = typeArr[i];
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (getArgument(actualTypeArguments, typeVariableArr, typeArr2)) {
                    typeArr[i] = new ParameterizedTypeImpl(actualTypeArguments, parameterizedType.getOwnerType(), parameterizedType.getRawType());
                    z = true;
                }
            } else if (type instanceof TypeVariable) {
                for (int i2 = 0; i2 < typeVariableArr.length; i2++) {
                    if (type.equals(typeVariableArr[i2])) {
                        typeArr[i] = typeArr2[i2];
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    public static long fnv_64_lower(String str) {
        long j = -3750763034362895579L;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt != '_' && charAt != '-') {
                if (charAt >= 'A' && charAt <= 'Z') {
                    charAt = (char) (charAt + TokenParser.SP);
                }
                j = (j ^ charAt) * 1099511628211L;
            }
        }
        return j;
    }
}
