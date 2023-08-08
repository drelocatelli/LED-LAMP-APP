package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.TypeUtils;
import com.home.utils.Utils;
import java.lang.reflect.Type;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ThrowableDeserializer extends JavaBeanDeserializer {
    public ThrowableDeserializer(ParserConfig parserConfig, Class<?> cls) {
        super(parserConfig, cls, cls);
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x018c, code lost:
        if (r0.hasNext() == false) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x018e, code lost:
        r2 = (java.util.Map.Entry) r0.next();
        r2 = r2.getValue();
        r4 = r6.getFieldDeserializer((java.lang.String) r2.getKey());
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x01a2, code lost:
        if (r4 == null) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x01a4, code lost:
        r4.setValue(r3, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x01a8, code lost:
        return (T) r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x01a9, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x01b1, code lost:
        throw new com.alibaba.fastjson.JSONException("create instance error", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0032, code lost:
        if (java.lang.Throwable.class.isAssignableFrom(r2) != false) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00d5, code lost:
        if (r2 != null) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00d7, code lost:
        r3 = (T) new java.lang.Exception(r11, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00de, code lost:
        r3 = r2.getConstructors();
        r5 = r3.length;
        r8 = r6;
        r9 = r8;
        r13 = r9;
        r7 = 0;
        r8 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00e8, code lost:
        if (r7 >= r5) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00ea, code lost:
        r15 = r3[r7];
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00f1, code lost:
        if (r15.getParameterTypes().length != 0) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00f3, code lost:
        r13 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00fa, code lost:
        if (r15.getParameterTypes().length != 1) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0106, code lost:
        if (r15.getParameterTypes()[0] != java.lang.String.class) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0108, code lost:
        r9 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0110, code lost:
        if (r15.getParameterTypes().length != 2) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x011b, code lost:
        if (r15.getParameterTypes()[0] != java.lang.String.class) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0126, code lost:
        if (r15.getParameterTypes()[1] != java.lang.Throwable.class) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0128, code lost:
        r8 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0129, code lost:
        r7 = r7 + 1;
        r8 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x012d, code lost:
        if (r8 == 0) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x012f, code lost:
        r3 = (T) ((java.lang.Throwable) r8.newInstance(r11, r10));
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x013f, code lost:
        if (r9 == null) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0141, code lost:
        r3 = (java.lang.Throwable) r9.newInstance(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x014e, code lost:
        if (r13 == null) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0150, code lost:
        r3 = (java.lang.Throwable) r13.newInstance(new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x015a, code lost:
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x015b, code lost:
        if (r3 != null) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x015d, code lost:
        r3 = (T) new java.lang.Exception(r11, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0162, code lost:
        if (r12 == null) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0164, code lost:
        ((java.lang.Throwable) r3).setStackTrace(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0167, code lost:
        if (r4 == null) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0169, code lost:
        if (r2 == null) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x016d, code lost:
        if (r2 != r17.clazz) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x016f, code lost:
        r6 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0171, code lost:
        r0 = r18.config.getDeserializer(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0179, code lost:
        if ((r0 instanceof com.alibaba.fastjson.parser.JavaBeanDeserializer) == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x017b, code lost:
        r6 = (com.alibaba.fastjson.parser.JavaBeanDeserializer) r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x017f, code lost:
        r6 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0180, code lost:
        r0 = r4.entrySet().iterator();
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v5 */
    @Override // com.alibaba.fastjson.parser.JavaBeanDeserializer, com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        Class<?> cls;
        String str;
        String stringVal;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        Type type2 = null;
        if (jSONLexer.token == 8) {
            jSONLexer.nextToken();
            return null;
        }
        if (defaultJSONParser.resolveStatus == 2) {
            defaultJSONParser.resolveStatus = 0;
        } else if (jSONLexer.token != 12) {
            throw new JSONException("syntax error");
        }
        if (type != null && (type instanceof Class)) {
            cls = (Class) type;
        }
        cls = null;
        HashMap hashMap = null;
        Throwable th = null;
        Type type3 = null;
        StackTraceElement[] stackTraceElementArr = null;
        while (true) {
            String scanSymbol = jSONLexer.scanSymbol(defaultJSONParser.symbolTable);
            if (scanSymbol == null) {
                if (jSONLexer.token == 13) {
                    jSONLexer.nextToken(16);
                    str = type3;
                    break;
                } else if (jSONLexer.token == 16) {
                }
            }
            jSONLexer.nextTokenWithChar(':');
            if (JSON.DEFAULT_TYPE_KEY.equals(scanSymbol)) {
                if (jSONLexer.token == 4) {
                    cls = TypeUtils.loadClass(jSONLexer.stringVal(), defaultJSONParser.config.defaultClassLoader);
                    jSONLexer.nextToken(16);
                    type3 = type3;
                } else {
                    throw new JSONException("syntax error");
                }
            } else if (Utils.EXTRA_MESSAGE.equals(scanSymbol)) {
                if (jSONLexer.token == 8) {
                    stringVal = type2;
                } else if (jSONLexer.token == 4) {
                    stringVal = jSONLexer.stringVal();
                } else {
                    throw new JSONException("syntax error");
                }
                jSONLexer.nextToken();
                type3 = stringVal;
            } else if ("cause".equals(scanSymbol)) {
                th = (Throwable) deserialze(defaultJSONParser, type2, "cause");
                type3 = type3;
            } else if ("stackTrace".equals(scanSymbol)) {
                stackTraceElementArr = (StackTraceElement[]) defaultJSONParser.parseObject((Class<Object>) StackTraceElement[].class);
                type3 = type3;
            } else {
                if (hashMap == null) {
                    hashMap = new HashMap();
                }
                hashMap.put(scanSymbol, defaultJSONParser.parse());
                type3 = type3;
            }
            if (jSONLexer.token == 13) {
                jSONLexer.nextToken(16);
                str = type3;
                break;
            }
            type2 = null;
        }
    }
}
