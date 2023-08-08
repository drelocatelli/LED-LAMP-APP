package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.apache.http.message.TokenParser;

/* loaded from: classes.dex */
public class DefaultJSONParser implements Closeable {
    public static final int NONE = 0;
    public static final int NeedToResolve = 1;
    public static final int TypeNameRedirect = 2;
    public ParserConfig config;
    protected ParseContext contex;
    private ParseContext[] contextArray;
    private int contextArrayIndex;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    protected List<ExtraProcessor> extraProcessors;
    protected List<ExtraTypeProvider> extraTypeProviders;
    public FieldTypeResolver fieldTypeResolver;
    public final JSONLexer lexer;
    public int resolveStatus;
    private List<ResolveTask> resolveTaskList;
    public final SymbolTable symbolTable;

    public String getDateFomartPattern() {
        return this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (this.dateFormat == null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.dateFormatPattern, this.lexer.locale);
            this.dateFormat = simpleDateFormat;
            simpleDateFormat.setTimeZone(this.lexer.timeZone);
        }
        return this.dateFormat;
    }

    public void setDateFormat(String str) {
        this.dateFormatPattern = str;
        this.dateFormat = null;
    }

    public void setDateFomrat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DefaultJSONParser(String str) {
        this(str, ParserConfig.global, JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig) {
        this(new JSONLexer(str, JSON.DEFAULT_PARSER_FEATURE), parserConfig);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig, int i) {
        this(new JSONLexer(str, i), parserConfig);
    }

    public DefaultJSONParser(char[] cArr, int i, ParserConfig parserConfig, int i2) {
        this(new JSONLexer(cArr, i, i2), parserConfig);
    }

    public DefaultJSONParser(JSONLexer jSONLexer) {
        this(jSONLexer, ParserConfig.global);
    }

    public DefaultJSONParser(JSONLexer jSONLexer, ParserConfig parserConfig) {
        this.dateFormatPattern = JSON.DEFFAULT_DATE_FORMAT;
        this.contextArrayIndex = 0;
        this.resolveStatus = 0;
        this.extraTypeProviders = null;
        this.extraProcessors = null;
        this.fieldTypeResolver = null;
        this.lexer = jSONLexer;
        this.config = parserConfig;
        this.symbolTable = parserConfig.symbolTable;
        char c = jSONLexer.ch;
        char c2 = JSONLexer.EOI;
        if (c == '{') {
            int i = jSONLexer.bp + 1;
            jSONLexer.bp = i;
            jSONLexer.ch = i < jSONLexer.len ? jSONLexer.text.charAt(i) : c2;
            jSONLexer.token = 12;
        } else if (jSONLexer.ch == '[') {
            int i2 = jSONLexer.bp + 1;
            jSONLexer.bp = i2;
            jSONLexer.ch = i2 < jSONLexer.len ? jSONLexer.text.charAt(i2) : c2;
            jSONLexer.token = 14;
        } else {
            jSONLexer.nextToken();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:128:0x022d, code lost:
        r3.nextToken(16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0234, code lost:
        if (r3.token != 13) goto L109;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x0236, code lost:
        r3.nextToken(16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x0239, code lost:
        r2 = r18.config.getDeserializer(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x0241, code lost:
        if ((r2 instanceof com.alibaba.fastjson.parser.JavaBeanDeserializer) == false) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x0243, code lost:
        r2 = (com.alibaba.fastjson.parser.JavaBeanDeserializer) r2;
        r3 = r2.createInstance(r18, r7);
        r0 = r19.entrySet().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x0255, code lost:
        if (r0.hasNext() == false) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0257, code lost:
        r4 = (java.util.Map.Entry) r0.next();
        r5 = r4.getKey();
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0263, code lost:
        if ((r5 instanceof java.lang.String) == false) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0265, code lost:
        r5 = r2.getFieldDeserializer((java.lang.String) r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x026b, code lost:
        if (r5 == null) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x026d, code lost:
        r5.setValue(r3, r4.getValue());
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0275, code lost:
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0276, code lost:
        if (r3 != null) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x027a, code lost:
        if (r7 != java.lang.Cloneable.class) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x027c, code lost:
        r3 = new java.util.HashMap();
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x0288, code lost:
        if ("java.util.Collections$EmptyMap".equals(r6) == false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x028a, code lost:
        r3 = java.util.Collections.emptyMap();
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x028f, code lost:
        r3 = r7.newInstance();
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x0293, code lost:
        if (r13 != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x0295, code lost:
        r18.contex = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x0297, code lost:
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0298, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x02a0, code lost:
        throw new com.alibaba.fastjson.JSONException("create instance error", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x02a1, code lost:
        r18.resolveStatus = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x02a6, code lost:
        if (r18.contex == null) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x02aa, code lost:
        if ((r20 instanceof java.lang.Integer) != false) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x02ac, code lost:
        popContext();
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x02b3, code lost:
        if (r19.size() <= 0) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x02b5, code lost:
        r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r19, (java.lang.Class<java.lang.Object>) r7, r18.config);
        parseObject(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x02be, code lost:
        if (r13 != false) goto L119;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x02c0, code lost:
        r18.contex = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x02c2, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x02c3, code lost:
        r0 = r18.config.getDeserializer(r7).deserialze(r18, r7, r20);
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x02cd, code lost:
        if (r13 != false) goto L123;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x02cf, code lost:
        r18.contex = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x02d1, code lost:
        return r0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:118:0x01fc A[Catch: all -> 0x06af, TryCatch #2 {all -> 0x06af, blocks: (B:21:0x0063, B:24:0x006d, B:27:0x0076, B:31:0x0089, B:33:0x0093, B:36:0x009b, B:37:0x00b9, B:96:0x01c1, B:100:0x01d4, B:116:0x01f3, B:119:0x0200, B:122:0x0207, B:124:0x020f, B:126:0x0222, B:128:0x022d, B:130:0x0236, B:131:0x0239, B:133:0x0243, B:134:0x0251, B:136:0x0257, B:138:0x0265, B:140:0x026d, B:145:0x027c, B:146:0x0282, B:148:0x028a, B:149:0x028f, B:154:0x0299, B:155:0x02a0, B:156:0x02a1, B:158:0x02a8, B:160:0x02ac, B:161:0x02af, B:163:0x02b5, B:167:0x02c3, B:175:0x02da, B:177:0x02e2, B:179:0x02e9, B:181:0x02f8, B:183:0x0300, B:186:0x0305, B:188:0x0309, B:208:0x035b, B:210:0x035f, B:214:0x0369, B:215:0x0381, B:190:0x0310, B:192:0x0318, B:194:0x031c, B:195:0x031f, B:196:0x032b, B:199:0x0334, B:201:0x0338, B:202:0x033b, B:204:0x033f, B:205:0x0343, B:206:0x034f, B:216:0x0382, B:217:0x039e, B:220:0x03a3, B:226:0x03b4, B:228:0x03ba, B:230:0x03c6, B:231:0x03cc, B:233:0x03d1, B:333:0x0566, B:337:0x0570, B:340:0x0579, B:344:0x058c, B:343:0x0586, B:348:0x0598, B:352:0x05ab, B:354:0x05b4, B:358:0x05c7, B:375:0x060f, B:357:0x05c1, B:361:0x05d2, B:365:0x05e5, B:364:0x05df, B:368:0x05f0, B:372:0x0603, B:371:0x05fd, B:373:0x060a, B:351:0x05a5, B:379:0x0619, B:380:0x0631, B:234:0x03d5, B:241:0x03e5, B:245:0x03f4, B:249:0x040b, B:251:0x0414, B:256:0x0421, B:257:0x0424, B:259:0x042e, B:261:0x0435, B:263:0x0439, B:271:0x044c, B:272:0x0464, B:260:0x0432, B:248:0x0405, B:275:0x0469, B:279:0x047c, B:281:0x048d, B:285:0x04a1, B:287:0x04a7, B:290:0x04ad, B:292:0x04b7, B:294:0x04bf, B:298:0x04d1, B:301:0x04d9, B:302:0x04db, B:304:0x04e0, B:306:0x04e9, B:309:0x04f2, B:310:0x04f5, B:312:0x04fb, B:314:0x0502, B:321:0x050f, B:322:0x0527, B:307:0x04ed, B:282:0x0498, B:278:0x0476, B:325:0x052e, B:327:0x053a, B:330:0x054d, B:332:0x0559, B:381:0x0632, B:383:0x0643, B:384:0x0647, B:386:0x0650, B:391:0x065c, B:394:0x0664, B:395:0x067c, B:99:0x01ce, B:118:0x01fc, B:41:0x00c1, B:45:0x00d2, B:44:0x00cc, B:51:0x00e5, B:53:0x00ef, B:54:0x00f2, B:57:0x00f7, B:58:0x010d, B:68:0x0120, B:69:0x0126, B:71:0x012b, B:74:0x0138, B:75:0x013c, B:78:0x0142, B:79:0x015c, B:72:0x0130, B:80:0x015d, B:81:0x0177, B:87:0x0181, B:90:0x0190, B:91:0x0196, B:92:0x01b4, B:93:0x01b5, B:396:0x067d, B:397:0x0695, B:398:0x0696, B:399:0x06ae), top: B:409:0x0063, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:226:0x03b4 A[Catch: all -> 0x06af, TryCatch #2 {all -> 0x06af, blocks: (B:21:0x0063, B:24:0x006d, B:27:0x0076, B:31:0x0089, B:33:0x0093, B:36:0x009b, B:37:0x00b9, B:96:0x01c1, B:100:0x01d4, B:116:0x01f3, B:119:0x0200, B:122:0x0207, B:124:0x020f, B:126:0x0222, B:128:0x022d, B:130:0x0236, B:131:0x0239, B:133:0x0243, B:134:0x0251, B:136:0x0257, B:138:0x0265, B:140:0x026d, B:145:0x027c, B:146:0x0282, B:148:0x028a, B:149:0x028f, B:154:0x0299, B:155:0x02a0, B:156:0x02a1, B:158:0x02a8, B:160:0x02ac, B:161:0x02af, B:163:0x02b5, B:167:0x02c3, B:175:0x02da, B:177:0x02e2, B:179:0x02e9, B:181:0x02f8, B:183:0x0300, B:186:0x0305, B:188:0x0309, B:208:0x035b, B:210:0x035f, B:214:0x0369, B:215:0x0381, B:190:0x0310, B:192:0x0318, B:194:0x031c, B:195:0x031f, B:196:0x032b, B:199:0x0334, B:201:0x0338, B:202:0x033b, B:204:0x033f, B:205:0x0343, B:206:0x034f, B:216:0x0382, B:217:0x039e, B:220:0x03a3, B:226:0x03b4, B:228:0x03ba, B:230:0x03c6, B:231:0x03cc, B:233:0x03d1, B:333:0x0566, B:337:0x0570, B:340:0x0579, B:344:0x058c, B:343:0x0586, B:348:0x0598, B:352:0x05ab, B:354:0x05b4, B:358:0x05c7, B:375:0x060f, B:357:0x05c1, B:361:0x05d2, B:365:0x05e5, B:364:0x05df, B:368:0x05f0, B:372:0x0603, B:371:0x05fd, B:373:0x060a, B:351:0x05a5, B:379:0x0619, B:380:0x0631, B:234:0x03d5, B:241:0x03e5, B:245:0x03f4, B:249:0x040b, B:251:0x0414, B:256:0x0421, B:257:0x0424, B:259:0x042e, B:261:0x0435, B:263:0x0439, B:271:0x044c, B:272:0x0464, B:260:0x0432, B:248:0x0405, B:275:0x0469, B:279:0x047c, B:281:0x048d, B:285:0x04a1, B:287:0x04a7, B:290:0x04ad, B:292:0x04b7, B:294:0x04bf, B:298:0x04d1, B:301:0x04d9, B:302:0x04db, B:304:0x04e0, B:306:0x04e9, B:309:0x04f2, B:310:0x04f5, B:312:0x04fb, B:314:0x0502, B:321:0x050f, B:322:0x0527, B:307:0x04ed, B:282:0x0498, B:278:0x0476, B:325:0x052e, B:327:0x053a, B:330:0x054d, B:332:0x0559, B:381:0x0632, B:383:0x0643, B:384:0x0647, B:386:0x0650, B:391:0x065c, B:394:0x0664, B:395:0x067c, B:99:0x01ce, B:118:0x01fc, B:41:0x00c1, B:45:0x00d2, B:44:0x00cc, B:51:0x00e5, B:53:0x00ef, B:54:0x00f2, B:57:0x00f7, B:58:0x010d, B:68:0x0120, B:69:0x0126, B:71:0x012b, B:74:0x0138, B:75:0x013c, B:78:0x0142, B:79:0x015c, B:72:0x0130, B:80:0x015d, B:81:0x0177, B:87:0x0181, B:90:0x0190, B:91:0x0196, B:92:0x01b4, B:93:0x01b5, B:396:0x067d, B:397:0x0695, B:398:0x0696, B:399:0x06ae), top: B:409:0x0063, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:235:0x03d9  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x04d1 A[Catch: all -> 0x06af, TryCatch #2 {all -> 0x06af, blocks: (B:21:0x0063, B:24:0x006d, B:27:0x0076, B:31:0x0089, B:33:0x0093, B:36:0x009b, B:37:0x00b9, B:96:0x01c1, B:100:0x01d4, B:116:0x01f3, B:119:0x0200, B:122:0x0207, B:124:0x020f, B:126:0x0222, B:128:0x022d, B:130:0x0236, B:131:0x0239, B:133:0x0243, B:134:0x0251, B:136:0x0257, B:138:0x0265, B:140:0x026d, B:145:0x027c, B:146:0x0282, B:148:0x028a, B:149:0x028f, B:154:0x0299, B:155:0x02a0, B:156:0x02a1, B:158:0x02a8, B:160:0x02ac, B:161:0x02af, B:163:0x02b5, B:167:0x02c3, B:175:0x02da, B:177:0x02e2, B:179:0x02e9, B:181:0x02f8, B:183:0x0300, B:186:0x0305, B:188:0x0309, B:208:0x035b, B:210:0x035f, B:214:0x0369, B:215:0x0381, B:190:0x0310, B:192:0x0318, B:194:0x031c, B:195:0x031f, B:196:0x032b, B:199:0x0334, B:201:0x0338, B:202:0x033b, B:204:0x033f, B:205:0x0343, B:206:0x034f, B:216:0x0382, B:217:0x039e, B:220:0x03a3, B:226:0x03b4, B:228:0x03ba, B:230:0x03c6, B:231:0x03cc, B:233:0x03d1, B:333:0x0566, B:337:0x0570, B:340:0x0579, B:344:0x058c, B:343:0x0586, B:348:0x0598, B:352:0x05ab, B:354:0x05b4, B:358:0x05c7, B:375:0x060f, B:357:0x05c1, B:361:0x05d2, B:365:0x05e5, B:364:0x05df, B:368:0x05f0, B:372:0x0603, B:371:0x05fd, B:373:0x060a, B:351:0x05a5, B:379:0x0619, B:380:0x0631, B:234:0x03d5, B:241:0x03e5, B:245:0x03f4, B:249:0x040b, B:251:0x0414, B:256:0x0421, B:257:0x0424, B:259:0x042e, B:261:0x0435, B:263:0x0439, B:271:0x044c, B:272:0x0464, B:260:0x0432, B:248:0x0405, B:275:0x0469, B:279:0x047c, B:281:0x048d, B:285:0x04a1, B:287:0x04a7, B:290:0x04ad, B:292:0x04b7, B:294:0x04bf, B:298:0x04d1, B:301:0x04d9, B:302:0x04db, B:304:0x04e0, B:306:0x04e9, B:309:0x04f2, B:310:0x04f5, B:312:0x04fb, B:314:0x0502, B:321:0x050f, B:322:0x0527, B:307:0x04ed, B:282:0x0498, B:278:0x0476, B:325:0x052e, B:327:0x053a, B:330:0x054d, B:332:0x0559, B:381:0x0632, B:383:0x0643, B:384:0x0647, B:386:0x0650, B:391:0x065c, B:394:0x0664, B:395:0x067c, B:99:0x01ce, B:118:0x01fc, B:41:0x00c1, B:45:0x00d2, B:44:0x00cc, B:51:0x00e5, B:53:0x00ef, B:54:0x00f2, B:57:0x00f7, B:58:0x010d, B:68:0x0120, B:69:0x0126, B:71:0x012b, B:74:0x0138, B:75:0x013c, B:78:0x0142, B:79:0x015c, B:72:0x0130, B:80:0x015d, B:81:0x0177, B:87:0x0181, B:90:0x0190, B:91:0x0196, B:92:0x01b4, B:93:0x01b5, B:396:0x067d, B:397:0x0695, B:398:0x0696, B:399:0x06ae), top: B:409:0x0063, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:304:0x04e0 A[Catch: all -> 0x06af, TryCatch #2 {all -> 0x06af, blocks: (B:21:0x0063, B:24:0x006d, B:27:0x0076, B:31:0x0089, B:33:0x0093, B:36:0x009b, B:37:0x00b9, B:96:0x01c1, B:100:0x01d4, B:116:0x01f3, B:119:0x0200, B:122:0x0207, B:124:0x020f, B:126:0x0222, B:128:0x022d, B:130:0x0236, B:131:0x0239, B:133:0x0243, B:134:0x0251, B:136:0x0257, B:138:0x0265, B:140:0x026d, B:145:0x027c, B:146:0x0282, B:148:0x028a, B:149:0x028f, B:154:0x0299, B:155:0x02a0, B:156:0x02a1, B:158:0x02a8, B:160:0x02ac, B:161:0x02af, B:163:0x02b5, B:167:0x02c3, B:175:0x02da, B:177:0x02e2, B:179:0x02e9, B:181:0x02f8, B:183:0x0300, B:186:0x0305, B:188:0x0309, B:208:0x035b, B:210:0x035f, B:214:0x0369, B:215:0x0381, B:190:0x0310, B:192:0x0318, B:194:0x031c, B:195:0x031f, B:196:0x032b, B:199:0x0334, B:201:0x0338, B:202:0x033b, B:204:0x033f, B:205:0x0343, B:206:0x034f, B:216:0x0382, B:217:0x039e, B:220:0x03a3, B:226:0x03b4, B:228:0x03ba, B:230:0x03c6, B:231:0x03cc, B:233:0x03d1, B:333:0x0566, B:337:0x0570, B:340:0x0579, B:344:0x058c, B:343:0x0586, B:348:0x0598, B:352:0x05ab, B:354:0x05b4, B:358:0x05c7, B:375:0x060f, B:357:0x05c1, B:361:0x05d2, B:365:0x05e5, B:364:0x05df, B:368:0x05f0, B:372:0x0603, B:371:0x05fd, B:373:0x060a, B:351:0x05a5, B:379:0x0619, B:380:0x0631, B:234:0x03d5, B:241:0x03e5, B:245:0x03f4, B:249:0x040b, B:251:0x0414, B:256:0x0421, B:257:0x0424, B:259:0x042e, B:261:0x0435, B:263:0x0439, B:271:0x044c, B:272:0x0464, B:260:0x0432, B:248:0x0405, B:275:0x0469, B:279:0x047c, B:281:0x048d, B:285:0x04a1, B:287:0x04a7, B:290:0x04ad, B:292:0x04b7, B:294:0x04bf, B:298:0x04d1, B:301:0x04d9, B:302:0x04db, B:304:0x04e0, B:306:0x04e9, B:309:0x04f2, B:310:0x04f5, B:312:0x04fb, B:314:0x0502, B:321:0x050f, B:322:0x0527, B:307:0x04ed, B:282:0x0498, B:278:0x0476, B:325:0x052e, B:327:0x053a, B:330:0x054d, B:332:0x0559, B:381:0x0632, B:383:0x0643, B:384:0x0647, B:386:0x0650, B:391:0x065c, B:394:0x0664, B:395:0x067c, B:99:0x01ce, B:118:0x01fc, B:41:0x00c1, B:45:0x00d2, B:44:0x00cc, B:51:0x00e5, B:53:0x00ef, B:54:0x00f2, B:57:0x00f7, B:58:0x010d, B:68:0x0120, B:69:0x0126, B:71:0x012b, B:74:0x0138, B:75:0x013c, B:78:0x0142, B:79:0x015c, B:72:0x0130, B:80:0x015d, B:81:0x0177, B:87:0x0181, B:90:0x0190, B:91:0x0196, B:92:0x01b4, B:93:0x01b5, B:396:0x067d, B:397:0x0695, B:398:0x0696, B:399:0x06ae), top: B:409:0x0063, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:306:0x04e9 A[Catch: all -> 0x06af, TryCatch #2 {all -> 0x06af, blocks: (B:21:0x0063, B:24:0x006d, B:27:0x0076, B:31:0x0089, B:33:0x0093, B:36:0x009b, B:37:0x00b9, B:96:0x01c1, B:100:0x01d4, B:116:0x01f3, B:119:0x0200, B:122:0x0207, B:124:0x020f, B:126:0x0222, B:128:0x022d, B:130:0x0236, B:131:0x0239, B:133:0x0243, B:134:0x0251, B:136:0x0257, B:138:0x0265, B:140:0x026d, B:145:0x027c, B:146:0x0282, B:148:0x028a, B:149:0x028f, B:154:0x0299, B:155:0x02a0, B:156:0x02a1, B:158:0x02a8, B:160:0x02ac, B:161:0x02af, B:163:0x02b5, B:167:0x02c3, B:175:0x02da, B:177:0x02e2, B:179:0x02e9, B:181:0x02f8, B:183:0x0300, B:186:0x0305, B:188:0x0309, B:208:0x035b, B:210:0x035f, B:214:0x0369, B:215:0x0381, B:190:0x0310, B:192:0x0318, B:194:0x031c, B:195:0x031f, B:196:0x032b, B:199:0x0334, B:201:0x0338, B:202:0x033b, B:204:0x033f, B:205:0x0343, B:206:0x034f, B:216:0x0382, B:217:0x039e, B:220:0x03a3, B:226:0x03b4, B:228:0x03ba, B:230:0x03c6, B:231:0x03cc, B:233:0x03d1, B:333:0x0566, B:337:0x0570, B:340:0x0579, B:344:0x058c, B:343:0x0586, B:348:0x0598, B:352:0x05ab, B:354:0x05b4, B:358:0x05c7, B:375:0x060f, B:357:0x05c1, B:361:0x05d2, B:365:0x05e5, B:364:0x05df, B:368:0x05f0, B:372:0x0603, B:371:0x05fd, B:373:0x060a, B:351:0x05a5, B:379:0x0619, B:380:0x0631, B:234:0x03d5, B:241:0x03e5, B:245:0x03f4, B:249:0x040b, B:251:0x0414, B:256:0x0421, B:257:0x0424, B:259:0x042e, B:261:0x0435, B:263:0x0439, B:271:0x044c, B:272:0x0464, B:260:0x0432, B:248:0x0405, B:275:0x0469, B:279:0x047c, B:281:0x048d, B:285:0x04a1, B:287:0x04a7, B:290:0x04ad, B:292:0x04b7, B:294:0x04bf, B:298:0x04d1, B:301:0x04d9, B:302:0x04db, B:304:0x04e0, B:306:0x04e9, B:309:0x04f2, B:310:0x04f5, B:312:0x04fb, B:314:0x0502, B:321:0x050f, B:322:0x0527, B:307:0x04ed, B:282:0x0498, B:278:0x0476, B:325:0x052e, B:327:0x053a, B:330:0x054d, B:332:0x0559, B:381:0x0632, B:383:0x0643, B:384:0x0647, B:386:0x0650, B:391:0x065c, B:394:0x0664, B:395:0x067c, B:99:0x01ce, B:118:0x01fc, B:41:0x00c1, B:45:0x00d2, B:44:0x00cc, B:51:0x00e5, B:53:0x00ef, B:54:0x00f2, B:57:0x00f7, B:58:0x010d, B:68:0x0120, B:69:0x0126, B:71:0x012b, B:74:0x0138, B:75:0x013c, B:78:0x0142, B:79:0x015c, B:72:0x0130, B:80:0x015d, B:81:0x0177, B:87:0x0181, B:90:0x0190, B:91:0x0196, B:92:0x01b4, B:93:0x01b5, B:396:0x067d, B:397:0x0695, B:398:0x0696, B:399:0x06ae), top: B:409:0x0063, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:307:0x04ed A[Catch: all -> 0x06af, TryCatch #2 {all -> 0x06af, blocks: (B:21:0x0063, B:24:0x006d, B:27:0x0076, B:31:0x0089, B:33:0x0093, B:36:0x009b, B:37:0x00b9, B:96:0x01c1, B:100:0x01d4, B:116:0x01f3, B:119:0x0200, B:122:0x0207, B:124:0x020f, B:126:0x0222, B:128:0x022d, B:130:0x0236, B:131:0x0239, B:133:0x0243, B:134:0x0251, B:136:0x0257, B:138:0x0265, B:140:0x026d, B:145:0x027c, B:146:0x0282, B:148:0x028a, B:149:0x028f, B:154:0x0299, B:155:0x02a0, B:156:0x02a1, B:158:0x02a8, B:160:0x02ac, B:161:0x02af, B:163:0x02b5, B:167:0x02c3, B:175:0x02da, B:177:0x02e2, B:179:0x02e9, B:181:0x02f8, B:183:0x0300, B:186:0x0305, B:188:0x0309, B:208:0x035b, B:210:0x035f, B:214:0x0369, B:215:0x0381, B:190:0x0310, B:192:0x0318, B:194:0x031c, B:195:0x031f, B:196:0x032b, B:199:0x0334, B:201:0x0338, B:202:0x033b, B:204:0x033f, B:205:0x0343, B:206:0x034f, B:216:0x0382, B:217:0x039e, B:220:0x03a3, B:226:0x03b4, B:228:0x03ba, B:230:0x03c6, B:231:0x03cc, B:233:0x03d1, B:333:0x0566, B:337:0x0570, B:340:0x0579, B:344:0x058c, B:343:0x0586, B:348:0x0598, B:352:0x05ab, B:354:0x05b4, B:358:0x05c7, B:375:0x060f, B:357:0x05c1, B:361:0x05d2, B:365:0x05e5, B:364:0x05df, B:368:0x05f0, B:372:0x0603, B:371:0x05fd, B:373:0x060a, B:351:0x05a5, B:379:0x0619, B:380:0x0631, B:234:0x03d5, B:241:0x03e5, B:245:0x03f4, B:249:0x040b, B:251:0x0414, B:256:0x0421, B:257:0x0424, B:259:0x042e, B:261:0x0435, B:263:0x0439, B:271:0x044c, B:272:0x0464, B:260:0x0432, B:248:0x0405, B:275:0x0469, B:279:0x047c, B:281:0x048d, B:285:0x04a1, B:287:0x04a7, B:290:0x04ad, B:292:0x04b7, B:294:0x04bf, B:298:0x04d1, B:301:0x04d9, B:302:0x04db, B:304:0x04e0, B:306:0x04e9, B:309:0x04f2, B:310:0x04f5, B:312:0x04fb, B:314:0x0502, B:321:0x050f, B:322:0x0527, B:307:0x04ed, B:282:0x0498, B:278:0x0476, B:325:0x052e, B:327:0x053a, B:330:0x054d, B:332:0x0559, B:381:0x0632, B:383:0x0643, B:384:0x0647, B:386:0x0650, B:391:0x065c, B:394:0x0664, B:395:0x067c, B:99:0x01ce, B:118:0x01fc, B:41:0x00c1, B:45:0x00d2, B:44:0x00cc, B:51:0x00e5, B:53:0x00ef, B:54:0x00f2, B:57:0x00f7, B:58:0x010d, B:68:0x0120, B:69:0x0126, B:71:0x012b, B:74:0x0138, B:75:0x013c, B:78:0x0142, B:79:0x015c, B:72:0x0130, B:80:0x015d, B:81:0x0177, B:87:0x0181, B:90:0x0190, B:91:0x0196, B:92:0x01b4, B:93:0x01b5, B:396:0x067d, B:397:0x0695, B:398:0x0696, B:399:0x06ae), top: B:409:0x0063, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:309:0x04f2 A[Catch: all -> 0x06af, TryCatch #2 {all -> 0x06af, blocks: (B:21:0x0063, B:24:0x006d, B:27:0x0076, B:31:0x0089, B:33:0x0093, B:36:0x009b, B:37:0x00b9, B:96:0x01c1, B:100:0x01d4, B:116:0x01f3, B:119:0x0200, B:122:0x0207, B:124:0x020f, B:126:0x0222, B:128:0x022d, B:130:0x0236, B:131:0x0239, B:133:0x0243, B:134:0x0251, B:136:0x0257, B:138:0x0265, B:140:0x026d, B:145:0x027c, B:146:0x0282, B:148:0x028a, B:149:0x028f, B:154:0x0299, B:155:0x02a0, B:156:0x02a1, B:158:0x02a8, B:160:0x02ac, B:161:0x02af, B:163:0x02b5, B:167:0x02c3, B:175:0x02da, B:177:0x02e2, B:179:0x02e9, B:181:0x02f8, B:183:0x0300, B:186:0x0305, B:188:0x0309, B:208:0x035b, B:210:0x035f, B:214:0x0369, B:215:0x0381, B:190:0x0310, B:192:0x0318, B:194:0x031c, B:195:0x031f, B:196:0x032b, B:199:0x0334, B:201:0x0338, B:202:0x033b, B:204:0x033f, B:205:0x0343, B:206:0x034f, B:216:0x0382, B:217:0x039e, B:220:0x03a3, B:226:0x03b4, B:228:0x03ba, B:230:0x03c6, B:231:0x03cc, B:233:0x03d1, B:333:0x0566, B:337:0x0570, B:340:0x0579, B:344:0x058c, B:343:0x0586, B:348:0x0598, B:352:0x05ab, B:354:0x05b4, B:358:0x05c7, B:375:0x060f, B:357:0x05c1, B:361:0x05d2, B:365:0x05e5, B:364:0x05df, B:368:0x05f0, B:372:0x0603, B:371:0x05fd, B:373:0x060a, B:351:0x05a5, B:379:0x0619, B:380:0x0631, B:234:0x03d5, B:241:0x03e5, B:245:0x03f4, B:249:0x040b, B:251:0x0414, B:256:0x0421, B:257:0x0424, B:259:0x042e, B:261:0x0435, B:263:0x0439, B:271:0x044c, B:272:0x0464, B:260:0x0432, B:248:0x0405, B:275:0x0469, B:279:0x047c, B:281:0x048d, B:285:0x04a1, B:287:0x04a7, B:290:0x04ad, B:292:0x04b7, B:294:0x04bf, B:298:0x04d1, B:301:0x04d9, B:302:0x04db, B:304:0x04e0, B:306:0x04e9, B:309:0x04f2, B:310:0x04f5, B:312:0x04fb, B:314:0x0502, B:321:0x050f, B:322:0x0527, B:307:0x04ed, B:282:0x0498, B:278:0x0476, B:325:0x052e, B:327:0x053a, B:330:0x054d, B:332:0x0559, B:381:0x0632, B:383:0x0643, B:384:0x0647, B:386:0x0650, B:391:0x065c, B:394:0x0664, B:395:0x067c, B:99:0x01ce, B:118:0x01fc, B:41:0x00c1, B:45:0x00d2, B:44:0x00cc, B:51:0x00e5, B:53:0x00ef, B:54:0x00f2, B:57:0x00f7, B:58:0x010d, B:68:0x0120, B:69:0x0126, B:71:0x012b, B:74:0x0138, B:75:0x013c, B:78:0x0142, B:79:0x015c, B:72:0x0130, B:80:0x015d, B:81:0x0177, B:87:0x0181, B:90:0x0190, B:91:0x0196, B:92:0x01b4, B:93:0x01b5, B:396:0x067d, B:397:0x0695, B:398:0x0696, B:399:0x06ae), top: B:409:0x0063, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:318:0x0509  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x056c  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x0579 A[Catch: all -> 0x06af, TryCatch #2 {all -> 0x06af, blocks: (B:21:0x0063, B:24:0x006d, B:27:0x0076, B:31:0x0089, B:33:0x0093, B:36:0x009b, B:37:0x00b9, B:96:0x01c1, B:100:0x01d4, B:116:0x01f3, B:119:0x0200, B:122:0x0207, B:124:0x020f, B:126:0x0222, B:128:0x022d, B:130:0x0236, B:131:0x0239, B:133:0x0243, B:134:0x0251, B:136:0x0257, B:138:0x0265, B:140:0x026d, B:145:0x027c, B:146:0x0282, B:148:0x028a, B:149:0x028f, B:154:0x0299, B:155:0x02a0, B:156:0x02a1, B:158:0x02a8, B:160:0x02ac, B:161:0x02af, B:163:0x02b5, B:167:0x02c3, B:175:0x02da, B:177:0x02e2, B:179:0x02e9, B:181:0x02f8, B:183:0x0300, B:186:0x0305, B:188:0x0309, B:208:0x035b, B:210:0x035f, B:214:0x0369, B:215:0x0381, B:190:0x0310, B:192:0x0318, B:194:0x031c, B:195:0x031f, B:196:0x032b, B:199:0x0334, B:201:0x0338, B:202:0x033b, B:204:0x033f, B:205:0x0343, B:206:0x034f, B:216:0x0382, B:217:0x039e, B:220:0x03a3, B:226:0x03b4, B:228:0x03ba, B:230:0x03c6, B:231:0x03cc, B:233:0x03d1, B:333:0x0566, B:337:0x0570, B:340:0x0579, B:344:0x058c, B:343:0x0586, B:348:0x0598, B:352:0x05ab, B:354:0x05b4, B:358:0x05c7, B:375:0x060f, B:357:0x05c1, B:361:0x05d2, B:365:0x05e5, B:364:0x05df, B:368:0x05f0, B:372:0x0603, B:371:0x05fd, B:373:0x060a, B:351:0x05a5, B:379:0x0619, B:380:0x0631, B:234:0x03d5, B:241:0x03e5, B:245:0x03f4, B:249:0x040b, B:251:0x0414, B:256:0x0421, B:257:0x0424, B:259:0x042e, B:261:0x0435, B:263:0x0439, B:271:0x044c, B:272:0x0464, B:260:0x0432, B:248:0x0405, B:275:0x0469, B:279:0x047c, B:281:0x048d, B:285:0x04a1, B:287:0x04a7, B:290:0x04ad, B:292:0x04b7, B:294:0x04bf, B:298:0x04d1, B:301:0x04d9, B:302:0x04db, B:304:0x04e0, B:306:0x04e9, B:309:0x04f2, B:310:0x04f5, B:312:0x04fb, B:314:0x0502, B:321:0x050f, B:322:0x0527, B:307:0x04ed, B:282:0x0498, B:278:0x0476, B:325:0x052e, B:327:0x053a, B:330:0x054d, B:332:0x0559, B:381:0x0632, B:383:0x0643, B:384:0x0647, B:386:0x0650, B:391:0x065c, B:394:0x0664, B:395:0x067c, B:99:0x01ce, B:118:0x01fc, B:41:0x00c1, B:45:0x00d2, B:44:0x00cc, B:51:0x00e5, B:53:0x00ef, B:54:0x00f2, B:57:0x00f7, B:58:0x010d, B:68:0x0120, B:69:0x0126, B:71:0x012b, B:74:0x0138, B:75:0x013c, B:78:0x0142, B:79:0x015c, B:72:0x0130, B:80:0x015d, B:81:0x0177, B:87:0x0181, B:90:0x0190, B:91:0x0196, B:92:0x01b4, B:93:0x01b5, B:396:0x067d, B:397:0x0695, B:398:0x0696, B:399:0x06ae), top: B:409:0x0063, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:419:0x04fb A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:424:0x0594 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01c1 A[Catch: all -> 0x06af, TryCatch #2 {all -> 0x06af, blocks: (B:21:0x0063, B:24:0x006d, B:27:0x0076, B:31:0x0089, B:33:0x0093, B:36:0x009b, B:37:0x00b9, B:96:0x01c1, B:100:0x01d4, B:116:0x01f3, B:119:0x0200, B:122:0x0207, B:124:0x020f, B:126:0x0222, B:128:0x022d, B:130:0x0236, B:131:0x0239, B:133:0x0243, B:134:0x0251, B:136:0x0257, B:138:0x0265, B:140:0x026d, B:145:0x027c, B:146:0x0282, B:148:0x028a, B:149:0x028f, B:154:0x0299, B:155:0x02a0, B:156:0x02a1, B:158:0x02a8, B:160:0x02ac, B:161:0x02af, B:163:0x02b5, B:167:0x02c3, B:175:0x02da, B:177:0x02e2, B:179:0x02e9, B:181:0x02f8, B:183:0x0300, B:186:0x0305, B:188:0x0309, B:208:0x035b, B:210:0x035f, B:214:0x0369, B:215:0x0381, B:190:0x0310, B:192:0x0318, B:194:0x031c, B:195:0x031f, B:196:0x032b, B:199:0x0334, B:201:0x0338, B:202:0x033b, B:204:0x033f, B:205:0x0343, B:206:0x034f, B:216:0x0382, B:217:0x039e, B:220:0x03a3, B:226:0x03b4, B:228:0x03ba, B:230:0x03c6, B:231:0x03cc, B:233:0x03d1, B:333:0x0566, B:337:0x0570, B:340:0x0579, B:344:0x058c, B:343:0x0586, B:348:0x0598, B:352:0x05ab, B:354:0x05b4, B:358:0x05c7, B:375:0x060f, B:357:0x05c1, B:361:0x05d2, B:365:0x05e5, B:364:0x05df, B:368:0x05f0, B:372:0x0603, B:371:0x05fd, B:373:0x060a, B:351:0x05a5, B:379:0x0619, B:380:0x0631, B:234:0x03d5, B:241:0x03e5, B:245:0x03f4, B:249:0x040b, B:251:0x0414, B:256:0x0421, B:257:0x0424, B:259:0x042e, B:261:0x0435, B:263:0x0439, B:271:0x044c, B:272:0x0464, B:260:0x0432, B:248:0x0405, B:275:0x0469, B:279:0x047c, B:281:0x048d, B:285:0x04a1, B:287:0x04a7, B:290:0x04ad, B:292:0x04b7, B:294:0x04bf, B:298:0x04d1, B:301:0x04d9, B:302:0x04db, B:304:0x04e0, B:306:0x04e9, B:309:0x04f2, B:310:0x04f5, B:312:0x04fb, B:314:0x0502, B:321:0x050f, B:322:0x0527, B:307:0x04ed, B:282:0x0498, B:278:0x0476, B:325:0x052e, B:327:0x053a, B:330:0x054d, B:332:0x0559, B:381:0x0632, B:383:0x0643, B:384:0x0647, B:386:0x0650, B:391:0x065c, B:394:0x0664, B:395:0x067c, B:99:0x01ce, B:118:0x01fc, B:41:0x00c1, B:45:0x00d2, B:44:0x00cc, B:51:0x00e5, B:53:0x00ef, B:54:0x00f2, B:57:0x00f7, B:58:0x010d, B:68:0x0120, B:69:0x0126, B:71:0x012b, B:74:0x0138, B:75:0x013c, B:78:0x0142, B:79:0x015c, B:72:0x0130, B:80:0x015d, B:81:0x0177, B:87:0x0181, B:90:0x0190, B:91:0x0196, B:92:0x01b4, B:93:0x01b5, B:396:0x067d, B:397:0x0695, B:398:0x0696, B:399:0x06ae), top: B:409:0x0063, inners: #0, #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object parseObject(Map map, Object obj) {
        Map<String, Object> map2;
        boolean z;
        boolean z2;
        String str;
        char c;
        char c2;
        boolean z3;
        Object obj2;
        boolean z4;
        int i;
        String str2;
        char c3;
        Object obj3;
        String str3;
        JSONLexer jSONLexer = this.lexer;
        int i2 = jSONLexer.token;
        if (i2 == 8) {
            jSONLexer.nextToken();
            return null;
        } else if (i2 != 12 && i2 != 16) {
            throw new JSONException("syntax error, expect {, actual " + JSONToken.name(i2) + ", " + jSONLexer.info());
        } else {
            if (map instanceof JSONObject) {
                z = true;
                map2 = ((JSONObject) map).getInnerMap();
            } else {
                map2 = map;
                z = false;
            }
            boolean z5 = (jSONLexer.features & Feature.AllowISO8601DateFormat.mask) != 0;
            boolean z6 = jSONLexer.disableCircularReferenceDetect;
            ParseContext parseContext = this.contex;
            boolean z7 = false;
            while (true) {
                try {
                    char c4 = jSONLexer.ch;
                    if (c4 != '\"' && c4 != '}') {
                        jSONLexer.skipWhitespace();
                        c4 = jSONLexer.ch;
                    }
                    while (c4 == ',') {
                        jSONLexer.next();
                        jSONLexer.skipWhitespace();
                        c4 = jSONLexer.ch;
                    }
                    char c5 = JSONLexer.EOI;
                    if (c4 == '\"') {
                        String scanSymbol = jSONLexer.scanSymbol(this.symbolTable, TokenParser.DQUOTE);
                        str3 = scanSymbol;
                        if (jSONLexer.ch != ':') {
                            jSONLexer.skipWhitespace();
                            if (jSONLexer.ch != ':') {
                                throw new JSONException("expect ':' at " + jSONLexer.pos + ", name " + ((Object) scanSymbol));
                            }
                            str3 = scanSymbol;
                        }
                    } else if (c4 == '}') {
                        int i3 = jSONLexer.bp + 1;
                        jSONLexer.bp = i3;
                        if (i3 < jSONLexer.len) {
                            c5 = jSONLexer.text.charAt(i3);
                        }
                        jSONLexer.ch = c5;
                        jSONLexer.sp = 0;
                        jSONLexer.nextToken(16);
                        return map;
                    } else if (c4 == '\'') {
                        String scanSymbol2 = jSONLexer.scanSymbol(this.symbolTable, '\'');
                        if (jSONLexer.ch != ':') {
                            jSONLexer.skipWhitespace();
                        }
                        if (jSONLexer.ch != ':') {
                            throw new JSONException("expect ':' at " + jSONLexer.pos);
                        }
                        str3 = scanSymbol2;
                    } else if (c4 == 26) {
                        throw new JSONException("syntax error, " + jSONLexer.info());
                    } else if (c4 == ',') {
                        throw new JSONException("syntax error, " + jSONLexer.info());
                    } else if ((c4 < '0' || c4 > '9') && c4 != '-') {
                        if (c4 != '{' && c4 != '[') {
                            String scanSymbolUnQuoted = jSONLexer.scanSymbolUnQuoted(this.symbolTable);
                            jSONLexer.skipWhitespace();
                            char c6 = jSONLexer.ch;
                            str3 = scanSymbolUnQuoted;
                            if (c6 != ':') {
                                throw new JSONException("expect ':' at " + jSONLexer.bp + ", actual " + c6);
                            } else if (z) {
                                str3 = scanSymbolUnQuoted.toString();
                            }
                        }
                        jSONLexer.nextToken();
                        z2 = true;
                        str = parse();
                        if (z2) {
                            int i4 = jSONLexer.bp + 1;
                            jSONLexer.bp = i4;
                            c = i4 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i4);
                            jSONLexer.ch = c;
                            while (c <= ' ') {
                                if (c != ' ' && c != '\n' && c != '\r' && c != '\t' && c != '\f') {
                                    if (c != '\b') {
                                        break;
                                    }
                                }
                                jSONLexer.next();
                                c = jSONLexer.ch;
                            }
                        } else {
                            c = jSONLexer.ch;
                        }
                        jSONLexer.sp = 0;
                        if (str != JSON.DEFAULT_TYPE_KEY && !jSONLexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                            String scanSymbol3 = jSONLexer.scanSymbol(this.symbolTable, TokenParser.DQUOTE);
                            Class<?> checkAutoType = this.config.checkAutoType(scanSymbol3, null, jSONLexer.features);
                            if (checkAutoType != null) {
                                break;
                            }
                            map.put(JSON.DEFAULT_TYPE_KEY, scanSymbol3);
                        } else if (str != "$ref" && parseContext != null && !jSONLexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                            jSONLexer.nextToken(4);
                            if (jSONLexer.token != 4) {
                                throw new JSONException("illegal ref, " + JSONToken.name(jSONLexer.token));
                            }
                            String stringVal = jSONLexer.stringVal();
                            jSONLexer.nextToken(13);
                            if ("@".equals(stringVal)) {
                                ParseContext parseContext2 = this.contex;
                                Object obj4 = parseContext2.object;
                                if (!(obj4 instanceof Object[]) && !(obj4 instanceof Collection)) {
                                    if (parseContext2.parent != null) {
                                        obj3 = parseContext2.parent.object;
                                    }
                                    obj3 = null;
                                }
                                obj3 = obj4;
                            } else if (!"..".equals(stringVal)) {
                                if ("$".equals(stringVal)) {
                                    ParseContext parseContext3 = parseContext;
                                    while (parseContext3.parent != null) {
                                        parseContext3 = parseContext3.parent;
                                    }
                                    if (parseContext3.object != null) {
                                        obj3 = parseContext3.object;
                                    } else {
                                        addResolveTask(new ResolveTask(parseContext3, stringVal));
                                        this.resolveStatus = 1;
                                    }
                                } else {
                                    addResolveTask(new ResolveTask(parseContext, stringVal));
                                    this.resolveStatus = 1;
                                }
                                obj3 = null;
                            } else if (parseContext.object != null) {
                                obj3 = parseContext.object;
                            } else {
                                addResolveTask(new ResolveTask(parseContext, stringVal));
                                this.resolveStatus = 1;
                                obj3 = null;
                            }
                            if (jSONLexer.token == 13) {
                                jSONLexer.nextToken(16);
                                if (!z6) {
                                    this.contex = parseContext;
                                }
                                return obj3;
                            }
                            throw new JSONException("syntax error, " + jSONLexer.info());
                        } else {
                            if (!z6 || z7) {
                                c2 = TokenParser.DQUOTE;
                            } else {
                                ParseContext context = setContext(this.contex, map, obj);
                                if (parseContext == null) {
                                    parseContext = context;
                                }
                                c2 = TokenParser.DQUOTE;
                                z7 = true;
                            }
                            if (c != c2) {
                                String scanStringValue = jSONLexer.scanStringValue(c2);
                                String str4 = scanStringValue;
                                if (z5) {
                                    JSONLexer jSONLexer2 = new JSONLexer(scanStringValue);
                                    Date date = scanStringValue;
                                    if (jSONLexer2.scanISO8601DateIfMatch(true)) {
                                        date = jSONLexer2.calendar.getTime();
                                    }
                                    jSONLexer2.close();
                                    str4 = date;
                                }
                                if (map2 != null) {
                                    map2.put(str, str4);
                                } else {
                                    map.put(str, str4);
                                }
                            } else if ((c < '0' || c > '9') && c != '-') {
                                if (c == '[') {
                                    jSONLexer.token = 14;
                                    int i5 = jSONLexer.bp + 1;
                                    jSONLexer.bp = i5;
                                    jSONLexer.ch = i5 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i5);
                                    ArrayList arrayList = new ArrayList();
                                    if (!(obj != null && obj.getClass() == Integer.class)) {
                                        setContext(parseContext);
                                    }
                                    parseArray(arrayList, str);
                                    JSONArray jSONArray = new JSONArray(arrayList);
                                    if (map2 != null) {
                                        map2.put(str, jSONArray);
                                    } else {
                                        map.put(str, jSONArray);
                                    }
                                    int i6 = jSONLexer.token;
                                    if (i6 == 13) {
                                        jSONLexer.nextToken(16);
                                        if (!z6) {
                                            this.contex = parseContext;
                                        }
                                        return map;
                                    } else if (i6 != 16) {
                                        throw new JSONException("syntax error, " + jSONLexer.info());
                                    } else {
                                        z3 = z;
                                    }
                                } else if (c == '{') {
                                    int i7 = jSONLexer.bp + 1;
                                    jSONLexer.bp = i7;
                                    jSONLexer.ch = i7 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i7);
                                    jSONLexer.token = 12;
                                    boolean z8 = obj instanceof Integer;
                                    Map jSONObject = (jSONLexer.features & Feature.OrderedField.mask) != 0 ? new JSONObject(new LinkedHashMap()) : new JSONObject();
                                    ParseContext context2 = (z6 || z8) ? null : setContext(parseContext, jSONObject, str);
                                    if (this.fieldTypeResolver != null) {
                                        if (str != null) {
                                            str2 = str.toString();
                                            z3 = z;
                                        } else {
                                            z3 = z;
                                            str2 = null;
                                        }
                                        Type resolve = this.fieldTypeResolver.resolve(map, str2);
                                        if (resolve != null) {
                                            obj2 = this.config.getDeserializer(resolve).deserialze(this, resolve, str);
                                            z4 = true;
                                            if (!z4) {
                                                obj2 = parseObject(jSONObject, str);
                                            }
                                            if (context2 != null && jSONObject != obj2) {
                                                context2.object = map;
                                            }
                                            if (this.resolveStatus == 1) {
                                                checkMapResolve(map, str.toString());
                                            }
                                            if (map2 == null) {
                                                map2.put(str, obj2);
                                            } else {
                                                map.put(str, obj2);
                                            }
                                            if (z8) {
                                                setContext(parseContext, obj2, str);
                                            }
                                            i = jSONLexer.token;
                                            if (i != 13) {
                                                jSONLexer.nextToken(16);
                                                if (!z6) {
                                                    this.contex = parseContext;
                                                }
                                                if (!z6) {
                                                    this.contex = parseContext;
                                                }
                                                return map;
                                            } else if (i != 16) {
                                                throw new JSONException("syntax error, " + jSONLexer.info());
                                            }
                                        }
                                    } else {
                                        z3 = z;
                                    }
                                    obj2 = null;
                                    z4 = false;
                                    if (!z4) {
                                    }
                                    if (context2 != null) {
                                        context2.object = map;
                                    }
                                    if (this.resolveStatus == 1) {
                                    }
                                    if (map2 == null) {
                                    }
                                    if (z8) {
                                    }
                                    i = jSONLexer.token;
                                    if (i != 13) {
                                    }
                                } else {
                                    z3 = z;
                                    if (c == 't') {
                                        if (jSONLexer.text.startsWith("true", jSONLexer.bp)) {
                                            jSONLexer.bp += 3;
                                            jSONLexer.next();
                                            map.put(str, Boolean.TRUE);
                                        }
                                    } else if (c != 'f') {
                                        jSONLexer.nextToken();
                                        Object parse = parse();
                                        String str5 = str;
                                        if (map.getClass() == JSONObject.class) {
                                            str5 = str.toString();
                                        }
                                        map.put(str5, parse);
                                        if (jSONLexer.token == 13) {
                                            jSONLexer.nextToken(16);
                                            if (!z6) {
                                                this.contex = parseContext;
                                            }
                                            return map;
                                        }
                                        if (jSONLexer.token != 16) {
                                            throw new JSONException("syntax error, " + jSONLexer.info());
                                        }
                                        z = z3;
                                    } else if (jSONLexer.text.startsWith("false", jSONLexer.bp)) {
                                        jSONLexer.bp += 4;
                                        jSONLexer.next();
                                        map.put(str, Boolean.FALSE);
                                    }
                                    c3 = jSONLexer.ch;
                                    if (c3 != ',' && c3 != '}') {
                                        jSONLexer.skipWhitespace();
                                        c3 = jSONLexer.ch;
                                    }
                                    if (c3 != ',') {
                                        if (c3 != '}') {
                                            throw new JSONException("syntax error, " + jSONLexer.info());
                                        }
                                        int i8 = jSONLexer.bp + 1;
                                        jSONLexer.bp = i8;
                                        char charAt = i8 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i8);
                                        jSONLexer.ch = charAt;
                                        jSONLexer.sp = 0;
                                        if (charAt == ',') {
                                            int i9 = jSONLexer.bp + 1;
                                            jSONLexer.bp = i9;
                                            jSONLexer.ch = i9 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i9);
                                            jSONLexer.token = 16;
                                        } else if (charAt == '}') {
                                            int i10 = jSONLexer.bp + 1;
                                            jSONLexer.bp = i10;
                                            jSONLexer.ch = i10 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i10);
                                            jSONLexer.token = 13;
                                        } else if (charAt == ']') {
                                            int i11 = jSONLexer.bp + 1;
                                            jSONLexer.bp = i11;
                                            jSONLexer.ch = i11 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i11);
                                            jSONLexer.token = 15;
                                        } else {
                                            jSONLexer.nextToken();
                                        }
                                        if (!z6) {
                                            setContext(this.contex, map, obj);
                                        }
                                        if (!z6) {
                                            this.contex = parseContext;
                                        }
                                        return map;
                                    }
                                    int i12 = jSONLexer.bp + 1;
                                    jSONLexer.bp = i12;
                                    jSONLexer.ch = i12 >= jSONLexer.len ? JSONLexer.EOI : jSONLexer.text.charAt(i12);
                                    z = z3;
                                }
                                z = z3;
                            } else {
                                map2.put(str, jSONLexer.scanNumberValue());
                            }
                            z3 = z;
                            c3 = jSONLexer.ch;
                            if (c3 != ',') {
                                jSONLexer.skipWhitespace();
                                c3 = jSONLexer.ch;
                            }
                            if (c3 != ',') {
                            }
                        }
                    } else {
                        jSONLexer.sp = 0;
                        jSONLexer.scanNumber();
                        try {
                            String integerValue = jSONLexer.token == 2 ? jSONLexer.integerValue() : jSONLexer.decimalValue(true);
                            if (z) {
                                integerValue = integerValue.toString();
                            }
                            if (jSONLexer.ch != ':') {
                                throw new JSONException("parse number key error, " + jSONLexer.info());
                            }
                            str3 = integerValue;
                        } catch (NumberFormatException unused) {
                            throw new JSONException("parse number key error, " + jSONLexer.info());
                        }
                    }
                    z2 = false;
                    str = str3;
                    if (z2) {
                    }
                    jSONLexer.sp = 0;
                    if (str != JSON.DEFAULT_TYPE_KEY) {
                    }
                    if (str != "$ref") {
                    }
                    if (z6) {
                    }
                    c2 = TokenParser.DQUOTE;
                    if (c != c2) {
                    }
                    z3 = z;
                    c3 = jSONLexer.ch;
                    if (c3 != ',') {
                    }
                    if (c3 != ',') {
                    }
                } finally {
                    if (!z6) {
                        this.contex = parseContext;
                    }
                }
            }
        }
    }

    public <T> T parseObject(Class<T> cls) {
        return (T) parseObject(cls, (Object) null);
    }

    public <T> T parseObject(Type type) {
        return (T) parseObject(type, (Object) null);
    }

    public <T> T parseObject(Type type, Object obj) {
        if (this.lexer.token == 8) {
            this.lexer.nextToken();
            return null;
        }
        if (this.lexer.token == 4) {
            if (type == byte[].class) {
                T t = (T) this.lexer.bytesValue();
                this.lexer.nextToken();
                return t;
            } else if (type == char[].class) {
                String stringVal = this.lexer.stringVal();
                this.lexer.nextToken();
                return (T) stringVal.toCharArray();
            }
        }
        try {
            return (T) this.config.getDeserializer(type).deserialze(this, type, obj);
        } catch (JSONException e) {
            throw e;
        } catch (Exception e2) {
            throw new JSONException(e2.getMessage(), e2);
        }
    }

    public <T> List<T> parseArray(Class<T> cls) {
        ArrayList arrayList = new ArrayList();
        parseArray((Class<?>) cls, (Collection) arrayList);
        return arrayList;
    }

    public void parseArray(Class<?> cls, Collection collection) {
        parseArray((Type) cls, collection);
    }

    public void parseArray(Type type, Collection collection) {
        parseArray(type, collection, null);
    }

    public void parseArray(Type type, Collection collection, Object obj) {
        ObjectDeserializer deserializer;
        String str;
        if (this.lexer.token == 21 || this.lexer.token == 22) {
            this.lexer.nextToken();
        }
        if (this.lexer.token != 14) {
            throw new JSONException("exepct '[', but " + JSONToken.name(this.lexer.token) + ", " + this.lexer.info());
        }
        if (Integer.TYPE == type) {
            deserializer = IntegerCodec.instance;
            this.lexer.nextToken(2);
        } else if (String.class == type) {
            deserializer = StringCodec.instance;
            this.lexer.nextToken(4);
        } else {
            deserializer = this.config.getDeserializer(type);
            this.lexer.nextToken(12);
        }
        ParseContext parseContext = this.contex;
        if (!this.lexer.disableCircularReferenceDetect) {
            setContext(this.contex, collection, obj);
        }
        int i = 0;
        while (true) {
            try {
                if (this.lexer.token == 16) {
                    this.lexer.nextToken();
                } else if (this.lexer.token != 15) {
                    Object obj2 = null;
                    String obj3 = null;
                    if (Integer.TYPE == type) {
                        collection.add(IntegerCodec.instance.deserialze(this, null, null));
                    } else if (String.class == type) {
                        if (this.lexer.token == 4) {
                            str = this.lexer.stringVal();
                            this.lexer.nextToken(16);
                        } else {
                            Object parse = parse();
                            if (parse != null) {
                                obj3 = parse.toString();
                            }
                            str = obj3;
                        }
                        collection.add(str);
                    } else {
                        if (this.lexer.token == 8) {
                            this.lexer.nextToken();
                        } else {
                            obj2 = deserializer.deserialze(this, type, Integer.valueOf(i));
                        }
                        collection.add(obj2);
                        if (this.resolveStatus == 1) {
                            checkListResolve(collection);
                        }
                    }
                    if (this.lexer.token == 16) {
                        this.lexer.nextToken();
                    }
                    i++;
                } else {
                    this.contex = parseContext;
                    this.lexer.nextToken(16);
                    return;
                }
            } catch (Throwable th) {
                this.contex = parseContext;
                throw th;
            }
        }
    }

    public Object[] parseArray(Type[] typeArr) {
        Object cast;
        Class<?> cls;
        boolean z;
        int i = 8;
        if (this.lexer.token == 8) {
            this.lexer.nextToken(16);
            return null;
        } else if (this.lexer.token != 14) {
            throw new JSONException("syntax error, " + this.lexer.info());
        } else {
            Object[] objArr = new Object[typeArr.length];
            if (typeArr.length == 0) {
                this.lexer.nextToken(15);
                if (this.lexer.token != 15) {
                    throw new JSONException("syntax error, " + this.lexer.info());
                }
                this.lexer.nextToken(16);
                return new Object[0];
            }
            this.lexer.nextToken(2);
            int i2 = 0;
            while (i2 < typeArr.length) {
                if (this.lexer.token == i) {
                    this.lexer.nextToken(16);
                    cast = null;
                } else {
                    Type type = typeArr[i2];
                    if (type == Integer.TYPE || type == Integer.class) {
                        if (this.lexer.token == 2) {
                            cast = Integer.valueOf(this.lexer.intValue());
                            this.lexer.nextToken(16);
                        } else {
                            cast = TypeUtils.cast(parse(), type, this.config);
                        }
                    } else if (type == String.class) {
                        if (this.lexer.token == 4) {
                            cast = this.lexer.stringVal();
                            this.lexer.nextToken(16);
                        } else {
                            cast = TypeUtils.cast(parse(), type, this.config);
                        }
                    } else {
                        if (i2 == typeArr.length - 1 && (type instanceof Class)) {
                            Class cls2 = (Class) type;
                            z = cls2.isArray();
                            cls = cls2.getComponentType();
                        } else {
                            cls = null;
                            z = false;
                        }
                        if (z && this.lexer.token != 14) {
                            ArrayList arrayList = new ArrayList();
                            ObjectDeserializer deserializer = this.config.getDeserializer(cls);
                            if (this.lexer.token != 15) {
                                while (true) {
                                    arrayList.add(deserializer.deserialze(this, type, null));
                                    if (this.lexer.token != 16) {
                                        break;
                                    }
                                    this.lexer.nextToken(12);
                                }
                                if (this.lexer.token != 15) {
                                    throw new JSONException("syntax error, " + this.lexer.info());
                                }
                            }
                            cast = TypeUtils.cast(arrayList, type, this.config);
                        } else {
                            cast = this.config.getDeserializer(type).deserialze(this, type, null);
                        }
                    }
                }
                objArr[i2] = cast;
                if (this.lexer.token == 15) {
                    break;
                } else if (this.lexer.token != 16) {
                    throw new JSONException("syntax error, " + this.lexer.info());
                } else {
                    if (i2 == typeArr.length - 1) {
                        this.lexer.nextToken(15);
                    } else {
                        this.lexer.nextToken(2);
                    }
                    i2++;
                    i = 8;
                }
            }
            if (this.lexer.token != 15) {
                throw new JSONException("syntax error, " + this.lexer.info());
            }
            this.lexer.nextToken(16);
            return objArr;
        }
    }

    public void parseObject(Object obj) {
        Object deserialze;
        Class<?> cls = obj.getClass();
        ObjectDeserializer deserializer = this.config.getDeserializer(cls);
        JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
        int i = this.lexer.token;
        if (i != 12 && i != 16) {
            throw new JSONException("syntax error, expect {, actual " + JSONToken.name(i));
        }
        while (true) {
            String scanSymbol = this.lexer.scanSymbol(this.symbolTable);
            if (scanSymbol == null) {
                if (this.lexer.token == 13) {
                    this.lexer.nextToken(16);
                    return;
                } else if (this.lexer.token == 16) {
                    continue;
                }
            }
            FieldDeserializer fieldDeserializer = javaBeanDeserializer != null ? javaBeanDeserializer.getFieldDeserializer(scanSymbol) : null;
            if (fieldDeserializer == null) {
                if ((this.lexer.features & Feature.IgnoreNotMatch.mask) == 0) {
                    throw new JSONException("setter not found, class " + cls.getName() + ", property " + scanSymbol);
                }
                this.lexer.nextTokenWithChar(':');
                parse();
                if (this.lexer.token == 13) {
                    this.lexer.nextToken();
                    return;
                }
            } else {
                Class<?> cls2 = fieldDeserializer.fieldInfo.fieldClass;
                Type type = fieldDeserializer.fieldInfo.fieldType;
                if (cls2 == Integer.TYPE) {
                    this.lexer.nextTokenWithChar(':');
                    deserialze = IntegerCodec.instance.deserialze(this, type, null);
                } else if (cls2 == String.class) {
                    this.lexer.nextTokenWithChar(':');
                    deserialze = parseString();
                } else if (cls2 == Long.TYPE) {
                    this.lexer.nextTokenWithChar(':');
                    deserialze = IntegerCodec.instance.deserialze(this, type, null);
                } else {
                    ObjectDeserializer deserializer2 = this.config.getDeserializer(cls2, type);
                    this.lexer.nextTokenWithChar(':');
                    deserialze = deserializer2.deserialze(this, type, null);
                }
                fieldDeserializer.setValue(obj, deserialze);
                if (this.lexer.token != 16 && this.lexer.token == 13) {
                    this.lexer.nextToken(16);
                    return;
                }
            }
        }
    }

    public Object parseArrayWithType(Type type) {
        if (this.lexer.token == 8) {
            this.lexer.nextToken();
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        if (actualTypeArguments.length != 1) {
            throw new JSONException("not support type " + type);
        }
        Type type2 = actualTypeArguments[0];
        if (type2 instanceof Class) {
            ArrayList arrayList = new ArrayList();
            parseArray((Class) type2, (Collection) arrayList);
            return arrayList;
        } else if (type2 instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type2;
            Type type3 = wildcardType.getUpperBounds()[0];
            if (Object.class.equals(type3)) {
                if (wildcardType.getLowerBounds().length == 0) {
                    return parse();
                }
                throw new JSONException("not support type : " + type);
            }
            ArrayList arrayList2 = new ArrayList();
            parseArray((Class) type3, (Collection) arrayList2);
            return arrayList2;
        } else {
            if (type2 instanceof TypeVariable) {
                TypeVariable typeVariable = (TypeVariable) type2;
                Type[] bounds = typeVariable.getBounds();
                if (bounds.length != 1) {
                    throw new JSONException("not support : " + typeVariable);
                }
                Type type4 = bounds[0];
                if (type4 instanceof Class) {
                    ArrayList arrayList3 = new ArrayList();
                    parseArray((Class) type4, (Collection) arrayList3);
                    return arrayList3;
                }
            }
            if (type2 instanceof ParameterizedType) {
                ArrayList arrayList4 = new ArrayList();
                parseArray((ParameterizedType) type2, arrayList4);
                return arrayList4;
            }
            throw new JSONException("TODO : " + type);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkListResolve(Collection collection) {
        if (collection instanceof List) {
            ResolveTask lastResolveTask = getLastResolveTask();
            lastResolveTask.fieldDeserializer = new ResolveFieldDeserializer(this, (List) collection, collection.size() - 1);
            lastResolveTask.ownerContext = this.contex;
            this.resolveStatus = 0;
            return;
        }
        ResolveTask lastResolveTask2 = getLastResolveTask();
        lastResolveTask2.fieldDeserializer = new ResolveFieldDeserializer(collection);
        lastResolveTask2.ownerContext = this.contex;
        this.resolveStatus = 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkMapResolve(Map map, Object obj) {
        ResolveFieldDeserializer resolveFieldDeserializer = new ResolveFieldDeserializer(map, obj);
        ResolveTask lastResolveTask = getLastResolveTask();
        lastResolveTask.fieldDeserializer = resolveFieldDeserializer;
        lastResolveTask.ownerContext = this.contex;
        this.resolveStatus = 0;
    }

    public Object parseObject(Map map) {
        return parseObject(map, (Object) null);
    }

    public JSONObject parseObject() {
        return (JSONObject) parseObject((this.lexer.features & Feature.OrderedField.mask) != 0 ? new JSONObject(new LinkedHashMap()) : new JSONObject(), (Object) null);
    }

    public final void parseArray(Collection collection) {
        parseArray(collection, (Object) null);
    }

    /* JADX WARN: Removed duplicated region for block: B:113:0x01fc A[Catch: all -> 0x0274, TryCatch #0 {all -> 0x0274, blocks: (B:12:0x002a, B:15:0x003e, B:20:0x004f, B:24:0x0067, B:33:0x008b, B:35:0x0091, B:37:0x009f, B:41:0x00b7, B:43:0x00c0, B:47:0x00ca, B:40:0x00af, B:49:0x00d3, B:53:0x00eb, B:55:0x00f4, B:56:0x00f7, B:52:0x00e3, B:60:0x0101, B:61:0x0106, B:63:0x010c, B:84:0x013b, B:114:0x020a, B:116:0x0211, B:117:0x0214, B:119:0x021a, B:121:0x0220, B:126:0x0235, B:129:0x0246, B:133:0x0262, B:132:0x025a, B:134:0x0265, B:86:0x0143, B:90:0x014d, B:91:0x015a, B:92:0x0162, B:93:0x0169, B:94:0x016a, B:96:0x0177, B:98:0x0187, B:97:0x0182, B:99:0x0190, B:100:0x0198, B:101:0x01a2, B:102:0x01ac, B:104:0x01c4, B:106:0x01cf, B:107:0x01d5, B:108:0x01da, B:110:0x01e7, B:112:0x01f6, B:111:0x01ef, B:113:0x01fc, B:23:0x005f, B:25:0x006e, B:27:0x0075, B:30:0x0082), top: B:143:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0211 A[Catch: all -> 0x0274, TryCatch #0 {all -> 0x0274, blocks: (B:12:0x002a, B:15:0x003e, B:20:0x004f, B:24:0x0067, B:33:0x008b, B:35:0x0091, B:37:0x009f, B:41:0x00b7, B:43:0x00c0, B:47:0x00ca, B:40:0x00af, B:49:0x00d3, B:53:0x00eb, B:55:0x00f4, B:56:0x00f7, B:52:0x00e3, B:60:0x0101, B:61:0x0106, B:63:0x010c, B:84:0x013b, B:114:0x020a, B:116:0x0211, B:117:0x0214, B:119:0x021a, B:121:0x0220, B:126:0x0235, B:129:0x0246, B:133:0x0262, B:132:0x025a, B:134:0x0265, B:86:0x0143, B:90:0x014d, B:91:0x015a, B:92:0x0162, B:93:0x0169, B:94:0x016a, B:96:0x0177, B:98:0x0187, B:97:0x0182, B:99:0x0190, B:100:0x0198, B:101:0x01a2, B:102:0x01ac, B:104:0x01c4, B:106:0x01cf, B:107:0x01d5, B:108:0x01da, B:110:0x01e7, B:112:0x01f6, B:111:0x01ef, B:113:0x01fc, B:23:0x005f, B:25:0x006e, B:27:0x0075, B:30:0x0082), top: B:143:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x021a A[Catch: all -> 0x0274, TryCatch #0 {all -> 0x0274, blocks: (B:12:0x002a, B:15:0x003e, B:20:0x004f, B:24:0x0067, B:33:0x008b, B:35:0x0091, B:37:0x009f, B:41:0x00b7, B:43:0x00c0, B:47:0x00ca, B:40:0x00af, B:49:0x00d3, B:53:0x00eb, B:55:0x00f4, B:56:0x00f7, B:52:0x00e3, B:60:0x0101, B:61:0x0106, B:63:0x010c, B:84:0x013b, B:114:0x020a, B:116:0x0211, B:117:0x0214, B:119:0x021a, B:121:0x0220, B:126:0x0235, B:129:0x0246, B:133:0x0262, B:132:0x025a, B:134:0x0265, B:86:0x0143, B:90:0x014d, B:91:0x015a, B:92:0x0162, B:93:0x0169, B:94:0x016a, B:96:0x0177, B:98:0x0187, B:97:0x0182, B:99:0x0190, B:100:0x0198, B:101:0x01a2, B:102:0x01ac, B:104:0x01c4, B:106:0x01cf, B:107:0x01d5, B:108:0x01da, B:110:0x01e7, B:112:0x01f6, B:111:0x01ef, B:113:0x01fc, B:23:0x005f, B:25:0x006e, B:27:0x0075, B:30:0x0082), top: B:143:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x009f A[Catch: all -> 0x0274, TryCatch #0 {all -> 0x0274, blocks: (B:12:0x002a, B:15:0x003e, B:20:0x004f, B:24:0x0067, B:33:0x008b, B:35:0x0091, B:37:0x009f, B:41:0x00b7, B:43:0x00c0, B:47:0x00ca, B:40:0x00af, B:49:0x00d3, B:53:0x00eb, B:55:0x00f4, B:56:0x00f7, B:52:0x00e3, B:60:0x0101, B:61:0x0106, B:63:0x010c, B:84:0x013b, B:114:0x020a, B:116:0x0211, B:117:0x0214, B:119:0x021a, B:121:0x0220, B:126:0x0235, B:129:0x0246, B:133:0x0262, B:132:0x025a, B:134:0x0265, B:86:0x0143, B:90:0x014d, B:91:0x015a, B:92:0x0162, B:93:0x0169, B:94:0x016a, B:96:0x0177, B:98:0x0187, B:97:0x0182, B:99:0x0190, B:100:0x0198, B:101:0x01a2, B:102:0x01ac, B:104:0x01c4, B:106:0x01cf, B:107:0x01d5, B:108:0x01da, B:110:0x01e7, B:112:0x01f6, B:111:0x01ef, B:113:0x01fc, B:23:0x005f, B:25:0x006e, B:27:0x0075, B:30:0x0082), top: B:143:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x010c A[Catch: all -> 0x0274, LOOP:1: B:62:0x010a->B:63:0x010c, LOOP_END, TryCatch #0 {all -> 0x0274, blocks: (B:12:0x002a, B:15:0x003e, B:20:0x004f, B:24:0x0067, B:33:0x008b, B:35:0x0091, B:37:0x009f, B:41:0x00b7, B:43:0x00c0, B:47:0x00ca, B:40:0x00af, B:49:0x00d3, B:53:0x00eb, B:55:0x00f4, B:56:0x00f7, B:52:0x00e3, B:60:0x0101, B:61:0x0106, B:63:0x010c, B:84:0x013b, B:114:0x020a, B:116:0x0211, B:117:0x0214, B:119:0x021a, B:121:0x0220, B:126:0x0235, B:129:0x0246, B:133:0x0262, B:132:0x025a, B:134:0x0265, B:86:0x0143, B:90:0x014d, B:91:0x015a, B:92:0x0162, B:93:0x0169, B:94:0x016a, B:96:0x0177, B:98:0x0187, B:97:0x0182, B:99:0x0190, B:100:0x0198, B:101:0x01a2, B:102:0x01ac, B:104:0x01c4, B:106:0x01cf, B:107:0x01d5, B:108:0x01da, B:110:0x01e7, B:112:0x01f6, B:111:0x01ef, B:113:0x01fc, B:23:0x005f, B:25:0x006e, B:27:0x0075, B:30:0x0082), top: B:143:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x011a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void parseArray(Collection collection, Object obj) {
        boolean z;
        int i;
        int i2;
        Number integerValue;
        char c;
        int i3 = this.lexer.token;
        if (i3 == 21 || i3 == 22) {
            this.lexer.nextToken();
            i3 = this.lexer.token;
        }
        if (i3 != 14) {
            throw new JSONException("syntax error, expect [, actual " + JSONToken.name(i3) + ", pos " + this.lexer.pos);
        }
        boolean z2 = this.lexer.disableCircularReferenceDetect;
        ParseContext parseContext = this.contex;
        if (!z2) {
            setContext(parseContext, collection, obj);
        }
        try {
            char c2 = this.lexer.ch;
            char c3 = ']';
            if (c2 != '\"') {
                if (c2 == ']') {
                    this.lexer.next();
                    this.lexer.nextToken(16);
                    if (z2) {
                        return;
                    }
                    return;
                } else if (c2 == '{') {
                    JSONLexer jSONLexer = this.lexer;
                    int i4 = jSONLexer.bp + 1;
                    jSONLexer.bp = i4;
                    JSONLexer jSONLexer2 = this.lexer;
                    jSONLexer2.ch = i4 >= jSONLexer2.len ? JSONLexer.EOI : this.lexer.text.charAt(i4);
                    this.lexer.token = 12;
                } else {
                    this.lexer.nextToken(12);
                }
            } else if ((this.lexer.features & Feature.AllowISO8601DateFormat.mask) == 0) {
                z = true;
                i = 0;
                while (true) {
                    if (z && this.lexer.ch == '\"') {
                        String scanStringValue = this.lexer.scanStringValue(TokenParser.DQUOTE);
                        c = this.lexer.ch;
                        if (c != ',') {
                            JSONLexer jSONLexer3 = this.lexer;
                            int i5 = jSONLexer3.bp + 1;
                            jSONLexer3.bp = i5;
                            JSONLexer jSONLexer4 = this.lexer;
                            char charAt = i5 >= jSONLexer4.len ? JSONLexer.EOI : this.lexer.text.charAt(i5);
                            jSONLexer4.ch = charAt;
                            collection.add(scanStringValue);
                            if (this.resolveStatus == 1) {
                                checkListResolve(collection);
                            }
                            if (charAt == '\"') {
                                i++;
                                c3 = ']';
                            } else {
                                this.lexer.nextToken();
                                z = false;
                            }
                        } else if (c == c3) {
                            JSONLexer jSONLexer5 = this.lexer;
                            int i6 = jSONLexer5.bp + 1;
                            jSONLexer5.bp = i6;
                            JSONLexer jSONLexer6 = this.lexer;
                            jSONLexer6.ch = i6 >= jSONLexer6.len ? JSONLexer.EOI : this.lexer.text.charAt(i6);
                            collection.add(scanStringValue);
                            if (this.resolveStatus == 1) {
                                checkListResolve(collection);
                            }
                            this.lexer.nextToken(16);
                            if (z2) {
                                return;
                            }
                            this.contex = parseContext;
                            return;
                        } else {
                            this.lexer.nextToken();
                        }
                    }
                    i2 = this.lexer.token;
                    while (i2 == 16) {
                        this.lexer.nextToken();
                        i2 = this.lexer.token;
                    }
                    JSONArray jSONArray = null;
                    jSONArray = null;
                    if (i2 != 2) {
                        integerValue = this.lexer.integerValue();
                        this.lexer.nextToken(16);
                    } else if (i2 != 3) {
                        if (i2 == 4) {
                            String stringVal = this.lexer.stringVal();
                            this.lexer.nextToken(16);
                            jSONArray = stringVal;
                            if ((this.lexer.features & Feature.AllowISO8601DateFormat.mask) != 0) {
                                JSONLexer jSONLexer7 = new JSONLexer(stringVal);
                                Date date = stringVal;
                                if (jSONLexer7.scanISO8601DateIfMatch(true)) {
                                    date = jSONLexer7.calendar.getTime();
                                }
                                jSONLexer7.close();
                                jSONArray = date;
                            }
                        } else if (i2 == 6) {
                            Boolean bool = Boolean.TRUE;
                            this.lexer.nextToken(16);
                            jSONArray = bool;
                        } else if (i2 == 7) {
                            Boolean bool2 = Boolean.FALSE;
                            this.lexer.nextToken(16);
                            jSONArray = bool2;
                        } else if (i2 == 8) {
                            this.lexer.nextToken(4);
                        } else if (i2 == 12) {
                            jSONArray = parseObject((this.lexer.features & Feature.OrderedField.mask) != 0 ? new JSONObject(new LinkedHashMap()) : new JSONObject(), Integer.valueOf(i));
                        } else if (i2 == 20) {
                            throw new JSONException("unclosed jsonArray");
                        } else {
                            if (i2 == 23) {
                                this.lexer.nextToken(4);
                            } else if (i2 == 14) {
                                JSONArray jSONArray2 = new JSONArray();
                                parseArray(jSONArray2, Integer.valueOf(i));
                                jSONArray = jSONArray2;
                            } else if (i2 == 15) {
                                this.lexer.nextToken(16);
                                if (z2) {
                                    return;
                                }
                                this.contex = parseContext;
                                return;
                            } else {
                                jSONArray = parse();
                            }
                        }
                        integerValue = jSONArray;
                    } else {
                        integerValue = (this.lexer.features & Feature.UseBigDecimal.mask) != 0 ? this.lexer.decimalValue(true) : this.lexer.decimalValue(false);
                        this.lexer.nextToken(16);
                    }
                    collection.add(integerValue);
                    if (this.resolveStatus == 1) {
                        checkListResolve(collection);
                    }
                    if (this.lexer.token == 16) {
                        char c4 = this.lexer.ch;
                        if (c4 == '\"') {
                            JSONLexer jSONLexer8 = this.lexer;
                            jSONLexer8.pos = jSONLexer8.bp;
                            this.lexer.scanString();
                        } else if (c4 < '0' || c4 > '9') {
                            if (c4 == '{') {
                                this.lexer.token = 12;
                                JSONLexer jSONLexer9 = this.lexer;
                                int i7 = jSONLexer9.bp + 1;
                                jSONLexer9.bp = i7;
                                JSONLexer jSONLexer10 = this.lexer;
                                jSONLexer10.ch = i7 >= jSONLexer10.len ? JSONLexer.EOI : this.lexer.text.charAt(i7);
                            } else {
                                this.lexer.nextToken();
                            }
                            i++;
                            c3 = ']';
                        } else {
                            JSONLexer jSONLexer11 = this.lexer;
                            jSONLexer11.pos = jSONLexer11.bp;
                            this.lexer.scanNumber();
                        }
                    }
                    i++;
                    c3 = ']';
                }
            } else {
                this.lexer.nextToken(4);
            }
            z = false;
            i = 0;
            while (true) {
                if (z) {
                    String scanStringValue2 = this.lexer.scanStringValue(TokenParser.DQUOTE);
                    c = this.lexer.ch;
                    if (c != ',') {
                    }
                }
                i2 = this.lexer.token;
                while (i2 == 16) {
                }
                JSONArray jSONArray3 = null;
                jSONArray3 = null;
                if (i2 != 2) {
                }
                collection.add(integerValue);
                if (this.resolveStatus == 1) {
                }
                if (this.lexer.token == 16) {
                }
                i++;
                c3 = ']';
            }
        } finally {
            if (!z2) {
                this.contex = parseContext;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addResolveTask(ResolveTask resolveTask) {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        this.resolveTaskList.add(resolveTask);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ResolveTask getLastResolveTask() {
        List<ResolveTask> list = this.resolveTaskList;
        return list.get(list.size() - 1);
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (this.extraProcessors == null) {
            this.extraProcessors = new ArrayList(2);
        }
        return this.extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (this.extraTypeProviders == null) {
            this.extraTypeProviders = new ArrayList(2);
        }
        return this.extraTypeProviders;
    }

    public void setContext(ParseContext parseContext) {
        if (this.lexer.disableCircularReferenceDetect) {
            return;
        }
        this.contex = parseContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void popContext() {
        this.contex = this.contex.parent;
        ParseContext[] parseContextArr = this.contextArray;
        int i = this.contextArrayIndex;
        parseContextArr[i - 1] = null;
        this.contextArrayIndex = i - 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ParseContext setContext(ParseContext parseContext, Object obj, Object obj2) {
        if (this.lexer.disableCircularReferenceDetect) {
            return null;
        }
        this.contex = new ParseContext(parseContext, obj, obj2);
        int i = this.contextArrayIndex;
        this.contextArrayIndex = i + 1;
        ParseContext[] parseContextArr = this.contextArray;
        if (parseContextArr == null) {
            this.contextArray = new ParseContext[8];
        } else if (i >= parseContextArr.length) {
            ParseContext[] parseContextArr2 = new ParseContext[(parseContextArr.length * 3) / 2];
            System.arraycopy(parseContextArr, 0, parseContextArr2, 0, parseContextArr.length);
            this.contextArray = parseContextArr2;
        }
        ParseContext[] parseContextArr3 = this.contextArray;
        ParseContext parseContext2 = this.contex;
        parseContextArr3[i] = parseContext2;
        return parseContext2;
    }

    public Object parse() {
        return parse(null);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public Object parse(Object obj) {
        int i = this.lexer.token;
        if (i == 2) {
            Number integerValue = this.lexer.integerValue();
            this.lexer.nextToken();
            return integerValue;
        }
        if (i == 3) {
            Number decimalValue = this.lexer.decimalValue((this.lexer.features & Feature.UseBigDecimal.mask) != 0);
            this.lexer.nextToken();
            return decimalValue;
        } else if (i == 4) {
            String stringVal = this.lexer.stringVal();
            this.lexer.nextToken(16);
            if ((this.lexer.features & Feature.AllowISO8601DateFormat.mask) != 0) {
                JSONLexer jSONLexer = new JSONLexer(stringVal);
                try {
                    if (jSONLexer.scanISO8601DateIfMatch(true)) {
                        return jSONLexer.calendar.getTime();
                    }
                } finally {
                    jSONLexer.close();
                }
            }
            return stringVal;
        } else if (i == 12) {
            return parseObject((this.lexer.features & Feature.OrderedField.mask) != 0 ? new JSONObject(new LinkedHashMap()) : new JSONObject(), obj);
        } else if (i == 14) {
            JSONArray jSONArray = new JSONArray();
            parseArray(jSONArray, obj);
            return jSONArray;
        } else {
            switch (i) {
                case 6:
                    this.lexer.nextToken(16);
                    return Boolean.TRUE;
                case 7:
                    this.lexer.nextToken(16);
                    return Boolean.FALSE;
                case 8:
                    break;
                case 9:
                    this.lexer.nextToken(18);
                    if (this.lexer.token != 18) {
                        throw new JSONException("syntax error, " + this.lexer.info());
                    }
                    this.lexer.nextToken(10);
                    accept(10);
                    long longValue = this.lexer.integerValue().longValue();
                    accept(2);
                    accept(11);
                    return new Date(longValue);
                default:
                    switch (i) {
                        case 20:
                            if (this.lexer.isBlankInput()) {
                                return null;
                            }
                            throw new JSONException("syntax error, " + this.lexer.info());
                        case 21:
                            this.lexer.nextToken();
                            HashSet hashSet = new HashSet();
                            parseArray(hashSet, obj);
                            return hashSet;
                        case 22:
                            this.lexer.nextToken();
                            TreeSet treeSet = new TreeSet();
                            parseArray(treeSet, obj);
                            return treeSet;
                        case 23:
                            break;
                        default:
                            throw new JSONException("syntax error, " + this.lexer.info());
                    }
            }
            this.lexer.nextToken();
            return null;
        }
    }

    public void config(Feature feature, boolean z) {
        this.lexer.config(feature, z);
    }

    public final void accept(int i) {
        if (this.lexer.token == i) {
            this.lexer.nextToken();
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(this.lexer.token));
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            if (this.lexer.token == 20) {
                return;
            }
            throw new JSONException("not close json text, token : " + JSONToken.name(this.lexer.token));
        } finally {
            this.lexer.close();
        }
    }

    public void handleResovleTask(Object obj) {
        List<ResolveTask> list = this.resolveTaskList;
        if (list == null) {
            return;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ResolveTask resolveTask = this.resolveTaskList.get(i);
            FieldDeserializer fieldDeserializer = resolveTask.fieldDeserializer;
            if (fieldDeserializer != null) {
                Object obj2 = null;
                Object obj3 = resolveTask.ownerContext != null ? resolveTask.ownerContext.object : null;
                String str = resolveTask.referenceValue;
                if (str.startsWith("$")) {
                    for (int i2 = 0; i2 < this.contextArrayIndex; i2++) {
                        if (str.equals(this.contextArray[i2].toString())) {
                            obj2 = this.contextArray[i2].object;
                        }
                    }
                } else {
                    obj2 = resolveTask.context.object;
                }
                fieldDeserializer.setValue(obj3, obj2);
            }
        }
    }

    public String parseString() {
        int i = this.lexer.token;
        if (i != 4) {
            if (i == 2) {
                String numberString = this.lexer.numberString();
                this.lexer.nextToken(16);
                return numberString;
            }
            Object parse = parse();
            if (parse == null) {
                return null;
            }
            return parse.toString();
        }
        String stringVal = this.lexer.stringVal();
        char c = this.lexer.ch;
        char c2 = JSONLexer.EOI;
        if (c == ',') {
            JSONLexer jSONLexer = this.lexer;
            int i2 = jSONLexer.bp + 1;
            jSONLexer.bp = i2;
            JSONLexer jSONLexer2 = this.lexer;
            if (i2 < jSONLexer2.len) {
                c2 = this.lexer.text.charAt(i2);
            }
            jSONLexer2.ch = c2;
            this.lexer.token = 16;
        } else if (this.lexer.ch == ']') {
            JSONLexer jSONLexer3 = this.lexer;
            int i3 = jSONLexer3.bp + 1;
            jSONLexer3.bp = i3;
            JSONLexer jSONLexer4 = this.lexer;
            if (i3 < jSONLexer4.len) {
                c2 = this.lexer.text.charAt(i3);
            }
            jSONLexer4.ch = c2;
            this.lexer.token = 15;
        } else if (this.lexer.ch == '}') {
            JSONLexer jSONLexer5 = this.lexer;
            int i4 = jSONLexer5.bp + 1;
            jSONLexer5.bp = i4;
            JSONLexer jSONLexer6 = this.lexer;
            if (i4 < jSONLexer6.len) {
                c2 = this.lexer.text.charAt(i4);
            }
            jSONLexer6.ch = c2;
            this.lexer.token = 13;
        } else {
            this.lexer.nextToken();
        }
        return stringVal;
    }

    /* loaded from: classes.dex */
    public static class ResolveTask {
        private final ParseContext context;
        public FieldDeserializer fieldDeserializer;
        public ParseContext ownerContext;
        private final String referenceValue;

        public ResolveTask(ParseContext parseContext, String str) {
            this.context = parseContext;
            this.referenceValue = str;
        }
    }
}
