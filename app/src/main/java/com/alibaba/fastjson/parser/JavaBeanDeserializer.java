package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.http.message.TokenParser;

/* loaded from: classes.dex */
public class JavaBeanDeserializer implements ObjectDeserializer {
    private final Map<String, FieldDeserializer> alterNameFieldDeserializers;
    public final JavaBeanInfo beanInfo;
    protected final Class<?> clazz;
    private ConcurrentMap<String, Object> extraFieldDeserializers;
    private final FieldDeserializer[] fieldDeserializers;
    private transient long[] smartMatchHashArray;
    private transient int[] smartMatchHashArrayMapping;
    private final FieldDeserializer[] sortedFieldDeserializers;

    public JavaBeanDeserializer(ParserConfig parserConfig, Class<?> cls, Type type) {
        this(parserConfig, cls, type, JavaBeanInfo.build(cls, cls.getModifiers(), type, false, true, true, true, parserConfig.propertyNamingStrategy));
    }

    public JavaBeanDeserializer(ParserConfig parserConfig, Class<?> cls, Type type, JavaBeanInfo javaBeanInfo) {
        String[] strArr;
        this.clazz = cls;
        this.beanInfo = javaBeanInfo;
        this.sortedFieldDeserializers = new FieldDeserializer[javaBeanInfo.sortedFields.length];
        int length = javaBeanInfo.sortedFields.length;
        HashMap hashMap = null;
        for (int i = 0; i < length; i++) {
            FieldInfo fieldInfo = javaBeanInfo.sortedFields[i];
            FieldDeserializer createFieldDeserializer = parserConfig.createFieldDeserializer(parserConfig, cls, fieldInfo);
            this.sortedFieldDeserializers[i] = createFieldDeserializer;
            for (String str : fieldInfo.alternateNames) {
                if (hashMap == null) {
                    hashMap = new HashMap();
                }
                hashMap.put(str, createFieldDeserializer);
            }
        }
        this.alterNameFieldDeserializers = hashMap;
        this.fieldDeserializers = new FieldDeserializer[javaBeanInfo.fields.length];
        int length2 = javaBeanInfo.fields.length;
        for (int i2 = 0; i2 < length2; i2++) {
            this.fieldDeserializers[i2] = getFieldDeserializer(javaBeanInfo.fields[i2].name);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object createInstance(DefaultJSONParser defaultJSONParser, Type type) {
        Object newInstance;
        FieldInfo[] fieldInfoArr;
        if ((type instanceof Class) && this.clazz.isInterface()) {
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{(Class) type}, new JSONObject((defaultJSONParser.lexer.features & Feature.OrderedField.mask) != 0));
        } else if (this.beanInfo.defaultConstructor == null && this.beanInfo.factoryMethod == null) {
            return null;
        } else {
            if (this.beanInfo.factoryMethod == null || this.beanInfo.defaultConstructorParameterSize <= 0) {
                try {
                    Constructor<?> constructor = this.beanInfo.defaultConstructor;
                    if (this.beanInfo.defaultConstructorParameterSize != 0) {
                        newInstance = constructor.newInstance(defaultJSONParser.contex.object);
                    } else if (constructor != null) {
                        newInstance = constructor.newInstance(new Object[0]);
                    } else {
                        newInstance = this.beanInfo.factoryMethod.invoke(null, new Object[0]);
                    }
                    if (defaultJSONParser != null && (defaultJSONParser.lexer.features & Feature.InitStringFieldAsEmpty.mask) != 0) {
                        for (FieldInfo fieldInfo : this.beanInfo.fields) {
                            if (fieldInfo.fieldClass == String.class) {
                                fieldInfo.set(newInstance, "");
                            }
                        }
                    }
                    return newInstance;
                } catch (Exception e) {
                    throw new JSONException("create instance error, class " + this.clazz.getName(), e);
                }
            }
            return null;
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return (T) deserialze(defaultJSONParser, type, obj, null);
    }

    private <T> T deserialzeArrayMapping(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2) {
        Enum r8;
        String str;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        T t = (T) createInstance(defaultJSONParser, type);
        int length = this.sortedFieldDeserializers.length;
        int i = 0;
        while (i < length) {
            char c = i == length + (-1) ? ']' : ',';
            FieldDeserializer fieldDeserializer = this.sortedFieldDeserializers[i];
            FieldInfo fieldInfo = fieldDeserializer.fieldInfo;
            Class<?> cls = fieldInfo.fieldClass;
            try {
                if (cls == Integer.TYPE) {
                    int scanLongValue = (int) jSONLexer.scanLongValue();
                    if (fieldInfo.fieldAccess) {
                        fieldInfo.field.setInt(t, scanLongValue);
                    } else {
                        fieldDeserializer.setValue(t, new Integer(scanLongValue));
                    }
                    if (jSONLexer.ch == ',') {
                        int i2 = jSONLexer.bp + 1;
                        jSONLexer.bp = i2;
                        jSONLexer.ch = i2 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i2);
                        jSONLexer.token = 16;
                    } else if (jSONLexer.ch == ']') {
                        int i3 = jSONLexer.bp + 1;
                        jSONLexer.bp = i3;
                        jSONLexer.ch = i3 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i3);
                        jSONLexer.token = 15;
                    } else {
                        jSONLexer.nextToken();
                    }
                } else if (cls == String.class) {
                    if (jSONLexer.ch == '\"') {
                        str = jSONLexer.scanStringValue(TokenParser.DQUOTE);
                    } else if (jSONLexer.ch == 'n' && jSONLexer.text.startsWith("null", jSONLexer.bp)) {
                        jSONLexer.bp += 4;
                        jSONLexer.ch = jSONLexer.bp >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(jSONLexer.bp);
                        str = null;
                    } else {
                        throw new JSONException("not match string. feild : " + obj);
                    }
                    if (fieldInfo.fieldAccess) {
                        fieldInfo.field.set(t, str);
                    } else {
                        fieldDeserializer.setValue(t, str);
                    }
                    if (jSONLexer.ch == ',') {
                        int i4 = jSONLexer.bp + 1;
                        jSONLexer.bp = i4;
                        jSONLexer.ch = i4 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i4);
                        jSONLexer.token = 16;
                    } else if (jSONLexer.ch == ']') {
                        int i5 = jSONLexer.bp + 1;
                        jSONLexer.bp = i5;
                        jSONLexer.ch = i5 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i5);
                        jSONLexer.token = 15;
                    } else {
                        jSONLexer.nextToken();
                    }
                } else {
                    if (cls == Long.TYPE) {
                        long scanLongValue2 = jSONLexer.scanLongValue();
                        if (fieldInfo.fieldAccess) {
                            fieldInfo.field.setLong(t, scanLongValue2);
                        } else {
                            fieldDeserializer.setValue(t, new Long(scanLongValue2));
                        }
                        if (jSONLexer.ch == ',') {
                            int i6 = jSONLexer.bp + 1;
                            jSONLexer.bp = i6;
                            jSONLexer.ch = i6 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i6);
                            jSONLexer.token = 16;
                        } else if (jSONLexer.ch == ']') {
                            int i7 = jSONLexer.bp + 1;
                            jSONLexer.bp = i7;
                            jSONLexer.ch = i7 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i7);
                            jSONLexer.token = 15;
                        } else {
                            jSONLexer.nextToken();
                        }
                    } else if (cls == Boolean.TYPE) {
                        boolean scanBoolean = jSONLexer.scanBoolean();
                        if (fieldInfo.fieldAccess) {
                            fieldInfo.field.setBoolean(t, scanBoolean);
                        } else {
                            fieldDeserializer.setValue(t, Boolean.valueOf(scanBoolean));
                        }
                        if (jSONLexer.ch == ',') {
                            int i8 = jSONLexer.bp + 1;
                            jSONLexer.bp = i8;
                            jSONLexer.ch = i8 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i8);
                            jSONLexer.token = 16;
                        } else if (jSONLexer.ch == ']') {
                            int i9 = jSONLexer.bp + 1;
                            jSONLexer.bp = i9;
                            jSONLexer.ch = i9 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i9);
                            jSONLexer.token = 15;
                        } else {
                            jSONLexer.nextToken();
                        }
                    } else if (cls.isEnum()) {
                        char c2 = jSONLexer.ch;
                        if (c2 == '\"') {
                            String scanSymbol = jSONLexer.scanSymbol(defaultJSONParser.symbolTable);
                            r8 = scanSymbol == null ? null : Enum.valueOf(cls, scanSymbol);
                        } else if (c2 >= '0' && c2 <= '9') {
                            r8 = ((EnumDeserializer) ((DefaultFieldDeserializer) fieldDeserializer).getFieldValueDeserilizer(defaultJSONParser.config)).ordinalEnums[(int) jSONLexer.scanLongValue()];
                        } else {
                            throw new JSONException("illegal enum." + jSONLexer.info());
                        }
                        fieldDeserializer.setValue(t, r8);
                        if (jSONLexer.ch == ',') {
                            int i10 = jSONLexer.bp + 1;
                            jSONLexer.bp = i10;
                            jSONLexer.ch = i10 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i10);
                            jSONLexer.token = 16;
                        } else if (jSONLexer.ch == ']') {
                            int i11 = jSONLexer.bp + 1;
                            jSONLexer.bp = i11;
                            jSONLexer.ch = i11 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i11);
                            jSONLexer.token = 15;
                        } else {
                            jSONLexer.nextToken();
                        }
                    } else if (cls == Date.class && jSONLexer.ch == '1') {
                        fieldDeserializer.setValue(t, new Date(jSONLexer.scanLongValue()));
                        if (jSONLexer.ch == ',') {
                            int i12 = jSONLexer.bp + 1;
                            jSONLexer.bp = i12;
                            jSONLexer.ch = i12 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i12);
                            jSONLexer.token = 16;
                        } else if (jSONLexer.ch == ']') {
                            int i13 = jSONLexer.bp + 1;
                            jSONLexer.bp = i13;
                            jSONLexer.ch = i13 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i13);
                            jSONLexer.token = 15;
                        } else {
                            jSONLexer.nextToken();
                        }
                    } else {
                        if (jSONLexer.ch == '[') {
                            int i14 = jSONLexer.bp + 1;
                            jSONLexer.bp = i14;
                            jSONLexer.ch = i14 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i14);
                            jSONLexer.token = 14;
                        } else if (jSONLexer.ch == '{') {
                            int i15 = jSONLexer.bp + 1;
                            jSONLexer.bp = i15;
                            jSONLexer.ch = i15 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i15);
                            jSONLexer.token = 12;
                        } else {
                            jSONLexer.nextToken();
                        }
                        fieldDeserializer.parseField(defaultJSONParser, t, fieldInfo.fieldType, null);
                        if (c == ']') {
                            if (jSONLexer.token != 15) {
                                throw new JSONException("syntax error");
                            }
                        } else if (c == ',' && jSONLexer.token != 16) {
                            throw new JSONException("syntax error");
                        }
                    }
                    i++;
                }
                i++;
            } catch (IllegalAccessException e) {
                throw new JSONException("set " + fieldInfo.name + "error", e);
            }
        }
        if (jSONLexer.ch == ',') {
            int i16 = jSONLexer.bp + 1;
            jSONLexer.bp = i16;
            jSONLexer.ch = i16 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i16);
            jSONLexer.token = 16;
        } else {
            jSONLexer.nextToken();
        }
        return t;
    }

    /* JADX WARN: Code restructure failed: missing block: B:236:0x0320, code lost:
        r10.nextTokenWithChar(':');
     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x0326, code lost:
        if (r10.token != 4) goto L136;
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x0328, code lost:
        r0 = r10.stringVal();
     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x0332, code lost:
        if ("@".equals(r0) == false) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x0336, code lost:
        r1 = (T) r14.object;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x033e, code lost:
        if ("..".equals(r0) == false) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x0340, code lost:
        r2 = r14.parent;
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x0344, code lost:
        if (r2.object == null) goto L123;
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x0346, code lost:
        r1 = (T) r2.object;
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x0349, code lost:
        r38.addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r2, r0));
        r38.resolveStatus = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:250:0x035b, code lost:
        if ("$".equals(r0) == false) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:251:0x035d, code lost:
        r2 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x0360, code lost:
        if (r2.parent == null) goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:254:0x0362, code lost:
        r2 = r2.parent;
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x0367, code lost:
        if (r2.object == null) goto L134;
     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x0369, code lost:
        r1 = (T) r2.object;
     */
    /* JADX WARN: Code restructure failed: missing block: B:258:0x036c, code lost:
        r38.addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r2, r0));
        r38.resolveStatus = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x0378, code lost:
        r38.addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r14, r0));
        r38.resolveStatus = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x0383, code lost:
        r10.nextToken(13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x038a, code lost:
        if (r10.token != 13) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x038c, code lost:
        r10.nextToken(16);
        r38.setContext(r14, r1, r40);
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x0394, code lost:
        r2 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x0396, code lost:
        if (r2 == null) goto L112;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x0398, code lost:
        r2.object = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x039a, code lost:
        r38.setContext(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x039d, code lost:
        return (T) r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x03a7, code lost:
        throw new com.alibaba.fastjson.JSONException("illegal ref");
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x03c4, code lost:
        throw new com.alibaba.fastjson.JSONException("illegal ref, " + com.alibaba.fastjson.parser.JSONToken.name(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0415, code lost:
        r5 = getSeeAlso(r38.config, r37.beanInfo, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x041d, code lost:
        if (r5 != null) goto L380;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x041f, code lost:
        r5 = r38.config.checkAutoType(r4, r37.clazz, r10.features);
        r0 = com.alibaba.fastjson.util.TypeUtils.getClass(r39);
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x042d, code lost:
        if (r0 == null) goto L369;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x042f, code lost:
        if (r5 == null) goto L367;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x0435, code lost:
        if (r0.isAssignableFrom(r5) == false) goto L367;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x043f, code lost:
        throw new com.alibaba.fastjson.JSONException("type not match");
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x0440, code lost:
        r5 = r38.config.getDeserializer(r5);
        r0 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x044c, code lost:
        r0 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x044f, code lost:
        if ((r5 instanceof com.alibaba.fastjson.parser.JavaBeanDeserializer) == false) goto L379;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0451, code lost:
        r5 = r5;
        r0 = (T) r5.deserialze(r38, r0, r40, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x0458, code lost:
        if (r3 == null) goto L375;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x045a, code lost:
        r5.getFieldDeserializer(r3).setValue(r0, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x0462, code lost:
        r0 = (T) r5.deserialze(r38, r0, r40);
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0466, code lost:
        if (r2 == null) goto L377;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x0468, code lost:
        r2.object = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x046a, code lost:
        r38.setContext(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x046d, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x05f8, code lost:
        r2 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0711, code lost:
        throw new com.alibaba.fastjson.JSONException("syntax error, unexpect token " + com.alibaba.fastjson.parser.JSONToken.name(r10.token));
     */
    /* JADX WARN: Removed duplicated region for block: B:214:0x02dc  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x02ef  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x03dc A[Catch: all -> 0x04a1, TryCatch #1 {all -> 0x04a1, blocks: (B:439:0x0604, B:461:0x0652, B:462:0x065a, B:464:0x0660, B:466:0x0672, B:270:0x03a0, B:271:0x03a7, B:272:0x03a8, B:273:0x03c4, B:276:0x03ca, B:280:0x03d4, B:282:0x03dc, B:284:0x03e9, B:286:0x03f6, B:288:0x03fc, B:292:0x0415, B:294:0x041f, B:297:0x0431, B:300:0x0438, B:301:0x043f, B:302:0x0440, B:304:0x044d, B:306:0x0451, B:308:0x045a, B:309:0x0462, B:314:0x046e, B:315:0x0475, B:322:0x0489, B:324:0x048f, B:326:0x049b), top: B:506:0x03ca }] */
    /* JADX WARN: Removed duplicated region for block: B:321:0x0487 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:324:0x048f A[Catch: all -> 0x04a1, TryCatch #1 {all -> 0x04a1, blocks: (B:439:0x0604, B:461:0x0652, B:462:0x065a, B:464:0x0660, B:466:0x0672, B:270:0x03a0, B:271:0x03a7, B:272:0x03a8, B:273:0x03c4, B:276:0x03ca, B:280:0x03d4, B:282:0x03dc, B:284:0x03e9, B:286:0x03f6, B:288:0x03fc, B:292:0x0415, B:294:0x041f, B:297:0x0431, B:300:0x0438, B:301:0x043f, B:302:0x0440, B:304:0x044d, B:306:0x0451, B:308:0x045a, B:309:0x0462, B:314:0x046e, B:315:0x0475, B:322:0x0489, B:324:0x048f, B:326:0x049b), top: B:506:0x03ca }] */
    /* JADX WARN: Removed duplicated region for block: B:326:0x049b A[Catch: all -> 0x04a1, TRY_LEAVE, TryCatch #1 {all -> 0x04a1, blocks: (B:439:0x0604, B:461:0x0652, B:462:0x065a, B:464:0x0660, B:466:0x0672, B:270:0x03a0, B:271:0x03a7, B:272:0x03a8, B:273:0x03c4, B:276:0x03ca, B:280:0x03d4, B:282:0x03dc, B:284:0x03e9, B:286:0x03f6, B:288:0x03fc, B:292:0x0415, B:294:0x041f, B:297:0x0431, B:300:0x0438, B:301:0x043f, B:302:0x0440, B:304:0x044d, B:306:0x0451, B:308:0x045a, B:309:0x0462, B:314:0x046e, B:315:0x0475, B:322:0x0489, B:324:0x048f, B:326:0x049b), top: B:506:0x03ca }] */
    /* JADX WARN: Removed duplicated region for block: B:333:0x04aa  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x005f A[Catch: all -> 0x0040, TRY_LEAVE, TryCatch #0 {all -> 0x0040, blocks: (B:17:0x0030, B:19:0x0035, B:28:0x004a, B:30:0x0050, B:36:0x005f, B:42:0x006e, B:47:0x007a, B:49:0x0084, B:52:0x008b, B:54:0x00a0, B:55:0x00a8, B:56:0x00b1, B:60:0x00b7), top: B:505:0x002e }] */
    /* JADX WARN: Removed duplicated region for block: B:419:0x05b8 A[Catch: all -> 0x071a, TryCatch #7 {all -> 0x071a, blocks: (B:334:0x04ac, B:428:0x05e7, B:431:0x05ef, B:433:0x05f5, B:486:0x06d9, B:488:0x06df, B:491:0x06f5, B:492:0x0711, B:337:0x04b9, B:342:0x04c2, B:347:0x04cb, B:352:0x04d4, B:362:0x04f9, B:416:0x05b0, B:358:0x04e0, B:359:0x04e8, B:360:0x04f0, B:361:0x04f5, B:366:0x050a, B:371:0x0514, B:376:0x051d, B:381:0x0526, B:386:0x052f, B:387:0x0536, B:389:0x053a, B:391:0x053e, B:392:0x0543, B:393:0x054c, B:395:0x0550, B:397:0x0554, B:398:0x0558, B:399:0x0561, B:401:0x0565, B:403:0x0569, B:404:0x056f, B:405:0x0579, B:407:0x057d, B:409:0x0581, B:410:0x0587, B:415:0x05ab, B:413:0x0592, B:414:0x05aa, B:419:0x05b8, B:421:0x05d0, B:423:0x05d6, B:426:0x05e1, B:493:0x0712, B:494:0x0719), top: B:516:0x04ac, inners: #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:430:0x05ed  */
    /* JADX WARN: Removed duplicated region for block: B:431:0x05ef A[Catch: all -> 0x071a, TryCatch #7 {all -> 0x071a, blocks: (B:334:0x04ac, B:428:0x05e7, B:431:0x05ef, B:433:0x05f5, B:486:0x06d9, B:488:0x06df, B:491:0x06f5, B:492:0x0711, B:337:0x04b9, B:342:0x04c2, B:347:0x04cb, B:352:0x04d4, B:362:0x04f9, B:416:0x05b0, B:358:0x04e0, B:359:0x04e8, B:360:0x04f0, B:361:0x04f5, B:366:0x050a, B:371:0x0514, B:376:0x051d, B:381:0x0526, B:386:0x052f, B:387:0x0536, B:389:0x053a, B:391:0x053e, B:392:0x0543, B:393:0x054c, B:395:0x0550, B:397:0x0554, B:398:0x0558, B:399:0x0561, B:401:0x0565, B:403:0x0569, B:404:0x056f, B:405:0x0579, B:407:0x057d, B:409:0x0581, B:410:0x0587, B:415:0x05ab, B:413:0x0592, B:414:0x05aa, B:419:0x05b8, B:421:0x05d0, B:423:0x05d6, B:426:0x05e1, B:493:0x0712, B:494:0x0719), top: B:516:0x04ac, inners: #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:500:0x0724  */
    /* JADX WARN: Removed duplicated region for block: B:523:0x046e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:525:0x047c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0110 A[Catch: all -> 0x00e5, TryCatch #4 {all -> 0x00e5, blocks: (B:65:0x00d0, B:67:0x00d6, B:75:0x00f3, B:80:0x0110, B:85:0x0121, B:92:0x012f, B:225:0x02fb, B:227:0x0303, B:240:0x0334, B:244:0x0340, B:246:0x0346, B:247:0x0349, B:252:0x035e, B:254:0x0362, B:255:0x0365, B:257:0x0369, B:258:0x036c, B:95:0x0142, B:97:0x0146, B:101:0x0159, B:104:0x0162, B:106:0x0166, B:107:0x016a, B:112:0x0174, B:117:0x017e, B:122:0x0188, B:124:0x018c, B:126:0x0196, B:128:0x019e, B:129:0x01a5, B:131:0x01a9, B:136:0x01c1, B:139:0x01cb, B:141:0x01cf, B:144:0x01d7, B:147:0x01e1, B:149:0x01e5, B:152:0x01ed, B:155:0x01f7, B:157:0x01fb, B:160:0x0203, B:163:0x020d, B:165:0x0211, B:168:0x0219, B:171:0x0223, B:173:0x0227, B:174:0x022b, B:177:0x023b, B:180:0x024c, B:182:0x0250, B:183:0x0254, B:187:0x026e, B:189:0x0272, B:191:0x027a, B:194:0x0288, B:196:0x028c, B:197:0x028f, B:201:0x02a7, B:203:0x02ab, B:205:0x02b4, B:209:0x02c8, B:211:0x02cc, B:76:0x0103), top: B:511:0x00d0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2) {
        Object obj3;
        Class<?> cls;
        FieldInfo fieldInfo;
        Class<?> cls2;
        FieldDeserializer fieldDeserializer;
        FieldInfo fieldInfo2;
        int i;
        FieldInfo fieldInfo3;
        Class<?> cls3;
        int i2;
        long j;
        int i3;
        double d;
        float f;
        boolean z;
        boolean z2;
        Object obj4;
        ParseContext parseContext;
        int i4;
        long j2;
        float f2;
        FieldInfo fieldInfo4;
        double d2;
        String str;
        HashMap hashMap;
        Object obj5;
        int i5;
        String str2;
        int i6;
        Object valueOf;
        Object valueOf2;
        Enum r11;
        boolean z3;
        boolean z4;
        boolean z5;
        Type type2 = type;
        if (type2 == JSON.class || type2 == JSONObject.class) {
            return (T) defaultJSONParser.parse();
        }
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i7 = jSONLexer.token;
        ParseContext parseContext2 = null;
        if (i7 == 8) {
            jSONLexer.nextToken(16);
            return null;
        }
        boolean z6 = jSONLexer.disableCircularReferenceDetect;
        ParseContext parseContext3 = defaultJSONParser.contex;
        if (obj2 != null && parseContext3 != null) {
            parseContext3 = parseContext3.parent;
        }
        ParseContext parseContext4 = parseContext3;
        try {
            if (i7 == 13) {
                jSONLexer.nextToken(16);
                T t = obj2 == null ? (T) createInstance(defaultJSONParser, type) : (T) obj2;
                defaultJSONParser.setContext(parseContext4);
                return t;
            }
            int i8 = 0;
            if (i7 == 14) {
                if (!this.beanInfo.supportBeanToArray && (jSONLexer.features & Feature.SupportArrayToBean.mask) == 0) {
                    z5 = false;
                    if (z5) {
                        T t2 = (T) deserialzeArrayMapping(defaultJSONParser, type, obj, obj2);
                        defaultJSONParser.setContext(parseContext4);
                        return t2;
                    }
                }
                z5 = true;
                if (z5) {
                }
            }
            if (i7 != 12 && i7 != 16) {
                if (jSONLexer.isBlankInput()) {
                    defaultJSONParser.setContext(parseContext4);
                    return null;
                } else if (i7 == 4 && jSONLexer.stringVal().length() == 0) {
                    jSONLexer.nextToken();
                    defaultJSONParser.setContext(parseContext4);
                    return null;
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("syntax error, expect {, actual ");
                    stringBuffer.append(jSONLexer.info());
                    if (obj instanceof String) {
                        stringBuffer.append(", fieldName ");
                        stringBuffer.append(obj);
                    }
                    throw new JSONException(stringBuffer.toString());
                }
            }
            try {
                if (defaultJSONParser.resolveStatus == 2) {
                    defaultJSONParser.resolveStatus = 0;
                }
                String str3 = this.beanInfo.typeKey;
                int length = this.sortedFieldDeserializers.length;
                obj3 = obj2;
                ParseContext parseContext5 = null;
                HashMap hashMap2 = null;
                long j3 = 0;
                while (true) {
                    if (j3 != 0) {
                        try {
                            FieldDeserializer fieldDeserializerByHash = getFieldDeserializerByHash(j3);
                            if (fieldDeserializerByHash != null) {
                                fieldInfo = fieldDeserializerByHash.fieldInfo;
                                cls = fieldInfo.fieldClass;
                            } else {
                                cls = null;
                                fieldInfo = null;
                            }
                            cls2 = cls;
                            fieldDeserializer = fieldDeserializerByHash;
                            fieldInfo2 = fieldInfo;
                            j3 = 0;
                        } catch (Throwable th) {
                            th = th;
                            parseContext2 = parseContext5;
                            if (parseContext2 != null) {
                            }
                            defaultJSONParser.setContext(parseContext4);
                            throw th;
                        }
                    } else {
                        fieldDeserializer = null;
                        fieldInfo2 = null;
                        cls2 = null;
                    }
                    if (fieldDeserializer == null) {
                        if (i8 < length) {
                            fieldDeserializer = this.sortedFieldDeserializers[i8];
                            fieldInfo3 = fieldDeserializer.fieldInfo;
                            i = i8 + 1;
                            cls3 = fieldInfo3.fieldClass;
                            double d3 = 0.0d;
                            if (fieldDeserializer == null) {
                                i2 = i;
                                j = j3;
                                long j4 = fieldInfo3.nameHashCode;
                                if (cls3 != Integer.TYPE && cls3 != Integer.class) {
                                    if (cls3 != Long.TYPE && cls3 != Long.class) {
                                        if (cls3 == String.class) {
                                            valueOf2 = jSONLexer.scanFieldString(j4);
                                            if (jSONLexer.matchStat <= 0) {
                                                if (jSONLexer.matchStat == -2) {
                                                    j3 = jSONLexer.fieldHash;
                                                    i8 = i2;
                                                }
                                                i3 = length;
                                                obj4 = valueOf2;
                                                d = 0.0d;
                                                f = 0.0f;
                                                z = false;
                                                z2 = false;
                                                i4 = 0;
                                                j2 = 0;
                                            }
                                            i3 = length;
                                            obj4 = valueOf2;
                                            d = 0.0d;
                                            f = 0.0f;
                                            z = true;
                                            z2 = true;
                                            i4 = 0;
                                            j2 = 0;
                                        } else if (cls3 == Date.class) {
                                            valueOf2 = jSONLexer.scanFieldDate(j4);
                                            if (jSONLexer.matchStat > 0) {
                                                i3 = length;
                                                obj4 = valueOf2;
                                                d = 0.0d;
                                                f = 0.0f;
                                                z = true;
                                                z2 = true;
                                                i4 = 0;
                                                j2 = 0;
                                            } else {
                                                if (jSONLexer.matchStat == -2) {
                                                    j3 = jSONLexer.fieldHash;
                                                    i8 = i2;
                                                }
                                                i3 = length;
                                                obj4 = valueOf2;
                                                d = 0.0d;
                                                f = 0.0f;
                                                z = false;
                                                z2 = false;
                                                i4 = 0;
                                                j2 = 0;
                                            }
                                        } else {
                                            if (cls3 != Boolean.TYPE && cls3 != Boolean.class) {
                                                if (cls3 != Float.TYPE && cls3 != Float.class) {
                                                    if (cls3 != Double.TYPE && cls3 != Double.class) {
                                                        if (fieldInfo3.isEnum && (defaultJSONParser.config.getDeserializer(cls3) instanceof EnumDeserializer)) {
                                                            long scanFieldSymbol = jSONLexer.scanFieldSymbol(j4);
                                                            if (jSONLexer.matchStat > 0) {
                                                                r11 = fieldDeserializer.getEnumByHashCode(scanFieldSymbol);
                                                                z3 = true;
                                                                z4 = true;
                                                            } else if (jSONLexer.matchStat == -2) {
                                                                j3 = jSONLexer.fieldHash;
                                                            } else {
                                                                r11 = null;
                                                                z3 = false;
                                                                z4 = false;
                                                            }
                                                            i3 = length;
                                                            obj4 = r11;
                                                            z2 = z4;
                                                            f = 0.0f;
                                                            i4 = 0;
                                                            z = z3;
                                                            d = 0.0d;
                                                            j2 = 0;
                                                        } else if (cls3 == int[].class) {
                                                            valueOf2 = jSONLexer.scanFieldIntArray(j4);
                                                            if (jSONLexer.matchStat > 0) {
                                                                i3 = length;
                                                                obj4 = valueOf2;
                                                                d = 0.0d;
                                                                f = 0.0f;
                                                                z = true;
                                                                z2 = true;
                                                                i4 = 0;
                                                                j2 = 0;
                                                            } else {
                                                                if (jSONLexer.matchStat == -2) {
                                                                    j3 = jSONLexer.fieldHash;
                                                                }
                                                                i3 = length;
                                                                obj4 = valueOf2;
                                                                d = 0.0d;
                                                                f = 0.0f;
                                                                z = false;
                                                                z2 = false;
                                                                i4 = 0;
                                                                j2 = 0;
                                                            }
                                                        } else if (cls3 == float[].class) {
                                                            valueOf2 = jSONLexer.scanFieldFloatArray(j4);
                                                            if (jSONLexer.matchStat > 0) {
                                                                i3 = length;
                                                                obj4 = valueOf2;
                                                                d = 0.0d;
                                                                f = 0.0f;
                                                                z = true;
                                                                z2 = true;
                                                                i4 = 0;
                                                                j2 = 0;
                                                            } else {
                                                                if (jSONLexer.matchStat == -2) {
                                                                    j3 = jSONLexer.fieldHash;
                                                                }
                                                                i3 = length;
                                                                obj4 = valueOf2;
                                                                d = 0.0d;
                                                                f = 0.0f;
                                                                z = false;
                                                                z2 = false;
                                                                i4 = 0;
                                                                j2 = 0;
                                                            }
                                                        } else if (cls3 == double[].class) {
                                                            valueOf2 = jSONLexer.scanFieldDoubleArray(j4);
                                                            if (jSONLexer.matchStat > 0) {
                                                                i3 = length;
                                                                obj4 = valueOf2;
                                                                d = 0.0d;
                                                                f = 0.0f;
                                                                z = true;
                                                                z2 = true;
                                                                i4 = 0;
                                                                j2 = 0;
                                                            } else {
                                                                if (jSONLexer.matchStat == -2) {
                                                                    j3 = jSONLexer.fieldHash;
                                                                }
                                                                i3 = length;
                                                                obj4 = valueOf2;
                                                                d = 0.0d;
                                                                f = 0.0f;
                                                                z = false;
                                                                z2 = false;
                                                                i4 = 0;
                                                                j2 = 0;
                                                            }
                                                        } else if (cls3 == float[][].class) {
                                                            valueOf2 = jSONLexer.scanFieldFloatArray2(j4);
                                                            if (jSONLexer.matchStat > 0) {
                                                                i3 = length;
                                                                obj4 = valueOf2;
                                                                d = 0.0d;
                                                                f = 0.0f;
                                                                z = true;
                                                                z2 = true;
                                                                i4 = 0;
                                                                j2 = 0;
                                                            } else {
                                                                if (jSONLexer.matchStat == -2) {
                                                                    j3 = jSONLexer.fieldHash;
                                                                }
                                                                i3 = length;
                                                                obj4 = valueOf2;
                                                                d = 0.0d;
                                                                f = 0.0f;
                                                                z = false;
                                                                z2 = false;
                                                                i4 = 0;
                                                                j2 = 0;
                                                            }
                                                        } else if (cls3 == double[][].class) {
                                                            valueOf2 = jSONLexer.scanFieldDoubleArray2(j4);
                                                            if (jSONLexer.matchStat > 0) {
                                                                i3 = length;
                                                                obj4 = valueOf2;
                                                                d = 0.0d;
                                                                f = 0.0f;
                                                                z = true;
                                                                z2 = true;
                                                                i4 = 0;
                                                                j2 = 0;
                                                            } else {
                                                                if (jSONLexer.matchStat == -2) {
                                                                    j3 = jSONLexer.fieldHash;
                                                                }
                                                                i3 = length;
                                                                obj4 = valueOf2;
                                                                d = 0.0d;
                                                                f = 0.0f;
                                                                z = false;
                                                                z2 = false;
                                                                i4 = 0;
                                                                j2 = 0;
                                                            }
                                                        } else {
                                                            i3 = length;
                                                            if (jSONLexer.matchField(fieldInfo3.nameHashCode)) {
                                                                d = 0.0d;
                                                                f = 0.0f;
                                                                z = true;
                                                                z2 = false;
                                                                obj4 = null;
                                                                i4 = 0;
                                                                j2 = 0;
                                                            } else {
                                                                parseContext = parseContext5;
                                                                i8 = i2;
                                                                parseContext5 = parseContext;
                                                                length = i3;
                                                                j3 = j;
                                                            }
                                                        }
                                                        i8 = i2;
                                                    }
                                                    d3 = jSONLexer.scanFieldDouble(j4);
                                                    if (jSONLexer.matchStat > 0) {
                                                        i3 = length;
                                                        d = d3;
                                                        f = 0.0f;
                                                        z = true;
                                                        z2 = true;
                                                        obj4 = null;
                                                        i4 = 0;
                                                        j2 = 0;
                                                    } else if (jSONLexer.matchStat == -2) {
                                                        j3 = jSONLexer.fieldHash;
                                                        i8 = i2;
                                                    }
                                                }
                                                float scanFieldFloat = jSONLexer.scanFieldFloat(j4);
                                                if (jSONLexer.matchStat > 0) {
                                                    d = 0.0d;
                                                    z = true;
                                                    z2 = true;
                                                } else if (jSONLexer.matchStat == -2) {
                                                    j3 = jSONLexer.fieldHash;
                                                    i8 = i2;
                                                } else {
                                                    d = 0.0d;
                                                    z = false;
                                                    z2 = false;
                                                }
                                                obj4 = null;
                                                i4 = 0;
                                                j2 = 0;
                                                i3 = length;
                                                f = scanFieldFloat;
                                            }
                                            valueOf2 = Boolean.valueOf(jSONLexer.scanFieldBoolean(j4));
                                            if (jSONLexer.matchStat > 0) {
                                                i3 = length;
                                                obj4 = valueOf2;
                                                d = 0.0d;
                                                f = 0.0f;
                                                z = true;
                                                z2 = true;
                                                i4 = 0;
                                                j2 = 0;
                                            } else {
                                                if (jSONLexer.matchStat == -2) {
                                                    j3 = jSONLexer.fieldHash;
                                                    i8 = i2;
                                                }
                                                i3 = length;
                                                obj4 = valueOf2;
                                                d = 0.0d;
                                                f = 0.0f;
                                                z = false;
                                                z2 = false;
                                                i4 = 0;
                                                j2 = 0;
                                            }
                                        }
                                        if (z) {
                                            f2 = f;
                                            fieldInfo4 = fieldInfo3;
                                            d2 = d;
                                            parseContext = parseContext5;
                                            str = null;
                                        } else {
                                            fieldInfo4 = fieldInfo3;
                                            try {
                                                str = jSONLexer.scanSymbol(defaultJSONParser.symbolTable);
                                                if (str == null) {
                                                    f2 = f;
                                                    int i9 = jSONLexer.token;
                                                    d2 = d;
                                                    if (i9 == 13) {
                                                        jSONLexer.nextToken(16);
                                                        parseContext = parseContext5;
                                                        break;
                                                    } else if (i9 == 16) {
                                                        parseContext = parseContext5;
                                                        i8 = i2;
                                                        parseContext5 = parseContext;
                                                        length = i3;
                                                        j3 = j;
                                                    }
                                                } else {
                                                    f2 = f;
                                                    d2 = d;
                                                }
                                                if ("$ref" == str && parseContext4 != null) {
                                                    break;
                                                }
                                                parseContext = parseContext5;
                                                if (str3 != null) {
                                                    try {
                                                        if (str3.equals(str)) {
                                                            jSONLexer.nextTokenWithChar(':');
                                                            if (jSONLexer.token == 4) {
                                                                throw new JSONException("syntax error");
                                                            }
                                                            String stringVal = jSONLexer.stringVal();
                                                            jSONLexer.nextToken(16);
                                                            if (!(type2 instanceof Class) || !stringVal.equals(((Class) type2).getName())) {
                                                                break;
                                                            }
                                                            if (jSONLexer.token == 13) {
                                                                jSONLexer.nextToken();
                                                                break;
                                                            }
                                                            i8 = i2;
                                                            parseContext5 = parseContext;
                                                            length = i3;
                                                            j3 = j;
                                                        }
                                                    } catch (Throwable th2) {
                                                        th = th2;
                                                        parseContext2 = parseContext;
                                                        if (parseContext2 != null) {
                                                            parseContext2.object = obj3;
                                                        }
                                                        defaultJSONParser.setContext(parseContext4);
                                                        throw th;
                                                    }
                                                }
                                                if (JSON.DEFAULT_TYPE_KEY == str) {
                                                    jSONLexer.nextTokenWithChar(':');
                                                    if (jSONLexer.token == 4) {
                                                    }
                                                }
                                            } catch (Throwable th3) {
                                                th = th3;
                                                parseContext = parseContext5;
                                            }
                                        }
                                        if (obj3 == null && hashMap2 == null) {
                                            obj3 = (T) createInstance(defaultJSONParser, type);
                                            if (obj3 == null) {
                                                hashMap2 = new HashMap(this.fieldDeserializers.length);
                                            }
                                            if (!z6) {
                                                parseContext5 = defaultJSONParser.setContext(parseContext4, obj3, obj);
                                                obj5 = (T) obj3;
                                                hashMap = hashMap2;
                                                if (z) {
                                                    i5 = i3;
                                                    str2 = str3;
                                                    i6 = 1;
                                                    if (parseField(defaultJSONParser, str, obj5, type, hashMap)) {
                                                        if (jSONLexer.token == 17) {
                                                            throw new JSONException("syntax error, unexpect token ':'");
                                                        }
                                                        if (jSONLexer.token != 16) {
                                                        }
                                                    } else if (jSONLexer.token == 13) {
                                                        jSONLexer.nextToken();
                                                        break;
                                                    } else {
                                                        i8 = i2;
                                                        length = i5;
                                                        obj3 = (T) obj5;
                                                        hashMap2 = hashMap;
                                                        str3 = str2;
                                                        j3 = j;
                                                        type2 = type;
                                                    }
                                                } else {
                                                    if (z2) {
                                                        if (obj5 == null) {
                                                            if (cls3 != Integer.TYPE && cls3 != Integer.class) {
                                                                if (cls3 != Long.TYPE && cls3 != Long.class) {
                                                                    if (cls3 != Float.TYPE && cls3 != Float.class) {
                                                                        if (cls3 != Double.TYPE && cls3 != Double.class) {
                                                                            valueOf = obj4;
                                                                            hashMap.put(fieldInfo4.name, valueOf);
                                                                        }
                                                                        valueOf = new Double(d2);
                                                                        hashMap.put(fieldInfo4.name, valueOf);
                                                                    }
                                                                    valueOf = new Float(f2);
                                                                    hashMap.put(fieldInfo4.name, valueOf);
                                                                }
                                                                valueOf = Long.valueOf(j2);
                                                                hashMap.put(fieldInfo4.name, valueOf);
                                                            }
                                                            valueOf = Integer.valueOf(i4);
                                                            hashMap.put(fieldInfo4.name, valueOf);
                                                        } else {
                                                            FieldInfo fieldInfo5 = fieldInfo4;
                                                            float f3 = f2;
                                                            double d4 = d2;
                                                            if (obj4 == null) {
                                                                try {
                                                                    if (cls3 != Integer.TYPE && cls3 != Integer.class) {
                                                                        if (cls3 != Long.TYPE && cls3 != Long.class) {
                                                                            if (cls3 != Float.TYPE && cls3 != Float.class) {
                                                                                if (cls3 != Double.TYPE && cls3 != Double.class) {
                                                                                    fieldDeserializer.setValue(obj5, obj4);
                                                                                }
                                                                                if (fieldInfo5.fieldAccess && cls3 == Double.TYPE) {
                                                                                    fieldDeserializer.setValue(obj5, d4);
                                                                                } else {
                                                                                    fieldDeserializer.setValue(obj5, new Double(d4));
                                                                                }
                                                                            }
                                                                            if (fieldInfo5.fieldAccess && cls3 == Float.TYPE) {
                                                                                fieldDeserializer.setValue(obj5, f3);
                                                                            } else {
                                                                                fieldDeserializer.setValue(obj5, new Float(f3));
                                                                            }
                                                                        }
                                                                        if (fieldInfo5.fieldAccess && cls3 == Long.TYPE) {
                                                                            fieldDeserializer.setValue(obj5, j2);
                                                                        } else {
                                                                            fieldDeserializer.setValue(obj5, Long.valueOf(j2));
                                                                        }
                                                                    }
                                                                    if (fieldInfo5.fieldAccess && cls3 == Integer.TYPE) {
                                                                        fieldDeserializer.setValue(obj5, i4);
                                                                    } else {
                                                                        fieldDeserializer.setValue(obj5, Integer.valueOf(i4));
                                                                    }
                                                                } catch (IllegalAccessException e) {
                                                                    throw new JSONException("set property error, " + fieldInfo5.name, e);
                                                                }
                                                            } else {
                                                                fieldDeserializer.setValue(obj5, obj4);
                                                            }
                                                        }
                                                        if (jSONLexer.matchStat == 4) {
                                                            break;
                                                        }
                                                        str2 = str3;
                                                        i5 = i3;
                                                        i6 = 1;
                                                    } else {
                                                        try {
                                                            fieldDeserializer.parseField(defaultJSONParser, obj5, type2, hashMap);
                                                            str2 = str3;
                                                            i5 = i3;
                                                            i6 = 1;
                                                        } catch (Throwable th4) {
                                                            th = th4;
                                                            obj3 = (T) obj5;
                                                            parseContext2 = parseContext5;
                                                            if (parseContext2 != null) {
                                                            }
                                                            defaultJSONParser.setContext(parseContext4);
                                                            throw th;
                                                        }
                                                    }
                                                    if (jSONLexer.token != 16) {
                                                        i8 = i2;
                                                        length = i5;
                                                        obj3 = (T) obj5;
                                                        hashMap2 = hashMap;
                                                        str3 = str2;
                                                        j3 = j;
                                                        type2 = type;
                                                    } else if (jSONLexer.token == 13) {
                                                        jSONLexer.nextToken(16);
                                                        break;
                                                    } else {
                                                        if (jSONLexer.token == 18 || jSONLexer.token == i6) {
                                                            break;
                                                        }
                                                        i8 = i2;
                                                        length = i5;
                                                        obj3 = (T) obj5;
                                                        hashMap2 = hashMap;
                                                        str3 = str2;
                                                        j3 = j;
                                                        type2 = type;
                                                    }
                                                }
                                            }
                                        }
                                        obj5 = obj3;
                                        parseContext5 = parseContext;
                                        hashMap = hashMap2;
                                        if (z) {
                                        }
                                    }
                                    long scanFieldLong = jSONLexer.scanFieldLong(j4);
                                    if (jSONLexer.matchStat > 0) {
                                        i3 = length;
                                        f = 0.0f;
                                        z = true;
                                        z2 = true;
                                    } else if (jSONLexer.matchStat == -2) {
                                        j3 = jSONLexer.fieldHash;
                                        i8 = i2;
                                    } else {
                                        i3 = length;
                                        f = 0.0f;
                                        z = false;
                                        z2 = false;
                                    }
                                    obj4 = null;
                                    i4 = 0;
                                    d = 0.0d;
                                    j2 = scanFieldLong;
                                    if (z) {
                                    }
                                    if (obj3 == null) {
                                        obj3 = (T) createInstance(defaultJSONParser, type);
                                        if (obj3 == null) {
                                        }
                                        if (!z6) {
                                        }
                                    }
                                    obj5 = obj3;
                                    parseContext5 = parseContext;
                                    hashMap = hashMap2;
                                    if (z) {
                                    }
                                }
                                int scanFieldInt = jSONLexer.scanFieldInt(j4);
                                if (jSONLexer.matchStat > 0) {
                                    i3 = length;
                                    i4 = scanFieldInt;
                                    d = 0.0d;
                                    f = 0.0f;
                                    z = true;
                                    z2 = true;
                                } else if (jSONLexer.matchStat == -2) {
                                    j3 = jSONLexer.fieldHash;
                                    i8 = i2;
                                } else {
                                    i3 = length;
                                    i4 = scanFieldInt;
                                    d = 0.0d;
                                    f = 0.0f;
                                    z = false;
                                    z2 = false;
                                }
                                obj4 = null;
                                j2 = 0;
                                if (z) {
                                }
                                if (obj3 == null) {
                                }
                                obj5 = obj3;
                                parseContext5 = parseContext;
                                hashMap = hashMap2;
                                if (z) {
                                }
                            } else {
                                i2 = i;
                                j = j3;
                            }
                            i3 = length;
                            d = d3;
                            f = 0.0f;
                            z = false;
                            z2 = false;
                            obj4 = null;
                            i4 = 0;
                            j2 = 0;
                            if (z) {
                            }
                            if (obj3 == null) {
                            }
                            obj5 = obj3;
                            parseContext5 = parseContext;
                            hashMap = hashMap2;
                            if (z) {
                            }
                        } else {
                            i8++;
                        }
                    }
                    i = i8;
                    fieldInfo3 = fieldInfo2;
                    cls3 = cls2;
                    double d32 = 0.0d;
                    if (fieldDeserializer == null) {
                    }
                    i3 = length;
                    d = d32;
                    f = 0.0f;
                    z = false;
                    z2 = false;
                    obj4 = null;
                    i4 = 0;
                    j2 = 0;
                    if (z) {
                    }
                    if (obj3 == null) {
                    }
                    obj5 = obj3;
                    parseContext5 = parseContext;
                    hashMap = hashMap2;
                    if (z) {
                    }
                }
                obj5 = (T) obj3;
                hashMap = hashMap2;
                if (obj5 == null) {
                    try {
                        if (hashMap == null) {
                            T t3 = (T) createInstance(defaultJSONParser, type);
                            if (parseContext == null) {
                                parseContext = defaultJSONParser.setContext(parseContext4, t3, obj);
                            }
                            if (parseContext != null) {
                                parseContext.object = t3;
                            }
                            defaultJSONParser.setContext(parseContext4);
                            return t3;
                        }
                        String[] strArr = this.beanInfo.creatorConstructorParameters;
                        int length2 = strArr != null ? strArr.length : this.fieldDeserializers.length;
                        Object[] objArr = new Object[length2];
                        for (int i10 = 0; i10 < length2; i10++) {
                            FieldInfo fieldInfo6 = this.fieldDeserializers[i10].fieldInfo;
                            Object remove = strArr != null ? hashMap.remove(fieldInfo6.name) : hashMap.get(fieldInfo6.name);
                            if (remove == null) {
                                remove = TypeUtils.defaultValue(fieldInfo6.fieldClass);
                            }
                            objArr[i10] = remove;
                        }
                        if (this.beanInfo.creatorConstructor != null) {
                            try {
                                Object newInstance = this.beanInfo.creatorConstructor.newInstance(objArr);
                                if (strArr != null) {
                                    for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                                        FieldDeserializer fieldDeserializer2 = getFieldDeserializer(entry.getKey());
                                        if (fieldDeserializer2 != null) {
                                            fieldDeserializer2.setValue(newInstance, entry.getValue());
                                        }
                                    }
                                }
                                obj5 = (T) newInstance;
                            } catch (Exception e2) {
                                throw new JSONException("create instance error, " + this.beanInfo.creatorConstructor.toGenericString(), e2);
                            }
                        } else if (this.beanInfo.factoryMethod != null) {
                            try {
                                obj5 = (T) this.beanInfo.factoryMethod.invoke(null, objArr);
                            } catch (Exception e3) {
                                throw new JSONException("create factory method error, " + this.beanInfo.factoryMethod.toString(), e3);
                            }
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        parseContext2 = parseContext;
                        obj3 = obj5;
                        if (parseContext2 != null) {
                        }
                        defaultJSONParser.setContext(parseContext4);
                        throw th;
                    }
                }
                if (parseContext != null) {
                    parseContext.object = obj5;
                }
                defaultJSONParser.setContext(parseContext4);
                return (T) obj5;
            } catch (Throwable th6) {
                th = th6;
                obj3 = obj2;
                if (parseContext2 != null) {
                }
                defaultJSONParser.setContext(parseContext4);
                throw th;
            }
        } catch (Throwable th7) {
            th = th7;
        }
    }

    protected FieldDeserializer getFieldDeserializerByHash(long j) {
        int i = 0;
        while (true) {
            FieldDeserializer[] fieldDeserializerArr = this.sortedFieldDeserializers;
            if (i >= fieldDeserializerArr.length) {
                return null;
            }
            FieldDeserializer fieldDeserializer = fieldDeserializerArr[i];
            if (fieldDeserializer.fieldInfo.nameHashCode == j) {
                return fieldDeserializer;
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FieldDeserializer getFieldDeserializer(String str) {
        if (str == null) {
            return null;
        }
        int i = 0;
        if (!this.beanInfo.ordered) {
            int length = this.sortedFieldDeserializers.length - 1;
            while (i <= length) {
                int i2 = (i + length) >>> 1;
                int compareTo = this.sortedFieldDeserializers[i2].fieldInfo.name.compareTo(str);
                if (compareTo < 0) {
                    i = i2 + 1;
                } else if (compareTo <= 0) {
                    return this.sortedFieldDeserializers[i2];
                } else {
                    length = i2 - 1;
                }
            }
            Map<String, FieldDeserializer> map = this.alterNameFieldDeserializers;
            if (map != null) {
                return map.get(str);
            }
            return null;
        }
        while (true) {
            FieldDeserializer[] fieldDeserializerArr = this.sortedFieldDeserializers;
            if (i >= fieldDeserializerArr.length) {
                return null;
            }
            FieldDeserializer fieldDeserializer = fieldDeserializerArr[i];
            if (fieldDeserializer.fieldInfo.name.equalsIgnoreCase(str)) {
                return fieldDeserializer;
            }
            i++;
        }
    }

    private boolean parseField(DefaultJSONParser defaultJSONParser, String str, Object obj, Type type, Map<String, Object> map) {
        Field[] declaredFields;
        boolean z;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        FieldDeserializer fieldDeserializer = getFieldDeserializer(str);
        if (fieldDeserializer == null) {
            long fnv_64_lower = TypeUtils.fnv_64_lower(str);
            if (this.smartMatchHashArray == null) {
                long[] jArr = new long[this.sortedFieldDeserializers.length];
                int i = 0;
                while (true) {
                    FieldDeserializer[] fieldDeserializerArr = this.sortedFieldDeserializers;
                    if (i >= fieldDeserializerArr.length) {
                        break;
                    }
                    jArr[i] = TypeUtils.fnv_64_lower(fieldDeserializerArr[i].fieldInfo.name);
                    i++;
                }
                Arrays.sort(jArr);
                this.smartMatchHashArray = jArr;
            }
            int binarySearch = Arrays.binarySearch(this.smartMatchHashArray, fnv_64_lower);
            if (binarySearch < 0) {
                z = str.startsWith("is");
                if (z) {
                    binarySearch = Arrays.binarySearch(this.smartMatchHashArray, TypeUtils.fnv_64_lower(str.substring(2)));
                }
            } else {
                z = false;
            }
            if (binarySearch >= 0) {
                if (this.smartMatchHashArrayMapping == null) {
                    int[] iArr = new int[this.smartMatchHashArray.length];
                    Arrays.fill(iArr, -1);
                    int i2 = 0;
                    while (true) {
                        FieldDeserializer[] fieldDeserializerArr2 = this.sortedFieldDeserializers;
                        if (i2 >= fieldDeserializerArr2.length) {
                            break;
                        }
                        int binarySearch2 = Arrays.binarySearch(this.smartMatchHashArray, TypeUtils.fnv_64_lower(fieldDeserializerArr2[i2].fieldInfo.name));
                        if (binarySearch2 >= 0) {
                            iArr[binarySearch2] = i2;
                        }
                        i2++;
                    }
                    this.smartMatchHashArrayMapping = iArr;
                }
                int i3 = this.smartMatchHashArrayMapping[binarySearch];
                if (i3 != -1) {
                    fieldDeserializer = this.sortedFieldDeserializers[i3];
                    Class<?> cls = fieldDeserializer.fieldInfo.fieldClass;
                    if (z && cls != Boolean.TYPE && cls != Boolean.class) {
                        fieldDeserializer = null;
                    }
                }
            }
        }
        int i4 = Feature.SupportNonPublicField.mask;
        if (fieldDeserializer == null && ((defaultJSONParser.lexer.features & i4) != 0 || (i4 & this.beanInfo.parserFeatures) != 0)) {
            if (this.extraFieldDeserializers == null) {
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(1, 0.75f, 1);
                for (Class<?> cls2 = this.clazz; cls2 != null && cls2 != Object.class; cls2 = cls2.getSuperclass()) {
                    for (Field field : cls2.getDeclaredFields()) {
                        String name = field.getName();
                        if (getFieldDeserializer(name) == null) {
                            int modifiers = field.getModifiers();
                            if ((modifiers & 16) == 0 && (modifiers & 8) == 0) {
                                concurrentHashMap.put(name, field);
                            }
                        }
                    }
                }
                this.extraFieldDeserializers = concurrentHashMap;
            }
            Object obj2 = this.extraFieldDeserializers.get(str);
            if (obj2 != null) {
                if (obj2 instanceof FieldDeserializer) {
                    fieldDeserializer = (FieldDeserializer) obj2;
                } else {
                    Field field2 = (Field) obj2;
                    field2.setAccessible(true);
                    fieldDeserializer = new DefaultFieldDeserializer(defaultJSONParser.config, this.clazz, new FieldInfo(str, field2.getDeclaringClass(), field2.getType(), field2.getGenericType(), field2, 0, 0));
                    this.extraFieldDeserializers.put(str, fieldDeserializer);
                }
            }
        }
        if (fieldDeserializer == null) {
            parseExtra(defaultJSONParser, obj, str);
            return false;
        }
        jSONLexer.nextTokenWithChar(':');
        fieldDeserializer.parseField(defaultJSONParser, obj, type, map);
        return true;
    }

    void parseExtra(DefaultJSONParser defaultJSONParser, Object obj, String str) {
        Object parseObject;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if ((defaultJSONParser.lexer.features & Feature.IgnoreNotMatch.mask) == 0) {
            throw new JSONException("setter not found, class " + this.clazz.getName() + ", property " + str);
        }
        jSONLexer.nextTokenWithChar(':');
        Type type = null;
        List<ExtraTypeProvider> list = defaultJSONParser.extraTypeProviders;
        if (list != null) {
            for (ExtraTypeProvider extraTypeProvider : list) {
                type = extraTypeProvider.getExtraType(obj, str);
            }
        }
        if (type == null) {
            parseObject = defaultJSONParser.parse();
        } else {
            parseObject = defaultJSONParser.parseObject(type);
        }
        if (obj instanceof ExtraProcessable) {
            ((ExtraProcessable) obj).processExtra(str, parseObject);
            return;
        }
        List<ExtraProcessor> list2 = defaultJSONParser.extraProcessors;
        if (list2 != null) {
            for (ExtraProcessor extraProcessor : list2) {
                extraProcessor.processExtra(obj, str, parseObject);
            }
        }
    }

    public Object createInstance(Map<String, Object> map, ParserConfig parserConfig) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (this.beanInfo.creatorConstructor == null) {
            Object createInstance = createInstance((DefaultJSONParser) null, this.clazz);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                FieldDeserializer fieldDeserializer = getFieldDeserializer(entry.getKey());
                if (fieldDeserializer != null) {
                    Object value = entry.getValue();
                    Method method = fieldDeserializer.fieldInfo.method;
                    if (method != null) {
                        method.invoke(createInstance, TypeUtils.cast(value, method.getGenericParameterTypes()[0], parserConfig));
                    } else {
                        fieldDeserializer.fieldInfo.field.set(createInstance, TypeUtils.cast(value, fieldDeserializer.fieldInfo.fieldType, parserConfig));
                    }
                }
            }
            return createInstance;
        }
        FieldInfo[] fieldInfoArr = this.beanInfo.fields;
        int length = fieldInfoArr.length;
        Object[] objArr = new Object[length];
        for (int i = 0; i < length; i++) {
            FieldInfo fieldInfo = fieldInfoArr[i];
            Object obj = map.get(fieldInfo.name);
            if (obj == null) {
                obj = TypeUtils.defaultValue(fieldInfo.fieldClass);
            }
            objArr[i] = obj;
        }
        if (this.beanInfo.creatorConstructor != null) {
            try {
                return this.beanInfo.creatorConstructor.newInstance(objArr);
            } catch (Exception e) {
                throw new JSONException("create instance error, " + this.beanInfo.creatorConstructor.toGenericString(), e);
            }
        }
        return null;
    }

    protected JavaBeanDeserializer getSeeAlso(ParserConfig parserConfig, JavaBeanInfo javaBeanInfo, String str) {
        if (javaBeanInfo.jsonType == null) {
            return null;
        }
        for (Class<?> cls : javaBeanInfo.jsonType.seeAlso()) {
            ObjectDeserializer deserializer = parserConfig.getDeserializer(cls);
            if (deserializer instanceof JavaBeanDeserializer) {
                JavaBeanDeserializer javaBeanDeserializer = (JavaBeanDeserializer) deserializer;
                JavaBeanInfo javaBeanInfo2 = javaBeanDeserializer.beanInfo;
                if (javaBeanInfo2.typeName.equals(str)) {
                    return javaBeanDeserializer;
                }
                JavaBeanDeserializer seeAlso = getSeeAlso(parserConfig, javaBeanInfo2, str);
                if (seeAlso != null) {
                    return seeAlso;
                }
            }
        }
        return null;
    }
}
