package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class JavaBeanSerializer implements ObjectSerializer {
    protected int features;
    private final FieldSerializer[] getters;
    private final FieldSerializer[] sortedGetters;
    protected final String typeKey;
    protected final String typeName;
    private static final char[] true_chars = {'t', 'r', 'u', 'e'};
    private static final char[] false_chars = {'f', 'a', 'l', 's', 'e'};

    public JavaBeanSerializer(Class<?> cls) {
        this(cls, (PropertyNamingStrategy) null);
    }

    public JavaBeanSerializer(Class<?> cls, PropertyNamingStrategy propertyNamingStrategy) {
        this(cls, cls.getModifiers(), null, false, true, true, true, propertyNamingStrategy);
    }

    public JavaBeanSerializer(Class<?> cls, String... strArr) {
        this(cls, cls.getModifiers(), map(strArr), false, true, true, true, null);
    }

    private static Map<String, String> map(String... strArr) {
        HashMap hashMap = new HashMap();
        for (String str : strArr) {
            hashMap.put(str, str);
        }
        return hashMap;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00a7 A[LOOP:2: B:35:0x00a1->B:37:0x00a7, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x011f  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0122  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public JavaBeanSerializer(Class<?> cls, int i, Map<String, String> map, boolean z, boolean z2, boolean z3, boolean z4, PropertyNamingStrategy propertyNamingStrategy) {
        String str;
        String str2;
        FieldSerializer[] fieldSerializerArr;
        String[] orders;
        FieldSerializer[] fieldSerializerArr2;
        this.features = 0;
        JSONType jSONType = z2 ? (JSONType) cls.getAnnotation(JSONType.class) : null;
        if (jSONType != null) {
            this.features = SerializerFeature.of(jSONType.serialzeFeatures());
            str = jSONType.typeName();
            if (str.length() != 0) {
                str2 = null;
                for (Class<? super Object> superclass = cls.getSuperclass(); superclass != null && superclass != Object.class; superclass = superclass.getSuperclass()) {
                    JSONType jSONType2 = (JSONType) superclass.getAnnotation(JSONType.class);
                    if (jSONType2 == null) {
                        break;
                    }
                    str2 = jSONType2.typeKey();
                    if (str2.length() != 0) {
                        break;
                    }
                }
                for (Class<?> cls2 : cls.getInterfaces()) {
                    JSONType jSONType3 = (JSONType) cls2.getAnnotation(JSONType.class);
                    if (jSONType3 != null) {
                        str2 = jSONType3.typeKey();
                        if (str2.length() != 0) {
                            break;
                        }
                    }
                }
                if (str2 != null && str2.length() == 0) {
                    str2 = null;
                }
                this.typeName = str;
                this.typeKey = str2;
                List<FieldInfo> computeGetters = TypeUtils.computeGetters(cls, i, z, jSONType, map, false, z3, z4, propertyNamingStrategy);
                ArrayList arrayList = new ArrayList();
                for (FieldInfo fieldInfo : computeGetters) {
                    arrayList.add(new FieldSerializer(fieldInfo));
                }
                fieldSerializerArr = (FieldSerializer[]) arrayList.toArray(new FieldSerializer[arrayList.size()]);
                this.getters = fieldSerializerArr;
                orders = jSONType != null ? jSONType.orders() : null;
                if (orders == null && orders.length != 0) {
                    List<FieldInfo> computeGetters2 = TypeUtils.computeGetters(cls, i, z, jSONType, map, true, z3, z4, propertyNamingStrategy);
                    ArrayList arrayList2 = new ArrayList();
                    for (FieldInfo fieldInfo2 : computeGetters2) {
                        arrayList2.add(new FieldSerializer(fieldInfo2));
                    }
                    this.sortedGetters = (FieldSerializer[]) arrayList2.toArray(new FieldSerializer[arrayList2.size()]);
                    return;
                }
                fieldSerializerArr2 = new FieldSerializer[fieldSerializerArr.length];
                System.arraycopy(fieldSerializerArr, 0, fieldSerializerArr2, 0, fieldSerializerArr.length);
                Arrays.sort(fieldSerializerArr2);
                if (!Arrays.equals(fieldSerializerArr2, fieldSerializerArr)) {
                    this.sortedGetters = fieldSerializerArr;
                    return;
                } else {
                    this.sortedGetters = fieldSerializerArr2;
                    return;
                }
            }
        }
        str = null;
        str2 = null;
        this.typeName = str;
        this.typeKey = str2;
        List<FieldInfo> computeGetters3 = TypeUtils.computeGetters(cls, i, z, jSONType, map, false, z3, z4, propertyNamingStrategy);
        ArrayList arrayList3 = new ArrayList();
        while (r4.hasNext()) {
        }
        fieldSerializerArr = (FieldSerializer[]) arrayList3.toArray(new FieldSerializer[arrayList3.size()]);
        this.getters = fieldSerializerArr;
        if (jSONType != null) {
        }
        if (orders == null) {
        }
        fieldSerializerArr2 = new FieldSerializer[fieldSerializerArr.length];
        System.arraycopy(fieldSerializerArr, 0, fieldSerializerArr2, 0, fieldSerializerArr.length);
        Arrays.sort(fieldSerializerArr2);
        if (!Arrays.equals(fieldSerializerArr2, fieldSerializerArr)) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x0146 A[Catch: all -> 0x00a0, Exception -> 0x00a5, TRY_ENTER, TRY_LEAVE, TryCatch #8 {Exception -> 0x00a5, all -> 0x00a0, blocks: (B:41:0x0093, B:43:0x0097, B:44:0x009b, B:52:0x00b5, B:54:0x00be, B:58:0x00cd, B:61:0x00d8, B:63:0x00e1, B:65:0x00e5, B:71:0x00f1, B:73:0x00f7, B:77:0x0100, B:79:0x0107, B:80:0x010f, B:89:0x0121, B:90:0x0127, B:92:0x012d, B:100:0x0146, B:76:0x00fc), top: B:406:0x0093 }] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x015b  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x015d  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0167  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0169  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0268  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x0427 A[Catch: all -> 0x04f2, Exception -> 0x04f8, TryCatch #7 {Exception -> 0x04f8, all -> 0x04f2, blocks: (B:117:0x017c, B:119:0x0197, B:121:0x019b, B:124:0x01a0, B:126:0x01a4, B:130:0x01ad, B:131:0x01b1, B:133:0x01b7, B:139:0x01d0, B:141:0x01d7, B:143:0x01db, B:156:0x0227, B:158:0x022b, B:160:0x0232, B:162:0x0236, B:163:0x023b, B:165:0x023f, B:166:0x0244, B:167:0x0248, B:169:0x024e, B:179:0x026e, B:181:0x0272, B:183:0x027a, B:185:0x027e, B:186:0x0283, B:188:0x0287, B:189:0x028c, B:190:0x0293, B:192:0x0299, B:197:0x02b3, B:199:0x02b7, B:201:0x02be, B:203:0x02c2, B:204:0x02c7, B:206:0x02cb, B:207:0x02d0, B:208:0x02d7, B:210:0x02dd, B:216:0x02fa, B:218:0x02fe, B:225:0x0312, B:227:0x0316, B:229:0x031a, B:231:0x031e, B:233:0x0322, B:235:0x0326, B:242:0x0338, B:244:0x033c, B:246:0x0340, B:237:0x032a, B:239:0x032e, B:250:0x0352, B:252:0x035b, B:254:0x035f, B:255:0x0363, B:256:0x0367, B:258:0x037c, B:262:0x0388, B:263:0x038c, B:267:0x0396, B:268:0x0399, B:271:0x03a1, B:273:0x03ac, B:275:0x03b0, B:277:0x03b5, B:281:0x03d5, B:282:0x03df, B:285:0x03e6, B:289:0x03f0, B:294:0x03fc, B:296:0x0402, B:298:0x0406, B:299:0x0408, B:301:0x0410, B:303:0x0414, B:304:0x0418, B:307:0x0427, B:308:0x0431, B:309:0x0434, B:311:0x0438, B:312:0x0441, B:315:0x0447, B:316:0x0452, B:321:0x0465, B:323:0x046e, B:326:0x0478, B:327:0x047d, B:328:0x0484, B:330:0x0488, B:331:0x048d, B:332:0x0494, B:335:0x049a, B:337:0x04a3, B:342:0x04b7, B:343:0x04bc, B:344:0x04c1, B:345:0x04cc, B:346:0x04d1, B:347:0x04d6, B:144:0x01eb, B:146:0x01ef, B:147:0x01fb, B:149:0x01ff, B:150:0x020f, B:151:0x0216, B:360:0x050d, B:361:0x0513, B:363:0x0519, B:368:0x0529, B:370:0x0532, B:373:0x0541, B:375:0x0545, B:376:0x0549), top: B:408:0x017c }] */
    /* JADX WARN: Removed duplicated region for block: B:357:0x0507  */
    /* JADX WARN: Removed duplicated region for block: B:368:0x0529 A[Catch: all -> 0x04f2, Exception -> 0x04f8, TRY_ENTER, TryCatch #7 {Exception -> 0x04f8, all -> 0x04f2, blocks: (B:117:0x017c, B:119:0x0197, B:121:0x019b, B:124:0x01a0, B:126:0x01a4, B:130:0x01ad, B:131:0x01b1, B:133:0x01b7, B:139:0x01d0, B:141:0x01d7, B:143:0x01db, B:156:0x0227, B:158:0x022b, B:160:0x0232, B:162:0x0236, B:163:0x023b, B:165:0x023f, B:166:0x0244, B:167:0x0248, B:169:0x024e, B:179:0x026e, B:181:0x0272, B:183:0x027a, B:185:0x027e, B:186:0x0283, B:188:0x0287, B:189:0x028c, B:190:0x0293, B:192:0x0299, B:197:0x02b3, B:199:0x02b7, B:201:0x02be, B:203:0x02c2, B:204:0x02c7, B:206:0x02cb, B:207:0x02d0, B:208:0x02d7, B:210:0x02dd, B:216:0x02fa, B:218:0x02fe, B:225:0x0312, B:227:0x0316, B:229:0x031a, B:231:0x031e, B:233:0x0322, B:235:0x0326, B:242:0x0338, B:244:0x033c, B:246:0x0340, B:237:0x032a, B:239:0x032e, B:250:0x0352, B:252:0x035b, B:254:0x035f, B:255:0x0363, B:256:0x0367, B:258:0x037c, B:262:0x0388, B:263:0x038c, B:267:0x0396, B:268:0x0399, B:271:0x03a1, B:273:0x03ac, B:275:0x03b0, B:277:0x03b5, B:281:0x03d5, B:282:0x03df, B:285:0x03e6, B:289:0x03f0, B:294:0x03fc, B:296:0x0402, B:298:0x0406, B:299:0x0408, B:301:0x0410, B:303:0x0414, B:304:0x0418, B:307:0x0427, B:308:0x0431, B:309:0x0434, B:311:0x0438, B:312:0x0441, B:315:0x0447, B:316:0x0452, B:321:0x0465, B:323:0x046e, B:326:0x0478, B:327:0x047d, B:328:0x0484, B:330:0x0488, B:331:0x048d, B:332:0x0494, B:335:0x049a, B:337:0x04a3, B:342:0x04b7, B:343:0x04bc, B:344:0x04c1, B:345:0x04cc, B:346:0x04d1, B:347:0x04d6, B:144:0x01eb, B:146:0x01ef, B:147:0x01fb, B:149:0x01ff, B:150:0x020f, B:151:0x0216, B:360:0x050d, B:361:0x0513, B:363:0x0519, B:368:0x0529, B:370:0x0532, B:373:0x0541, B:375:0x0545, B:376:0x0549), top: B:408:0x017c }] */
    /* JADX WARN: Removed duplicated region for block: B:373:0x0541 A[Catch: all -> 0x04f2, Exception -> 0x04f8, TRY_ENTER, TryCatch #7 {Exception -> 0x04f8, all -> 0x04f2, blocks: (B:117:0x017c, B:119:0x0197, B:121:0x019b, B:124:0x01a0, B:126:0x01a4, B:130:0x01ad, B:131:0x01b1, B:133:0x01b7, B:139:0x01d0, B:141:0x01d7, B:143:0x01db, B:156:0x0227, B:158:0x022b, B:160:0x0232, B:162:0x0236, B:163:0x023b, B:165:0x023f, B:166:0x0244, B:167:0x0248, B:169:0x024e, B:179:0x026e, B:181:0x0272, B:183:0x027a, B:185:0x027e, B:186:0x0283, B:188:0x0287, B:189:0x028c, B:190:0x0293, B:192:0x0299, B:197:0x02b3, B:199:0x02b7, B:201:0x02be, B:203:0x02c2, B:204:0x02c7, B:206:0x02cb, B:207:0x02d0, B:208:0x02d7, B:210:0x02dd, B:216:0x02fa, B:218:0x02fe, B:225:0x0312, B:227:0x0316, B:229:0x031a, B:231:0x031e, B:233:0x0322, B:235:0x0326, B:242:0x0338, B:244:0x033c, B:246:0x0340, B:237:0x032a, B:239:0x032e, B:250:0x0352, B:252:0x035b, B:254:0x035f, B:255:0x0363, B:256:0x0367, B:258:0x037c, B:262:0x0388, B:263:0x038c, B:267:0x0396, B:268:0x0399, B:271:0x03a1, B:273:0x03ac, B:275:0x03b0, B:277:0x03b5, B:281:0x03d5, B:282:0x03df, B:285:0x03e6, B:289:0x03f0, B:294:0x03fc, B:296:0x0402, B:298:0x0406, B:299:0x0408, B:301:0x0410, B:303:0x0414, B:304:0x0418, B:307:0x0427, B:308:0x0431, B:309:0x0434, B:311:0x0438, B:312:0x0441, B:315:0x0447, B:316:0x0452, B:321:0x0465, B:323:0x046e, B:326:0x0478, B:327:0x047d, B:328:0x0484, B:330:0x0488, B:331:0x048d, B:332:0x0494, B:335:0x049a, B:337:0x04a3, B:342:0x04b7, B:343:0x04bc, B:344:0x04c1, B:345:0x04cc, B:346:0x04d1, B:347:0x04d6, B:144:0x01eb, B:146:0x01ef, B:147:0x01fb, B:149:0x01ff, B:150:0x020f, B:151:0x0216, B:360:0x050d, B:361:0x0513, B:363:0x0519, B:368:0x0529, B:370:0x0532, B:373:0x0541, B:375:0x0545, B:376:0x0549), top: B:408:0x017c }] */
    /* JADX WARN: Removed duplicated region for block: B:402:0x0571 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:408:0x017c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0119  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x011c  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0121 A[Catch: all -> 0x00a0, Exception -> 0x00a5, TRY_ENTER, TryCatch #8 {Exception -> 0x00a5, all -> 0x00a0, blocks: (B:41:0x0093, B:43:0x0097, B:44:0x009b, B:52:0x00b5, B:54:0x00be, B:58:0x00cd, B:61:0x00d8, B:63:0x00e1, B:65:0x00e5, B:71:0x00f1, B:73:0x00f7, B:77:0x0100, B:79:0x0107, B:80:0x010f, B:89:0x0121, B:90:0x0127, B:92:0x012d, B:100:0x0146, B:76:0x00fc), top: B:406:0x0093 }] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x013a  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x013c  */
    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) throws IOException {
        FieldSerializer[] fieldSerializerArr;
        SerialContext serialContext;
        Exception exc;
        Throwable th;
        boolean z;
        boolean z2;
        boolean z3;
        int i;
        FieldSerializer[] fieldSerializerArr2;
        int i2;
        int i3;
        String str;
        boolean z4;
        long j;
        boolean z5;
        boolean z6;
        int i4;
        boolean z7;
        boolean z8;
        List<PropertyPreFilter> list;
        String str2;
        List<ValueFilter> list2;
        Object obj3;
        List<NameFilter> list3;
        List<PropertyFilter> list4;
        boolean z9;
        int i5;
        Object valueOf;
        JavaBeanSerializer javaBeanSerializer = this;
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj == null) {
            serializeWriter.writeNull();
        } else if ((jSONSerializer.context == null || (jSONSerializer.context.features & SerializerFeature.DisableCircularReferenceDetect.mask) == 0) && jSONSerializer.references != null && jSONSerializer.references.containsKey(obj)) {
            jSONSerializer.writeReference(obj);
        } else {
            if ((serializeWriter.features & SerializerFeature.SortField.mask) != 0) {
                fieldSerializerArr = javaBeanSerializer.sortedGetters;
            } else {
                fieldSerializerArr = javaBeanSerializer.getters;
            }
            SerialContext serialContext2 = jSONSerializer.context;
            if ((serializeWriter.features & SerializerFeature.DisableCircularReferenceDetect.mask) == 0) {
                jSONSerializer.context = new SerialContext(serialContext2, obj, obj2, javaBeanSerializer.features);
                if (jSONSerializer.references == null) {
                    jSONSerializer.references = new IdentityHashMap<>();
                }
                jSONSerializer.references.put(obj, jSONSerializer.context);
            }
            boolean z10 = ((javaBeanSerializer.features & SerializerFeature.BeanToArray.mask) == 0 && (serializeWriter.features & SerializerFeature.BeanToArray.mask) == 0) ? false : true;
            char c = z10 ? '[' : '{';
            char c2 = z10 ? ']' : '}';
            try {
                int i6 = serializeWriter.count + 1;
                if (i6 > serializeWriter.buf.length) {
                    try {
                        if (serializeWriter.writer == null) {
                            serializeWriter.expandCapacity(i6);
                        } else {
                            serializeWriter.flush();
                            i6 = 1;
                        }
                    } catch (Exception e) {
                        exc = e;
                        serialContext = serialContext2;
                        String str3 = "write javaBean error, fastjson version 1.1.67";
                        if (obj2 != null) {
                        }
                        throw new JSONException(str3, exc);
                    } catch (Throwable th2) {
                        th = th2;
                        serialContext = serialContext2;
                        jSONSerializer.context = serialContext;
                        throw th;
                    }
                }
                serializeWriter.buf[serializeWriter.count] = c;
                serializeWriter.count = i6;
                if (fieldSerializerArr.length > 0 && (serializeWriter.features & SerializerFeature.PrettyFormat.mask) != 0) {
                    jSONSerializer.incrementIndent();
                    jSONSerializer.println();
                }
                if ((javaBeanSerializer.features & SerializerFeature.WriteClassName.mask) == 0 && ((serializeWriter.features & SerializerFeature.WriteClassName.mask) == 0 || (type == null && (serializeWriter.features & SerializerFeature.NotWriteRootClassName.mask) != 0 && (jSONSerializer.context == null || jSONSerializer.context.parent == null)))) {
                    z = false;
                    if (z || obj.getClass() == type) {
                        z2 = false;
                    } else {
                        String str4 = javaBeanSerializer.typeKey;
                        if (str4 == null) {
                            str4 = jSONSerializer.config.typeKey;
                        }
                        serializeWriter.writeFieldName(str4, false);
                        String str5 = javaBeanSerializer.typeName;
                        if (str5 == null) {
                            str5 = obj.getClass().getName();
                        }
                        jSONSerializer.write(str5);
                        z2 = true;
                    }
                    char c3 = !z2 ? ',' : (char) 0;
                    if (jSONSerializer.beforeFilters != null) {
                        for (BeforeFilter beforeFilter : jSONSerializer.beforeFilters) {
                            c3 = beforeFilter.writeBefore(jSONSerializer, obj, c3);
                        }
                    }
                    boolean z11 = c3 != ',';
                    if ((serializeWriter.features & SerializerFeature.QuoteFieldNames.mask) != 0) {
                        if ((serializeWriter.features & SerializerFeature.UseSingleQuotes.mask) == 0) {
                            z3 = true;
                            boolean z12 = (serializeWriter.features & SerializerFeature.UseSingleQuotes.mask) != 0;
                            boolean z13 = (SerializerFeature.NotWriteDefaultValue.mask & serializeWriter.features) != 0;
                            List<PropertyFilter> list5 = jSONSerializer.propertyFilters;
                            List<NameFilter> list6 = jSONSerializer.nameFilters;
                            boolean z14 = z11;
                            List<ValueFilter> list7 = jSONSerializer.valueFilters;
                            List<PropertyPreFilter> list8 = jSONSerializer.propertyPreFilters;
                            char c4 = c2;
                            i = 0;
                            while (i < fieldSerializerArr.length) {
                                try {
                                    try {
                                        FieldSerializer fieldSerializer = fieldSerializerArr[i];
                                        FieldSerializer[] fieldSerializerArr3 = fieldSerializerArr;
                                        FieldInfo fieldInfo = fieldSerializer.fieldInfo;
                                        int i7 = i;
                                        Class<?> cls = fieldInfo.fieldClass;
                                        boolean z15 = z12;
                                        String str6 = fieldInfo.name;
                                        boolean z16 = z3;
                                        boolean z17 = z13;
                                        if (((SerializerFeature.SkipTransientField.mask & serializeWriter.features) == 0 || fieldInfo.field == null || !fieldInfo.fieldTransient) && ((str = javaBeanSerializer.typeKey) == null || !str.equals(str6))) {
                                            if (list8 != null) {
                                                for (PropertyPreFilter propertyPreFilter : list8) {
                                                    if (!propertyPreFilter.apply(jSONSerializer, obj, str6)) {
                                                        z4 = false;
                                                        break;
                                                    }
                                                }
                                            }
                                            z4 = true;
                                            if (z4) {
                                                Object obj4 = null;
                                                if (fieldInfo.fieldAccess) {
                                                    if (cls == Integer.TYPE) {
                                                        j = 0;
                                                        z6 = false;
                                                        z7 = false;
                                                        i4 = fieldInfo.field.getInt(obj);
                                                        z5 = true;
                                                    } else if (cls == Long.TYPE) {
                                                        j = fieldInfo.field.getLong(obj);
                                                        z5 = true;
                                                        z6 = false;
                                                        i4 = 0;
                                                        z7 = false;
                                                    } else if (cls == Boolean.TYPE) {
                                                        z7 = fieldInfo.field.getBoolean(obj);
                                                        j = 0;
                                                        z5 = true;
                                                        z6 = false;
                                                        i4 = 0;
                                                    } else {
                                                        obj4 = fieldInfo.field.get(obj);
                                                    }
                                                    if (list5 != null) {
                                                        if (z5) {
                                                            if (cls == Integer.TYPE) {
                                                                obj4 = Integer.valueOf(i4);
                                                            } else if (cls == Long.TYPE) {
                                                                obj4 = Long.valueOf(j);
                                                            } else if (cls == Boolean.TYPE) {
                                                                obj4 = Boolean.valueOf(z7);
                                                            }
                                                            z6 = true;
                                                        }
                                                        Iterator<PropertyFilter> it = list5.iterator();
                                                        while (it.hasNext()) {
                                                            Iterator<PropertyFilter> it2 = it;
                                                            if (!it.next().apply(obj, str6, obj4)) {
                                                                z8 = false;
                                                                break;
                                                            }
                                                            it = it2;
                                                        }
                                                    }
                                                    z8 = true;
                                                    if (!z8) {
                                                        if (list6 != null) {
                                                            if (z5 && !z6) {
                                                                if (cls == Integer.TYPE) {
                                                                    valueOf = Integer.valueOf(i4);
                                                                } else if (cls == Long.TYPE) {
                                                                    valueOf = Long.valueOf(j);
                                                                } else if (cls == Boolean.TYPE) {
                                                                    valueOf = Boolean.valueOf(z7);
                                                                }
                                                                obj4 = valueOf;
                                                                z6 = true;
                                                            }
                                                            list = list8;
                                                            str2 = str6;
                                                            for (Iterator<NameFilter> it3 = list6.iterator(); it3.hasNext(); it3 = it3) {
                                                                str2 = it3.next().process(obj, str2, obj4);
                                                            }
                                                        } else {
                                                            list = list8;
                                                            str2 = str6;
                                                        }
                                                        if (list7 != null) {
                                                            if (z5 && !z6) {
                                                                if (cls == Integer.TYPE) {
                                                                    obj4 = Integer.valueOf(i4);
                                                                } else if (cls == Long.TYPE) {
                                                                    obj4 = Long.valueOf(j);
                                                                } else if (cls == Boolean.TYPE) {
                                                                    obj4 = Boolean.valueOf(z7);
                                                                }
                                                                z6 = true;
                                                            }
                                                            list2 = list7;
                                                            Object obj5 = obj4;
                                                            for (Iterator<ValueFilter> it4 = list7.iterator(); it4.hasNext(); it4 = it4) {
                                                                obj5 = it4.next().process(obj, str6, obj5);
                                                            }
                                                            obj3 = obj4;
                                                            obj4 = obj5;
                                                        } else {
                                                            list2 = list7;
                                                            obj3 = obj4;
                                                        }
                                                        if (!z6 || obj4 != null || z10 || fieldSerializer.writeNull) {
                                                            list3 = list6;
                                                        } else {
                                                            list3 = list6;
                                                            if ((serializeWriter.features & SerializerFeature.WriteMapNullValue.mask) == 0) {
                                                                list4 = list5;
                                                                i = i7 + 1;
                                                                javaBeanSerializer = this;
                                                                fieldSerializerArr = fieldSerializerArr3;
                                                                z12 = z15;
                                                                z3 = z16;
                                                                z13 = z17;
                                                                list8 = list;
                                                                list7 = list2;
                                                                list6 = list3;
                                                                list5 = list4;
                                                            }
                                                        }
                                                        if (!z6 || obj4 == null || !z17 || (((cls != Byte.TYPE && cls != Short.TYPE && cls != Integer.TYPE && cls != Long.TYPE && cls != Float.TYPE && cls != Double.TYPE) || !(obj4 instanceof Number) || ((Number) obj4).byteValue() != 0) && (cls != Boolean.TYPE || !(obj4 instanceof Boolean) || ((Boolean) obj4).booleanValue()))) {
                                                            if (z14) {
                                                                int i8 = serializeWriter.count + 1;
                                                                if (i8 > serializeWriter.buf.length) {
                                                                    if (serializeWriter.writer == null) {
                                                                        serializeWriter.expandCapacity(i8);
                                                                    } else {
                                                                        serializeWriter.flush();
                                                                        i8 = 1;
                                                                    }
                                                                }
                                                                list4 = list5;
                                                                serializeWriter.buf[serializeWriter.count] = ',';
                                                                serializeWriter.count = i8;
                                                                if ((serializeWriter.features & SerializerFeature.PrettyFormat.mask) != 0) {
                                                                    jSONSerializer.println();
                                                                }
                                                            } else {
                                                                list4 = list5;
                                                            }
                                                            if (str2 != str6) {
                                                                if (!z10) {
                                                                    serializeWriter.writeFieldName(str2, true);
                                                                }
                                                                jSONSerializer.write(obj4);
                                                            } else if (obj3 != obj4) {
                                                                if (!z10) {
                                                                    fieldSerializer.writePrefix(jSONSerializer);
                                                                }
                                                                jSONSerializer.write(obj4);
                                                            } else {
                                                                if (!z10) {
                                                                    if (z16) {
                                                                        char[] cArr = fieldSerializer.name_chars;
                                                                        int length = cArr.length;
                                                                        int i9 = serializeWriter.count + length;
                                                                        if (i9 > serializeWriter.buf.length) {
                                                                            if (serializeWriter.writer == null) {
                                                                                serializeWriter.expandCapacity(i9);
                                                                            } else {
                                                                                int i10 = 0;
                                                                                do {
                                                                                    int length2 = serializeWriter.buf.length - serializeWriter.count;
                                                                                    System.arraycopy(cArr, i10, serializeWriter.buf, serializeWriter.count, length2);
                                                                                    serializeWriter.count = serializeWriter.buf.length;
                                                                                    serializeWriter.flush();
                                                                                    length -= length2;
                                                                                    i10 += length2;
                                                                                } while (length > serializeWriter.buf.length);
                                                                                i5 = i10;
                                                                                i9 = length;
                                                                                System.arraycopy(cArr, i5, serializeWriter.buf, serializeWriter.count, length);
                                                                                serializeWriter.count = i9;
                                                                            }
                                                                        }
                                                                        i5 = 0;
                                                                        System.arraycopy(cArr, i5, serializeWriter.buf, serializeWriter.count, length);
                                                                        serializeWriter.count = i9;
                                                                    } else {
                                                                        fieldSerializer.writePrefix(jSONSerializer);
                                                                    }
                                                                }
                                                                if (!z5 || z6) {
                                                                    if (!z10) {
                                                                        if (cls == String.class) {
                                                                            if (obj4 == null) {
                                                                                if ((serializeWriter.features & SerializerFeature.WriteNullStringAsEmpty.mask) == 0 && (fieldSerializer.features & SerializerFeature.WriteNullStringAsEmpty.mask) == 0) {
                                                                                    serializeWriter.writeNull();
                                                                                }
                                                                                serializeWriter.writeString("");
                                                                            } else {
                                                                                String str7 = (String) obj4;
                                                                                if (z15) {
                                                                                    serializeWriter.writeStringWithSingleQuote(str7);
                                                                                } else {
                                                                                    serializeWriter.writeStringWithDoubleQuote(str7, (char) 0, true);
                                                                                }
                                                                            }
                                                                        } else if (!fieldInfo.isEnum) {
                                                                            fieldSerializer.writeValue(jSONSerializer, obj4);
                                                                        } else if (obj4 != null) {
                                                                            if ((serializeWriter.features & SerializerFeature.WriteEnumUsingToString.mask) != 0) {
                                                                                String str8 = ((Enum) obj4).toString();
                                                                                if ((serializeWriter.features & SerializerFeature.UseSingleQuotes.mask) != 0) {
                                                                                    serializeWriter.writeStringWithSingleQuote(str8);
                                                                                } else {
                                                                                    serializeWriter.writeStringWithDoubleQuote(str8, (char) 0, false);
                                                                                }
                                                                            } else {
                                                                                serializeWriter.writeInt(((Enum) obj4).ordinal());
                                                                            }
                                                                        } else {
                                                                            serializeWriter.writeNull();
                                                                        }
                                                                    } else {
                                                                        fieldSerializer.writeValue(jSONSerializer, obj4);
                                                                    }
                                                                    z14 = true;
                                                                    i = i7 + 1;
                                                                    javaBeanSerializer = this;
                                                                    fieldSerializerArr = fieldSerializerArr3;
                                                                    z12 = z15;
                                                                    z3 = z16;
                                                                    z13 = z17;
                                                                    list8 = list;
                                                                    list7 = list2;
                                                                    list6 = list3;
                                                                    list5 = list4;
                                                                } else if (cls == Integer.TYPE) {
                                                                    int i11 = i4;
                                                                    if (i11 == Integer.MIN_VALUE) {
                                                                        serializeWriter.write("-2147483648");
                                                                    } else {
                                                                        int i12 = 0;
                                                                        while ((i11 < 0 ? -i11 : i11) > SerializeWriter.sizeTable[i12]) {
                                                                            i12++;
                                                                        }
                                                                        int i13 = i12 + 1;
                                                                        if (i11 < 0) {
                                                                            i13++;
                                                                        }
                                                                        int i14 = serializeWriter.count + i13;
                                                                        if (i14 > serializeWriter.buf.length) {
                                                                            if (serializeWriter.writer == null) {
                                                                                serializeWriter.expandCapacity(i14);
                                                                            } else {
                                                                                char[] cArr2 = new char[i13];
                                                                                SerializeWriter.getChars(i11, i13, cArr2);
                                                                                serializeWriter.write(cArr2, 0, i13);
                                                                                z9 = true;
                                                                                if (!z9) {
                                                                                    SerializeWriter.getChars(i11, i14, serializeWriter.buf);
                                                                                    serializeWriter.count = i14;
                                                                                }
                                                                            }
                                                                        }
                                                                        z9 = false;
                                                                        if (!z9) {
                                                                        }
                                                                    }
                                                                } else if (cls == Long.TYPE) {
                                                                    jSONSerializer.out.writeLong(j);
                                                                } else if (cls == Boolean.TYPE) {
                                                                    if (z7) {
                                                                        SerializeWriter serializeWriter2 = jSONSerializer.out;
                                                                        char[] cArr3 = true_chars;
                                                                        serializeWriter2.write(cArr3, 0, cArr3.length);
                                                                    } else {
                                                                        SerializeWriter serializeWriter3 = jSONSerializer.out;
                                                                        char[] cArr4 = false_chars;
                                                                        serializeWriter3.write(cArr4, 0, cArr4.length);
                                                                    }
                                                                }
                                                            }
                                                            z14 = true;
                                                            i = i7 + 1;
                                                            javaBeanSerializer = this;
                                                            fieldSerializerArr = fieldSerializerArr3;
                                                            z12 = z15;
                                                            z3 = z16;
                                                            z13 = z17;
                                                            list8 = list;
                                                            list7 = list2;
                                                            list6 = list3;
                                                            list5 = list4;
                                                        }
                                                        list4 = list5;
                                                        i = i7 + 1;
                                                        javaBeanSerializer = this;
                                                        fieldSerializerArr = fieldSerializerArr3;
                                                        z12 = z15;
                                                        z3 = z16;
                                                        z13 = z17;
                                                        list8 = list;
                                                        list7 = list2;
                                                        list6 = list3;
                                                        list5 = list4;
                                                    }
                                                } else {
                                                    obj4 = fieldSerializer.getPropertyValue(obj);
                                                }
                                                j = 0;
                                                z5 = false;
                                                z6 = true;
                                                i4 = 0;
                                                z7 = false;
                                                if (list5 != null) {
                                                }
                                                z8 = true;
                                                if (!z8) {
                                                }
                                            }
                                        }
                                        list = list8;
                                        list2 = list7;
                                        list3 = list6;
                                        list4 = list5;
                                        i = i7 + 1;
                                        javaBeanSerializer = this;
                                        fieldSerializerArr = fieldSerializerArr3;
                                        z12 = z15;
                                        z3 = z16;
                                        z13 = z17;
                                        list8 = list;
                                        list7 = list2;
                                        list6 = list3;
                                        list5 = list4;
                                    } catch (Exception e2) {
                                        exc = e2;
                                        serialContext = serialContext2;
                                        String str32 = "write javaBean error, fastjson version 1.1.67";
                                        if (obj2 != null) {
                                        }
                                        throw new JSONException(str32, exc);
                                    } catch (Throwable th3) {
                                        th = th3;
                                        serialContext = serialContext2;
                                        jSONSerializer.context = serialContext;
                                        throw th;
                                    }
                                } catch (Exception e3) {
                                    e = e3;
                                    serialContext = serialContext2;
                                    exc = e;
                                    String str322 = "write javaBean error, fastjson version 1.1.67";
                                    if (obj2 != null) {
                                        try {
                                            str322 = "write javaBean error, fastjson version 1.1.67, fieldName : " + obj2;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            th = th;
                                            jSONSerializer.context = serialContext;
                                            throw th;
                                        }
                                    }
                                    throw new JSONException(str322, exc);
                                } catch (Throwable th5) {
                                    th = th5;
                                    serialContext = serialContext2;
                                    th = th;
                                    jSONSerializer.context = serialContext;
                                    throw th;
                                }
                            }
                            fieldSerializerArr2 = fieldSerializerArr;
                            if (jSONSerializer.afterFilters != null) {
                                char c5 = z14 ? ',' : (char) 0;
                                for (AfterFilter afterFilter : jSONSerializer.afterFilters) {
                                    c5 = afterFilter.writeAfter(jSONSerializer, obj, c5);
                                }
                            }
                            if (fieldSerializerArr2.length > 0 && (serializeWriter.features & SerializerFeature.PrettyFormat.mask) != 0) {
                                jSONSerializer.decrementIdent();
                                jSONSerializer.println();
                            }
                            i2 = serializeWriter.count + 1;
                            if (i2 > serializeWriter.buf.length) {
                                if (serializeWriter.writer == null) {
                                    serializeWriter.expandCapacity(i2);
                                } else {
                                    serializeWriter.flush();
                                    i3 = 1;
                                    serializeWriter.buf[serializeWriter.count] = c4;
                                    serializeWriter.count = i3;
                                    jSONSerializer.context = serialContext2;
                                }
                            }
                            i3 = i2;
                            serializeWriter.buf[serializeWriter.count] = c4;
                            serializeWriter.count = i3;
                            jSONSerializer.context = serialContext2;
                        }
                    }
                    z3 = false;
                    if ((serializeWriter.features & SerializerFeature.UseSingleQuotes.mask) != 0) {
                    }
                    if ((SerializerFeature.NotWriteDefaultValue.mask & serializeWriter.features) != 0) {
                    }
                    List<PropertyFilter> list52 = jSONSerializer.propertyFilters;
                    List<NameFilter> list62 = jSONSerializer.nameFilters;
                    boolean z142 = z11;
                    List<ValueFilter> list72 = jSONSerializer.valueFilters;
                    List<PropertyPreFilter> list82 = jSONSerializer.propertyPreFilters;
                    char c42 = c2;
                    i = 0;
                    while (i < fieldSerializerArr.length) {
                    }
                    fieldSerializerArr2 = fieldSerializerArr;
                    if (jSONSerializer.afterFilters != null) {
                    }
                    if (fieldSerializerArr2.length > 0) {
                        jSONSerializer.decrementIdent();
                        jSONSerializer.println();
                    }
                    i2 = serializeWriter.count + 1;
                    if (i2 > serializeWriter.buf.length) {
                    }
                    i3 = i2;
                    serializeWriter.buf[serializeWriter.count] = c42;
                    serializeWriter.count = i3;
                    jSONSerializer.context = serialContext2;
                }
                z = true;
                if (z) {
                }
                z2 = false;
                if (!z2) {
                }
                if (jSONSerializer.beforeFilters != null) {
                }
                if (c3 != ',') {
                }
                if ((serializeWriter.features & SerializerFeature.QuoteFieldNames.mask) != 0) {
                }
                z3 = false;
                if ((serializeWriter.features & SerializerFeature.UseSingleQuotes.mask) != 0) {
                }
                if ((SerializerFeature.NotWriteDefaultValue.mask & serializeWriter.features) != 0) {
                }
                List<PropertyFilter> list522 = jSONSerializer.propertyFilters;
                List<NameFilter> list622 = jSONSerializer.nameFilters;
                boolean z1422 = z11;
                List<ValueFilter> list722 = jSONSerializer.valueFilters;
                List<PropertyPreFilter> list822 = jSONSerializer.propertyPreFilters;
                char c422 = c2;
                i = 0;
                while (i < fieldSerializerArr.length) {
                }
                fieldSerializerArr2 = fieldSerializerArr;
                if (jSONSerializer.afterFilters != null) {
                }
                if (fieldSerializerArr2.length > 0) {
                }
                i2 = serializeWriter.count + 1;
                if (i2 > serializeWriter.buf.length) {
                }
                i3 = i2;
                serializeWriter.buf[serializeWriter.count] = c422;
                serializeWriter.count = i3;
                jSONSerializer.context = serialContext2;
            } catch (Exception e4) {
                e = e4;
                serialContext = serialContext2;
            } catch (Throwable th6) {
                th = th6;
                serialContext = serialContext2;
            }
        }
    }

    public Map<String, Object> getFieldValuesMap(Object obj) throws Exception {
        FieldSerializer[] fieldSerializerArr;
        LinkedHashMap linkedHashMap = new LinkedHashMap(this.sortedGetters.length);
        for (FieldSerializer fieldSerializer : this.sortedGetters) {
            linkedHashMap.put(fieldSerializer.fieldInfo.name, fieldSerializer.getPropertyValue(obj));
        }
        return linkedHashMap;
    }
}
