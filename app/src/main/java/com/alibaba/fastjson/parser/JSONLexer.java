package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.http.message.TokenParser;

/* loaded from: classes.dex */
public final class JSONLexer {
    public static final char[] CA;
    public static final int END = 4;
    public static final char EOI = 26;
    static final int[] IA;
    public static final int NOT_MATCH = -1;
    public static final int NOT_MATCH_NAME = -2;
    public static final int UNKNOWN = 0;
    private static boolean V6 = false;
    public static final int VALUE = 3;
    protected static final int[] digits;
    public static final boolean[] firstIdentifierFlags;
    public static final boolean[] identifierFlags;
    private static final ThreadLocal<char[]> sbufLocal;
    protected int bp;
    public Calendar calendar;
    protected char ch;
    public boolean disableCircularReferenceDetect;
    protected int eofPos;
    protected boolean exp;
    public int features;
    protected long fieldHash;
    protected boolean hasSpecial;
    protected boolean isDouble;
    protected final int len;
    public Locale locale;
    public int matchStat;
    protected int np;
    protected int pos;
    protected char[] sbuf;
    protected int sp;
    protected String stringDefaultValue;
    protected final String text;
    public TimeZone timeZone;
    protected int token;

    static boolean checkDate(char c, char c2, char c3, char c4, char c5, char c6, int i, int i2) {
        if (c >= '1' && c <= '3' && c2 >= '0' && c2 <= '9' && c3 >= '0' && c3 <= '9' && c4 >= '0' && c4 <= '9') {
            if (c5 == '0') {
                if (c6 < '1' || c6 > '9') {
                    return false;
                }
            } else if (c5 != '1' || (c6 != '0' && c6 != '1' && c6 != '2')) {
                return false;
            }
            if (i == 48) {
                return i2 >= 49 && i2 <= 57;
            } else if (i != 49 && i != 50) {
                return i == 51 && (i2 == 48 || i2 == 49);
            } else if (i2 >= 48 && i2 <= 57) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x001d, code lost:
        if (r5 <= '4') goto L5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static boolean checkTime(char c, char c2, char c3, char c4, char c5, char c6) {
        if (c == '0') {
            if (c2 < '0' || c2 > '9') {
                return false;
            }
        } else if (c != '1') {
            if (c == '2') {
                if (c2 >= '0') {
                }
            }
            return false;
        } else if (c2 < '0' || c2 > '9') {
            return false;
        }
        if (c3 < '0' || c3 > '5') {
            if (c3 != '6' || c4 != '0') {
                return false;
            }
        } else if (c4 < '0' || c4 > '9') {
            return false;
        }
        return (c5 < '0' || c5 > '5') ? c5 == '6' && c6 == '0' : c6 >= '0' && c6 <= '9';
    }

    static {
        int i;
        try {
            i = Class.forName("android.os.Build$VERSION").getField("SDK_INT").getInt(null);
        } catch (Exception unused) {
            i = -1;
        }
        char c = 0;
        V6 = i >= 23;
        sbufLocal = new ThreadLocal<>();
        digits = new int[103];
        for (int i2 = 48; i2 <= 57; i2++) {
            digits[i2] = i2 - 48;
        }
        for (int i3 = 97; i3 <= 102; i3++) {
            digits[i3] = (i3 - 97) + 10;
        }
        for (int i4 = 65; i4 <= 70; i4++) {
            digits[i4] = (i4 - 65) + 10;
        }
        char[] charArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
        CA = charArray;
        int[] iArr = new int[256];
        IA = iArr;
        Arrays.fill(iArr, -1);
        int length = charArray.length;
        for (int i5 = 0; i5 < length; i5++) {
            IA[CA[i5]] = i5;
        }
        IA[61] = 0;
        firstIdentifierFlags = new boolean[256];
        char c2 = 0;
        while (true) {
            boolean[] zArr = firstIdentifierFlags;
            if (c2 >= zArr.length) {
                break;
            }
            if (c2 >= 'A' && c2 <= 'Z') {
                zArr[c2] = true;
            } else if (c2 >= 'a' && c2 <= 'z') {
                zArr[c2] = true;
            } else if (c2 == '_') {
                zArr[c2] = true;
            }
            c2 = (char) (c2 + 1);
        }
        identifierFlags = new boolean[256];
        while (true) {
            boolean[] zArr2 = identifierFlags;
            if (c >= zArr2.length) {
                return;
            }
            if (c >= 'A' && c <= 'Z') {
                zArr2[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                zArr2[c] = true;
            } else if (c == '_') {
                zArr2[c] = true;
            } else if (c >= '0' && c <= '9') {
                zArr2[c] = true;
            }
            c = (char) (c + 1);
        }
    }

    public JSONLexer(String str) {
        this(str, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONLexer(char[] cArr, int i) {
        this(cArr, i, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONLexer(char[] cArr, int i, int i2) {
        this(new String(cArr, 0, i), i2);
    }

    public JSONLexer(String str, int i) {
        this.features = JSON.DEFAULT_PARSER_FEATURE;
        this.exp = false;
        this.isDouble = false;
        this.timeZone = JSON.defaultTimeZone;
        this.locale = JSON.defaultLocale;
        this.calendar = null;
        this.matchStat = 0;
        char[] cArr = sbufLocal.get();
        this.sbuf = cArr;
        if (cArr == null) {
            this.sbuf = new char[512];
        }
        this.features = i;
        this.text = str;
        int length = str.length();
        this.len = length;
        int i2 = (-1) + 1;
        this.bp = i2;
        char charAt = i2 >= length ? EOI : str.charAt(i2);
        this.ch = charAt;
        if (charAt == 65279) {
            next();
        }
        this.stringDefaultValue = (Feature.InitStringFieldAsEmpty.mask & i) != 0 ? "" : null;
        this.disableCircularReferenceDetect = (Feature.DisableCircularReferenceDetect.mask & i) != 0;
    }

    public final int token() {
        return this.token;
    }

    public void close() {
        char[] cArr = this.sbuf;
        if (cArr.length <= 8196) {
            sbufLocal.set(cArr);
        }
        this.sbuf = null;
    }

    public char next() {
        int i = this.bp + 1;
        this.bp = i;
        char charAt = i >= this.len ? EOI : this.text.charAt(i);
        this.ch = charAt;
        return charAt;
    }

    public final void config(Feature feature, boolean z) {
        if (z) {
            this.features |= feature.mask;
        } else {
            this.features &= feature.mask ^ (-1);
        }
        if (feature == Feature.InitStringFieldAsEmpty) {
            this.stringDefaultValue = z ? "" : null;
        }
        this.disableCircularReferenceDetect = (this.features & Feature.DisableCircularReferenceDetect.mask) != 0;
    }

    public final boolean isEnabled(Feature feature) {
        return (feature.mask & this.features) != 0;
    }

    public final void nextTokenWithChar(char c) {
        this.sp = 0;
        while (true) {
            char c2 = this.ch;
            if (c2 == c) {
                int i = this.bp + 1;
                this.bp = i;
                this.ch = i >= this.len ? EOI : this.text.charAt(i);
                nextToken();
                return;
            } else if (c2 == ' ' || c2 == '\n' || c2 == '\r' || c2 == '\t' || c2 == '\f' || c2 == '\b') {
                next();
            } else {
                throw new JSONException("not match " + c + " - " + this.ch);
            }
        }
    }

    public final String numberString() {
        char charAt = this.text.charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
            i--;
        }
        return subString(this.np, i);
    }

    protected char charAt(int i) {
        return i >= this.len ? EOI : this.text.charAt(i);
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0027, code lost:
        scanNumber();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x002a, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0103, code lost:
        scanIdent();
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0106, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void nextToken() {
        int i;
        this.sp = 0;
        while (true) {
            int i2 = this.bp;
            this.pos = i2;
            char c = this.ch;
            if (c == '/') {
                skipComment();
            } else if (c == '\"') {
                scanString();
                return;
            } else if ((c < '0' || c > '9') && c != '-') {
                if (c == ',') {
                    next();
                    this.token = 16;
                    return;
                }
                if (c != '\f' && c != '\r' && c != ' ') {
                    if (c == ':') {
                        next();
                        this.token = 17;
                        return;
                    }
                    char c2 = EOI;
                    if (c == '[') {
                        int i3 = i2 + 1;
                        this.bp = i3;
                        if (i3 < this.len) {
                            c2 = this.text.charAt(i3);
                        }
                        this.ch = c2;
                        this.token = 14;
                        return;
                    } else if (c == ']') {
                        next();
                        this.token = 15;
                        return;
                    } else if (c == 'f') {
                        if (this.text.startsWith("false", i2)) {
                            int i4 = this.bp + 5;
                            this.bp = i4;
                            char charAt = charAt(i4);
                            this.ch = charAt;
                            if (charAt == ' ' || charAt == ',' || charAt == '}' || charAt == ']' || charAt == '\n' || charAt == '\r' || charAt == '\t' || charAt == 26 || charAt == '\f' || charAt == '\b' || charAt == ':') {
                                this.token = 7;
                                return;
                            }
                        }
                        throw new JSONException("scan false error");
                    } else if (c == 'n') {
                        if (this.text.startsWith("null", i2)) {
                            this.bp += 4;
                            i = 8;
                        } else if (this.text.startsWith("new", this.bp)) {
                            this.bp += 3;
                            i = 9;
                        } else {
                            i = 0;
                        }
                        if (i != 0) {
                            char charAt2 = charAt(this.bp);
                            this.ch = charAt2;
                            if (charAt2 == ' ' || charAt2 == ',' || charAt2 == '}' || charAt2 == ']' || charAt2 == '\n' || charAt2 == '\r' || charAt2 == '\t' || charAt2 == 26 || charAt2 == '\f' || charAt2 == '\b') {
                                this.token = i;
                                return;
                            }
                        }
                        throw new JSONException("scan null/new error");
                    } else if (c == '{') {
                        int i5 = i2 + 1;
                        this.bp = i5;
                        if (i5 < this.len) {
                            c2 = this.text.charAt(i5);
                        }
                        this.ch = c2;
                        this.token = 12;
                        return;
                    } else if (c == '}') {
                        int i6 = i2 + 1;
                        this.bp = i6;
                        if (i6 < this.len) {
                            c2 = this.text.charAt(i6);
                        }
                        this.ch = c2;
                        this.token = 13;
                        return;
                    } else if (c != 'S' && c != 'T') {
                        if (c == 't') {
                            if (this.text.startsWith("true", i2)) {
                                int i7 = this.bp + 4;
                                this.bp = i7;
                                char charAt3 = charAt(i7);
                                this.ch = charAt3;
                                if (charAt3 == ' ' || charAt3 == ',' || charAt3 == '}' || charAt3 == ']' || charAt3 == '\n' || charAt3 == '\r' || charAt3 == '\t' || charAt3 == 26 || charAt3 == '\f' || charAt3 == '\b' || charAt3 == ':') {
                                    this.token = 6;
                                    return;
                                }
                            }
                            throw new JSONException("scan true error");
                        } else if (c != 'u') {
                            switch (c) {
                                case '\b':
                                case '\t':
                                case '\n':
                                    break;
                                default:
                                    switch (c) {
                                        case '\'':
                                            scanString();
                                            return;
                                        case '(':
                                            next();
                                            this.token = 10;
                                            return;
                                        case ')':
                                            next();
                                            this.token = 11;
                                            return;
                                        default:
                                            int i8 = this.len;
                                            if (i2 == i8 || (c == 26 && i2 + 1 == i8)) {
                                                if (this.token == 20) {
                                                    throw new JSONException("EOF error");
                                                }
                                                this.token = 20;
                                                int i9 = this.eofPos;
                                                this.bp = i9;
                                                this.pos = i9;
                                                return;
                                            } else if (c <= 31 || c == 127) {
                                                next();
                                                break;
                                            } else {
                                                this.token = 1;
                                                next();
                                                return;
                                            }
                                            break;
                                    }
                            }
                        }
                    }
                }
                next();
            }
        }
    }

    public final void nextToken(int i) {
        this.sp = 0;
        while (true) {
            if (i != 2) {
                char c = EOI;
                if (i == 4) {
                    char c2 = this.ch;
                    if (c2 == '\"') {
                        this.pos = this.bp;
                        scanString();
                        return;
                    } else if (c2 >= '0' && c2 <= '9') {
                        this.pos = this.bp;
                        scanNumber();
                        return;
                    } else if (c2 == '{') {
                        this.token = 12;
                        int i2 = this.bp + 1;
                        this.bp = i2;
                        if (i2 < this.len) {
                            c = this.text.charAt(i2);
                        }
                        this.ch = c;
                        return;
                    }
                } else if (i == 12) {
                    char c3 = this.ch;
                    if (c3 == '{') {
                        this.token = 12;
                        int i3 = this.bp + 1;
                        this.bp = i3;
                        if (i3 < this.len) {
                            c = this.text.charAt(i3);
                        }
                        this.ch = c;
                        return;
                    } else if (c3 == '[') {
                        this.token = 14;
                        int i4 = this.bp + 1;
                        this.bp = i4;
                        if (i4 < this.len) {
                            c = this.text.charAt(i4);
                        }
                        this.ch = c;
                        return;
                    }
                } else if (i != 18) {
                    if (i != 20) {
                        switch (i) {
                            case 14:
                                char c4 = this.ch;
                                if (c4 == '[') {
                                    this.token = 14;
                                    next();
                                    return;
                                } else if (c4 == '{') {
                                    this.token = 12;
                                    next();
                                    return;
                                }
                                break;
                            case 15:
                                if (this.ch == ']') {
                                    this.token = 15;
                                    next();
                                    return;
                                }
                                break;
                            case 16:
                                char c5 = this.ch;
                                if (c5 == ',') {
                                    this.token = 16;
                                    int i5 = this.bp + 1;
                                    this.bp = i5;
                                    if (i5 < this.len) {
                                        c = this.text.charAt(i5);
                                    }
                                    this.ch = c;
                                    return;
                                } else if (c5 == '}') {
                                    this.token = 13;
                                    int i6 = this.bp + 1;
                                    this.bp = i6;
                                    if (i6 < this.len) {
                                        c = this.text.charAt(i6);
                                    }
                                    this.ch = c;
                                    return;
                                } else if (c5 == ']') {
                                    this.token = 15;
                                    int i7 = this.bp + 1;
                                    this.bp = i7;
                                    if (i7 < this.len) {
                                        c = this.text.charAt(i7);
                                    }
                                    this.ch = c;
                                    return;
                                } else if (c5 == 26) {
                                    this.token = 20;
                                    return;
                                }
                                break;
                        }
                    }
                    if (this.ch == 26) {
                        this.token = 20;
                        return;
                    }
                } else {
                    nextIdent();
                    return;
                }
            } else {
                char c6 = this.ch;
                if (c6 >= '0' && c6 <= '9') {
                    this.pos = this.bp;
                    scanNumber();
                    return;
                } else if (c6 == '\"') {
                    this.pos = this.bp;
                    scanString();
                    return;
                } else if (c6 == '[') {
                    this.token = 14;
                    next();
                    return;
                } else if (c6 == '{') {
                    this.token = 12;
                    next();
                    return;
                }
            }
            char c7 = this.ch;
            if (c7 == ' ' || c7 == '\n' || c7 == '\r' || c7 == '\t' || c7 == '\f' || c7 == '\b') {
                next();
            } else {
                nextToken();
                return;
            }
        }
    }

    public final void nextIdent() {
        char c;
        while (true) {
            c = this.ch;
            if (!(c <= ' ' && (c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b'))) {
                break;
            }
            next();
        }
        if (c == '_' || Character.isLetter(c)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    public final Number integerValue() throws NumberFormatException {
        char c;
        long j;
        boolean z;
        long j2;
        int i = this.np;
        int i2 = this.sp + i;
        int i3 = i2 - 1;
        char charAt = i3 >= this.len ? EOI : this.text.charAt(i3);
        if (charAt == 'B') {
            i2--;
            c = 'B';
        } else if (charAt == 'L') {
            i2--;
            c = 'L';
        } else if (charAt != 'S') {
            c = TokenParser.SP;
        } else {
            i2--;
            c = 'S';
        }
        int i4 = this.np;
        if ((i4 >= this.len ? EOI : this.text.charAt(i4)) == '-') {
            j = Long.MIN_VALUE;
            i++;
            z = true;
        } else {
            j = -9223372036854775807L;
            z = false;
        }
        if (i < i2) {
            j2 = -((i >= this.len ? EOI : this.text.charAt(i)) - '0');
            i++;
        } else {
            j2 = 0;
        }
        while (i < i2) {
            int i5 = i + 1;
            int charAt2 = (i >= this.len ? EOI : this.text.charAt(i)) - '0';
            if (j2 < -922337203685477580L) {
                return new BigInteger(numberString());
            }
            long j3 = j2 * 10;
            long j4 = charAt2;
            if (j3 < j + j4) {
                return new BigInteger(numberString());
            }
            j2 = j3 - j4;
            i = i5;
        }
        if (!z) {
            long j5 = -j2;
            if (j5 > 2147483647L || c == 'L') {
                return Long.valueOf(j5);
            }
            if (c == 'S') {
                return Short.valueOf((short) j5);
            }
            if (c == 'B') {
                return Byte.valueOf((byte) j5);
            }
            return Integer.valueOf((int) j5);
        } else if (i > this.np + 1) {
            if (j2 < -2147483648L || c == 'L') {
                return Long.valueOf(j2);
            }
            if (c == 'S') {
                return Short.valueOf((short) j2);
            }
            if (c == 'B') {
                return Byte.valueOf((byte) j2);
            }
            return Integer.valueOf((int) j2);
        } else {
            throw new NumberFormatException(numberString());
        }
    }

    public final String scanSymbol(SymbolTable symbolTable) {
        char c;
        while (true) {
            c = this.ch;
            if (c != ' ' && c != '\n' && c != '\r' && c != '\t' && c != '\f' && c != '\b') {
                break;
            }
            next();
        }
        if (c == '\"') {
            return scanSymbol(symbolTable, TokenParser.DQUOTE);
        }
        if (c == '\'') {
            return scanSymbol(symbolTable, '\'');
        }
        if (c == '}') {
            next();
            this.token = 13;
            return null;
        } else if (c == ',') {
            next();
            this.token = 16;
            return null;
        } else if (c == 26) {
            this.token = 20;
            return null;
        } else {
            return scanSymbolUnQuoted(symbolTable);
        }
    }

    public String scanSymbol(SymbolTable symbolTable, char c) {
        String readString;
        int i = this.bp + 1;
        int indexOf = this.text.indexOf(c, i);
        if (indexOf == -1) {
            throw new JSONException("unclosed str, " + info());
        }
        int i2 = indexOf - i;
        char[] sub_chars = sub_chars(this.bp + 1, i2);
        boolean z = false;
        while (i2 > 0 && sub_chars[i2 - 1] == '\\') {
            int i3 = 1;
            for (int i4 = i2 - 2; i4 >= 0 && sub_chars[i4] == '\\'; i4--) {
                i3++;
            }
            if (i3 % 2 == 0) {
                break;
            }
            int indexOf2 = this.text.indexOf(c, indexOf + 1);
            int i5 = (indexOf2 - indexOf) + i2;
            if (i5 >= sub_chars.length) {
                int length = (sub_chars.length * 3) / 2;
                if (length < i5) {
                    length = i5;
                }
                char[] cArr = new char[length];
                System.arraycopy(sub_chars, 0, cArr, 0, sub_chars.length);
                sub_chars = cArr;
            }
            this.text.getChars(indexOf, indexOf2, sub_chars, i2);
            indexOf = indexOf2;
            i2 = i5;
            z = true;
        }
        if (z) {
            readString = readString(sub_chars, i2);
        } else {
            int i6 = 0;
            for (int i7 = 0; i7 < i2; i7++) {
                char c2 = sub_chars[i7];
                i6 = (i6 * 31) + c2;
                if (c2 == '\\') {
                    z = true;
                }
            }
            if (z) {
                readString = readString(sub_chars, i2);
            } else {
                readString = i2 < 20 ? symbolTable.addSymbol(sub_chars, 0, i2, i6) : new String(sub_chars, 0, i2);
            }
        }
        int i8 = indexOf + 1;
        this.bp = i8;
        this.ch = i8 >= this.len ? EOI : this.text.charAt(i8);
        return readString;
    }

    private static String readString(char[] cArr, int i) {
        int i2;
        char[] cArr2 = new char[i];
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            char c = cArr[i3];
            if (c != '\\') {
                cArr2[i4] = c;
                i4++;
            } else {
                i3++;
                char c2 = cArr[i3];
                if (c2 == '\"') {
                    i2 = i4 + 1;
                    cArr2[i4] = TokenParser.DQUOTE;
                } else if (c2 != '\'') {
                    if (c2 != 'F') {
                        if (c2 == '\\') {
                            i2 = i4 + 1;
                            cArr2[i4] = TokenParser.ESCAPE;
                        } else if (c2 == 'b') {
                            i2 = i4 + 1;
                            cArr2[i4] = '\b';
                        } else if (c2 != 'f') {
                            if (c2 == 'n') {
                                i2 = i4 + 1;
                                cArr2[i4] = '\n';
                            } else if (c2 == 'r') {
                                i2 = i4 + 1;
                                cArr2[i4] = TokenParser.CR;
                            } else if (c2 != 'x') {
                                switch (c2) {
                                    case '/':
                                        i2 = i4 + 1;
                                        cArr2[i4] = '/';
                                        break;
                                    case '0':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 0;
                                        break;
                                    case '1':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 1;
                                        break;
                                    case '2':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 2;
                                        break;
                                    case '3':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 3;
                                        break;
                                    case '4':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 4;
                                        break;
                                    case '5':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 5;
                                        break;
                                    case '6':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 6;
                                        break;
                                    case '7':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 7;
                                        break;
                                    default:
                                        switch (c2) {
                                            case 't':
                                                i2 = i4 + 1;
                                                cArr2[i4] = '\t';
                                                break;
                                            case 'u':
                                                i2 = i4 + 1;
                                                int i5 = i3 + 1;
                                                int i6 = i5 + 1;
                                                int i7 = i6 + 1;
                                                i3 = i7 + 1;
                                                cArr2[i4] = (char) Integer.parseInt(new String(new char[]{cArr[i5], cArr[i6], cArr[i7], cArr[i3]}), 16);
                                                break;
                                            case 'v':
                                                i2 = i4 + 1;
                                                cArr2[i4] = 11;
                                                break;
                                            default:
                                                throw new JSONException("unclosed.str.lit");
                                        }
                                }
                            } else {
                                i2 = i4 + 1;
                                int[] iArr = digits;
                                int i8 = i3 + 1;
                                i3 = i8 + 1;
                                cArr2[i4] = (char) ((iArr[cArr[i8]] * 16) + iArr[cArr[i3]]);
                            }
                        }
                    }
                    i2 = i4 + 1;
                    cArr2[i4] = '\f';
                } else {
                    i2 = i4 + 1;
                    cArr2[i4] = '\'';
                }
                i4 = i2;
            }
            i3++;
        }
        return new String(cArr2, 0, i4);
    }

    public String info() {
        StringBuilder sb = new StringBuilder();
        sb.append("pos ");
        sb.append(this.bp);
        sb.append(", json : ");
        sb.append(this.len < 65536 ? this.text : this.text.substring(0, 65536));
        return sb.toString();
    }

    protected void skipComment() {
        next();
        char c = this.ch;
        if (c == '/') {
            do {
                next();
            } while (this.ch != '\n');
            next();
        } else if (c == '*') {
            next();
            while (true) {
                char c2 = this.ch;
                if (c2 == 26) {
                    return;
                }
                if (c2 == '*') {
                    next();
                    if (this.ch == '/') {
                        next();
                        return;
                    }
                } else {
                    next();
                }
            }
        } else {
            throw new JSONException("invalid comment");
        }
    }

    public final String scanSymbolUnQuoted(SymbolTable symbolTable) {
        int i = this.ch;
        boolean[] zArr = firstIdentifierFlags;
        if (!(i >= zArr.length || zArr[i])) {
            throw new JSONException("illegal identifier : " + this.ch + ", " + info());
        }
        this.np = this.bp;
        this.sp = 1;
        while (true) {
            char next = next();
            boolean[] zArr2 = identifierFlags;
            if (next < zArr2.length && !zArr2[next]) {
                break;
            }
            i = (i * 31) + next;
            this.sp++;
        }
        this.ch = charAt(this.bp);
        this.token = 18;
        if (this.sp == 4 && this.text.startsWith("null", this.np)) {
            return null;
        }
        return symbolTable.addSymbol(this.text, this.np, this.sp, i);
    }

    public final void scanString() {
        char c = this.ch;
        int i = this.bp + 1;
        int indexOf = this.text.indexOf(c, i);
        if (indexOf == -1) {
            throw new JSONException("unclosed str, " + info());
        }
        int i2 = indexOf - i;
        char[] sub_chars = sub_chars(this.bp + 1, i2);
        boolean z = false;
        while (i2 > 0 && sub_chars[i2 - 1] == '\\') {
            int i3 = 1;
            for (int i4 = i2 - 2; i4 >= 0 && sub_chars[i4] == '\\'; i4--) {
                i3++;
            }
            if (i3 % 2 == 0) {
                break;
            }
            int indexOf2 = this.text.indexOf(c, indexOf + 1);
            int i5 = (indexOf2 - indexOf) + i2;
            if (i5 >= sub_chars.length) {
                int length = (sub_chars.length * 3) / 2;
                if (length < i5) {
                    length = i5;
                }
                char[] cArr = new char[length];
                System.arraycopy(sub_chars, 0, cArr, 0, sub_chars.length);
                sub_chars = cArr;
            }
            this.text.getChars(indexOf, indexOf2, sub_chars, i2);
            indexOf = indexOf2;
            i2 = i5;
            z = true;
        }
        if (!z) {
            for (int i6 = 0; i6 < i2; i6++) {
                if (sub_chars[i6] == '\\') {
                    z = true;
                }
            }
        }
        this.sbuf = sub_chars;
        this.sp = i2;
        this.np = this.bp;
        this.hasSpecial = z;
        int i7 = indexOf + 1;
        this.bp = i7;
        this.ch = i7 >= this.len ? EOI : this.text.charAt(i7);
        this.token = 4;
    }

    public String scanStringValue(char c) {
        String str;
        int i = this.bp + 1;
        int indexOf = this.text.indexOf(c, i);
        if (indexOf == -1) {
            throw new JSONException("unclosed str, " + info());
        }
        if (V6) {
            str = this.text.substring(i, indexOf);
        } else {
            int i2 = indexOf - i;
            str = new String(sub_chars(this.bp + 1, i2), 0, i2);
        }
        if (str.indexOf(92) != -1) {
            while (true) {
                int i3 = 0;
                for (int i4 = indexOf - 1; i4 >= 0 && this.text.charAt(i4) == '\\'; i4--) {
                    i3++;
                }
                if (i3 % 2 == 0) {
                    break;
                }
                indexOf = this.text.indexOf(c, indexOf + 1);
            }
            int i5 = indexOf - i;
            str = readString(sub_chars(this.bp + 1, i5), i5);
        }
        int i6 = indexOf + 1;
        this.bp = i6;
        this.ch = i6 >= this.len ? EOI : this.text.charAt(i6);
        return str;
    }

    public final int intValue() {
        int i;
        boolean z;
        int i2 = this.np;
        int i3 = this.sp + i2;
        int i4 = 0;
        if ((i2 >= this.len ? EOI : this.text.charAt(i2)) == '-') {
            i = Integer.MIN_VALUE;
            i2++;
            z = true;
        } else {
            i = -2147483647;
            z = false;
        }
        if (i2 < i3) {
            i4 = -((i2 >= this.len ? EOI : this.text.charAt(i2)) - '0');
            i2++;
        }
        while (i2 < i3) {
            int i5 = i2 + 1;
            char charAt = i2 >= this.len ? EOI : this.text.charAt(i2);
            if (charAt == 'L' || charAt == 'S' || charAt == 'B') {
                i2 = i5;
                break;
            }
            int i6 = charAt - '0';
            if (i4 < -214748364) {
                throw new NumberFormatException(numberString());
            }
            int i7 = i4 * 10;
            if (i7 < i + i6) {
                throw new NumberFormatException(numberString());
            }
            i4 = i7 - i6;
            i2 = i5;
        }
        if (z) {
            if (i2 > this.np + 1) {
                return i4;
            }
            throw new NumberFormatException(numberString());
        }
        return -i4;
    }

    public byte[] bytesValue() {
        return decodeFast(this.text, this.np + 1, this.sp);
    }

    private void scanIdent() {
        this.np = this.bp - 1;
        this.hasSpecial = false;
        do {
            this.sp++;
            next();
        } while (Character.isLetterOrDigit(this.ch));
        String stringVal = stringVal();
        if (stringVal.equals("null")) {
            this.token = 8;
        } else if (stringVal.equals("true")) {
            this.token = 6;
        } else if (stringVal.equals("false")) {
            this.token = 7;
        } else if (stringVal.equals("new")) {
            this.token = 9;
        } else if (stringVal.equals("undefined")) {
            this.token = 23;
        } else if (stringVal.equals("Set")) {
            this.token = 21;
        } else if (stringVal.equals("TreeSet")) {
            this.token = 22;
        } else {
            this.token = 18;
        }
    }

    public final String stringVal() {
        if (this.hasSpecial) {
            return readString(this.sbuf, this.sp);
        }
        return subString(this.np + 1, this.sp);
    }

    private final String subString(int i, int i2) {
        char[] cArr = this.sbuf;
        if (i2 < cArr.length) {
            this.text.getChars(i, i + i2, cArr, 0);
            return new String(this.sbuf, 0, i2);
        }
        char[] cArr2 = new char[i2];
        this.text.getChars(i, i2 + i, cArr2, 0);
        return new String(cArr2);
    }

    final char[] sub_chars(int i, int i2) {
        char[] cArr = this.sbuf;
        if (i2 < cArr.length) {
            this.text.getChars(i, i2 + i, cArr, 0);
            return this.sbuf;
        }
        char[] cArr2 = new char[i2];
        this.sbuf = cArr2;
        this.text.getChars(i, i2 + i, cArr2, 0);
        return cArr2;
    }

    public final boolean isBlankInput() {
        int i = 0;
        while (true) {
            char charAt = charAt(i);
            boolean z = true;
            if (charAt == 26) {
                return true;
            }
            if (charAt > ' ' || (charAt != ' ' && charAt != '\n' && charAt != '\r' && charAt != '\t' && charAt != '\f' && charAt != '\b')) {
                z = false;
            }
            if (!z) {
                return false;
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void skipWhitespace() {
        while (true) {
            char c = this.ch;
            if (c > '/') {
                return;
            }
            if (c == ' ' || c == '\r' || c == '\n' || c == '\t' || c == '\f' || c == '\b') {
                next();
            } else if (c != '/') {
                return;
            } else {
                skipComment();
            }
        }
    }

    public final void scanNumber() {
        char c;
        char c2;
        int i = this.bp;
        this.np = i;
        this.exp = false;
        if (this.ch == '-') {
            this.sp++;
            int i2 = i + 1;
            this.bp = i2;
            this.ch = i2 >= this.len ? EOI : this.text.charAt(i2);
        }
        while (true) {
            c = this.ch;
            if (c < '0' || c > '9') {
                break;
            }
            this.sp++;
            int i3 = this.bp + 1;
            this.bp = i3;
            this.ch = i3 >= this.len ? EOI : this.text.charAt(i3);
        }
        this.isDouble = false;
        if (c == '.') {
            this.sp++;
            int i4 = this.bp + 1;
            this.bp = i4;
            this.ch = i4 >= this.len ? EOI : this.text.charAt(i4);
            this.isDouble = true;
            while (true) {
                char c3 = this.ch;
                if (c3 < '0' || c3 > '9') {
                    break;
                }
                this.sp++;
                int i5 = this.bp + 1;
                this.bp = i5;
                this.ch = i5 >= this.len ? EOI : this.text.charAt(i5);
            }
        }
        char c4 = this.ch;
        if (c4 == 'L') {
            this.sp++;
            next();
        } else if (c4 == 'S') {
            this.sp++;
            next();
        } else if (c4 == 'B') {
            this.sp++;
            next();
        } else if (c4 == 'F') {
            this.sp++;
            next();
            this.isDouble = true;
        } else if (c4 == 'D') {
            this.sp++;
            next();
            this.isDouble = true;
        } else if (c4 == 'e' || c4 == 'E') {
            this.sp++;
            int i6 = this.bp + 1;
            this.bp = i6;
            char charAt = i6 >= this.len ? EOI : this.text.charAt(i6);
            this.ch = charAt;
            if (charAt == '+' || charAt == '-') {
                this.sp++;
                int i7 = this.bp + 1;
                this.bp = i7;
                this.ch = i7 >= this.len ? EOI : this.text.charAt(i7);
            }
            while (true) {
                c2 = this.ch;
                if (c2 < '0' || c2 > '9') {
                    break;
                }
                this.sp++;
                int i8 = this.bp + 1;
                this.bp = i8;
                this.ch = i8 >= this.len ? EOI : this.text.charAt(i8);
            }
            if (c2 == 'D' || c2 == 'F') {
                this.sp++;
                next();
            }
            this.exp = true;
            this.isDouble = true;
        }
        if (this.isDouble) {
            this.token = 3;
        } else {
            this.token = 2;
        }
    }

    public boolean scanBoolean() {
        boolean z = false;
        int i = 1;
        if (this.text.startsWith("false", this.bp)) {
            i = 5;
        } else if (this.text.startsWith("true", this.bp)) {
            z = true;
            i = 4;
        } else {
            char c = this.ch;
            if (c == '1') {
                z = true;
            } else if (c != '0') {
                this.matchStat = -1;
                return false;
            }
        }
        int i2 = this.bp + i;
        this.bp = i2;
        this.ch = charAt(i2);
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x020d A[Catch: NumberFormatException -> 0x025b, TryCatch #0 {NumberFormatException -> 0x025b, blocks: (B:118:0x01fd, B:124:0x0209, B:126:0x020d, B:132:0x021e, B:129:0x0215, B:131:0x021c, B:135:0x0225, B:138:0x022b, B:143:0x023c, B:123:0x0206, B:145:0x0241, B:147:0x024b, B:149:0x0251), top: B:156:0x01f9 }] */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0225 A[Catch: NumberFormatException -> 0x025b, TryCatch #0 {NumberFormatException -> 0x025b, blocks: (B:118:0x01fd, B:124:0x0209, B:126:0x020d, B:132:0x021e, B:129:0x0215, B:131:0x021c, B:135:0x0225, B:138:0x022b, B:143:0x023c, B:123:0x0206, B:145:0x0241, B:147:0x024b, B:149:0x0251), top: B:156:0x01f9 }] */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x024b A[Catch: NumberFormatException -> 0x025b, TryCatch #0 {NumberFormatException -> 0x025b, blocks: (B:118:0x01fd, B:124:0x0209, B:126:0x020d, B:132:0x021e, B:129:0x0215, B:131:0x021c, B:135:0x0225, B:138:0x022b, B:143:0x023c, B:123:0x0206, B:145:0x0241, B:147:0x024b, B:149:0x0251), top: B:156:0x01f9 }] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0251 A[Catch: NumberFormatException -> 0x025b, TRY_LEAVE, TryCatch #0 {NumberFormatException -> 0x025b, blocks: (B:118:0x01fd, B:124:0x0209, B:126:0x020d, B:132:0x021e, B:129:0x0215, B:131:0x021c, B:135:0x0225, B:138:0x022b, B:143:0x023c, B:123:0x0206, B:145:0x0241, B:147:0x024b, B:149:0x0251), top: B:156:0x01f9 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0150  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0153  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Number scanNumberValue() {
        long j;
        boolean z;
        char c;
        boolean z2;
        Number number;
        Number valueOf;
        boolean z3;
        char c2;
        char charAt;
        char c3;
        boolean z4;
        int i;
        char[] cArr;
        int i2;
        char[] cArr2;
        Number valueOf2;
        int i3;
        int i4 = this.bp;
        this.np = 0;
        if (this.ch == '-') {
            j = Long.MIN_VALUE;
            this.np = 0 + 1;
            int i5 = i4 + 1;
            this.bp = i5;
            this.ch = i5 >= this.len ? EOI : this.text.charAt(i5);
            z = true;
        } else {
            j = -9223372036854775807L;
            z = false;
        }
        long j2 = 0;
        boolean z5 = false;
        while (true) {
            c = this.ch;
            if (c < '0' || c > '9') {
                break;
            }
            int i6 = c - '0';
            if (j2 < -922337203685477580L) {
                z5 = true;
            }
            long j3 = j2 * 10;
            long j4 = i6;
            if (j3 < j + j4) {
                z5 = true;
            }
            j2 = j3 - j4;
            this.np++;
            int i7 = this.bp + 1;
            this.bp = i7;
            this.ch = i7 >= this.len ? EOI : this.text.charAt(i7);
        }
        if (!z) {
            j2 = -j2;
        }
        try {
            if (c == 'L') {
                this.np++;
                next();
                valueOf = Long.valueOf(j2);
            } else if (c == 'S') {
                this.np++;
                next();
                valueOf = Short.valueOf((short) j2);
            } else if (c == 'B') {
                this.np++;
                next();
                valueOf = Byte.valueOf((byte) j2);
            } else if (c == 'F') {
                this.np++;
                next();
                valueOf = Float.valueOf((float) j2);
            } else {
                if (c == 'D') {
                    this.np++;
                    next();
                    z2 = z;
                    number = Double.valueOf(j2);
                } else {
                    z2 = z;
                    number = null;
                }
                if (this.ch != '.') {
                    this.np++;
                    int i8 = this.bp + 1;
                    this.bp = i8;
                    this.ch = i8 >= this.len ? EOI : this.text.charAt(i8);
                    while (true) {
                        char c4 = this.ch;
                        if (c4 < '0' || c4 > '9') {
                            break;
                        }
                        this.np++;
                        int i9 = this.bp + 1;
                        this.bp = i9;
                        this.ch = i9 >= this.len ? EOI : this.text.charAt(i9);
                    }
                    z3 = true;
                } else {
                    z3 = false;
                }
                c2 = this.ch;
                if (c2 != 'e' || c2 == 'E') {
                    this.np++;
                    int i10 = this.bp + 1;
                    this.bp = i10;
                    charAt = i10 < this.len ? EOI : this.text.charAt(i10);
                    this.ch = charAt;
                    if (charAt != '+' || charAt == '-') {
                        this.np++;
                        int i11 = this.bp + 1;
                        this.bp = i11;
                        this.ch = i11 < this.len ? EOI : this.text.charAt(i11);
                    }
                    while (true) {
                        c3 = this.ch;
                        if (c3 < '0' || c3 > '9') {
                            break;
                        }
                        this.np++;
                        int i12 = this.bp + 1;
                        this.bp = i12;
                        this.ch = i12 >= this.len ? EOI : this.text.charAt(i12);
                    }
                    if (c3 != 'D' || c3 == 'F') {
                        this.np++;
                        next();
                    } else {
                        c3 = 0;
                    }
                    z4 = true;
                } else {
                    c3 = 0;
                    z4 = false;
                }
                if (z3 && !z4) {
                    if (z5) {
                        int i13 = this.bp;
                        char[] cArr3 = new char[i13 - i4];
                        this.text.getChars(i4, i13, cArr3, 0);
                        number = new BigInteger(new String(cArr3));
                    }
                    if (number == null) {
                        if (j2 > -2147483648L && j2 < 2147483647L) {
                            return Integer.valueOf((int) j2);
                        }
                        return Long.valueOf(j2);
                    }
                    return number;
                }
                i = this.bp - i4;
                if (c3 != 0) {
                    i--;
                }
                cArr = this.sbuf;
                if (i >= cArr.length) {
                    i2 = 0;
                    this.text.getChars(i4, i4 + i, cArr, 0);
                    cArr2 = this.sbuf;
                } else {
                    i2 = 0;
                    char[] cArr4 = new char[i];
                    this.text.getChars(i4, i4 + i, cArr4, 0);
                    cArr2 = cArr4;
                }
                if (z4 && (this.features & Feature.UseBigDecimal.mask) != 0) {
                    return new BigDecimal(cArr2, i2, i);
                }
                if (i > 9 && !z4) {
                    char c5 = cArr2[i2];
                    if (c5 != '-' && c5 != '+') {
                        i3 = 1;
                        int i14 = c5 - '0';
                        int i15 = 0;
                        while (i3 < i) {
                            char c6 = cArr2[i3];
                            if (c6 == '.') {
                                i15 = 1;
                            } else {
                                i14 = (i14 * 10) + (c6 - '0');
                                if (i15 != 0) {
                                    i15 *= 10;
                                }
                            }
                            i3++;
                        }
                        if (c3 != 'F') {
                            float f = i14 / i15;
                            if (z2) {
                                f = -f;
                            }
                            return Float.valueOf(f);
                        }
                        double d = i14;
                        double d2 = i15;
                        Double.isNaN(d);
                        Double.isNaN(d2);
                        double d3 = d / d2;
                        if (z2) {
                            d3 = -d3;
                        }
                        return Double.valueOf(d3);
                    }
                    i3 = 2;
                    c5 = cArr2[1];
                    int i142 = c5 - '0';
                    int i152 = 0;
                    while (i3 < i) {
                    }
                    if (c3 != 'F') {
                    }
                } else {
                    String str = new String(cArr2, 0, i);
                    if (c3 != 'F') {
                        valueOf2 = Float.valueOf(str);
                    } else {
                        valueOf2 = Double.valueOf(Double.parseDouble(str));
                    }
                    return valueOf2;
                }
            }
            if (i > 9) {
            }
            String str2 = new String(cArr2, 0, i);
            if (c3 != 'F') {
            }
            return valueOf2;
        } catch (NumberFormatException e) {
            throw new JSONException(e.getMessage() + ", " + info(), e);
        }
        z2 = z;
        number = valueOf;
        if (this.ch != '.') {
        }
        c2 = this.ch;
        if (c2 != 'e') {
        }
        this.np++;
        int i102 = this.bp + 1;
        this.bp = i102;
        if (i102 < this.len) {
        }
        this.ch = charAt;
        if (charAt != '+') {
        }
        this.np++;
        int i112 = this.bp + 1;
        this.bp = i112;
        this.ch = i112 < this.len ? EOI : this.text.charAt(i112);
        while (true) {
            c3 = this.ch;
            if (c3 < '0') {
                break;
            }
            break;
            this.ch = i12 >= this.len ? EOI : this.text.charAt(i12);
        }
        if (c3 != 'D') {
        }
        this.np++;
        next();
        z4 = true;
        if (z3) {
        }
        i = this.bp - i4;
        if (c3 != 0) {
        }
        cArr = this.sbuf;
        if (i >= cArr.length) {
        }
        if (z4) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00c3, code lost:
        if (r0 != false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00c6, code lost:
        return -r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:?, code lost:
        return r5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final long scanLongValue() {
        long j;
        boolean z = false;
        this.np = 0;
        if (this.ch == '-') {
            this.np = 0 + 1;
            int i = this.bp + 1;
            this.bp = i;
            if (i >= this.len) {
                throw new JSONException("syntax error, " + info());
            }
            this.ch = this.text.charAt(i);
            j = Long.MIN_VALUE;
            z = true;
        } else {
            j = -9223372036854775807L;
        }
        long j2 = 0;
        while (true) {
            char c = this.ch;
            if (c < '0' || c > '9') {
                break;
            }
            int i2 = c - '0';
            if (j2 < -922337203685477580L) {
                throw new JSONException("error long value, " + j2 + ", " + info());
            }
            long j3 = j2 * 10;
            long j4 = i2;
            if (j3 < j + j4) {
                throw new JSONException("error long value, " + j3 + ", " + info());
            }
            j2 = j3 - j4;
            this.np++;
            int i3 = this.bp + 1;
            this.bp = i3;
            this.ch = i3 >= this.len ? EOI : this.text.charAt(i3);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0087  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x005e -> B:8:0x0026). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final long longValue() throws NumberFormatException {
        long j;
        boolean z;
        long j2;
        int i = this.np;
        int i2 = this.sp + i;
        if (charAt(i) == '-') {
            j = Long.MIN_VALUE;
            i++;
            z = true;
        } else {
            j = -9223372036854775807L;
            z = false;
        }
        if (i < i2) {
            int i3 = i + 1;
            j2 = -(charAt(i) - '0');
            i = i3;
            if (i < i2) {
                i3 = i + 1;
                char charAt = i >= this.len ? EOI : this.text.charAt(i);
                if (charAt == 'L' || charAt == 'S' || charAt == 'B') {
                    i = i3;
                } else {
                    int i4 = charAt - '0';
                    if (j2 < -922337203685477580L) {
                        throw new NumberFormatException(numberString());
                    }
                    long j3 = j2 * 10;
                    long j4 = i4;
                    if (j3 < j + j4) {
                        throw new NumberFormatException(numberString());
                    }
                    j2 = j3 - j4;
                    i = i3;
                    if (i < i2) {
                    }
                }
            }
            if (z) {
                return -j2;
            }
            if (i > this.np + 1) {
                return j2;
            }
            throw new NumberFormatException(numberString());
        }
        j2 = 0;
        if (i < i2) {
        }
        if (z) {
        }
    }

    public final Number decimalValue(boolean z) {
        char[] cArr;
        boolean z2;
        int i = (this.np + this.sp) - 1;
        char charAt = i >= this.len ? EOI : this.text.charAt(i);
        try {
            if (charAt == 'F') {
                return Float.valueOf(Float.parseFloat(numberString()));
            }
            if (charAt == 'D') {
                return Double.valueOf(Double.parseDouble(numberString()));
            }
            if (z) {
                return decimalValue();
            }
            char charAt2 = this.text.charAt((this.np + this.sp) - 1);
            int i2 = this.sp;
            if (charAt2 == 'L' || charAt2 == 'S' || charAt2 == 'B' || charAt2 == 'F' || charAt2 == 'D') {
                i2--;
            }
            int i3 = this.np;
            char[] cArr2 = this.sbuf;
            int i4 = 0;
            if (i2 < cArr2.length) {
                this.text.getChars(i3, i3 + i2, cArr2, 0);
                cArr = this.sbuf;
            } else {
                char[] cArr3 = new char[i2];
                this.text.getChars(i3, i3 + i2, cArr3, 0);
                cArr = cArr3;
            }
            if (i2 <= 9 && !this.exp) {
                char c = cArr[0];
                int i5 = 2;
                if (c == '-') {
                    c = cArr[1];
                    z2 = true;
                } else if (c == '+') {
                    c = cArr[1];
                    z2 = false;
                } else {
                    z2 = false;
                    i5 = 1;
                }
                int i6 = c - '0';
                while (i5 < i2) {
                    char c2 = cArr[i5];
                    if (c2 == '.') {
                        i4 = 1;
                    } else {
                        i6 = (i6 * 10) + (c2 - '0');
                        if (i4 != 0) {
                            i4 *= 10;
                        }
                    }
                    i5++;
                }
                double d = i6;
                double d2 = i4;
                Double.isNaN(d);
                Double.isNaN(d2);
                double d3 = d / d2;
                if (z2) {
                    d3 = -d3;
                }
                return Double.valueOf(d3);
            }
            return Double.valueOf(Double.parseDouble(new String(cArr, 0, i2)));
        } catch (NumberFormatException e) {
            throw new JSONException(e.getMessage() + ", " + info());
        }
    }

    public final BigDecimal decimalValue() {
        char charAt = this.text.charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
            i--;
        }
        int i2 = this.np;
        char[] cArr = this.sbuf;
        if (i < cArr.length) {
            this.text.getChars(i2, i2 + i, cArr, 0);
            return new BigDecimal(this.sbuf, 0, i);
        }
        char[] cArr2 = new char[i];
        this.text.getChars(i2, i + i2, cArr2, 0);
        return new BigDecimal(cArr2);
    }

    public boolean matchField(long j) {
        char c = this.ch;
        int i = this.bp + 1;
        int i2 = 1;
        while (c != '\"' && c != '\'') {
            if (c <= ' ' && (c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b')) {
                int i3 = i2 + 1;
                int i4 = this.bp + i2;
                c = i4 >= this.len ? EOI : this.text.charAt(i4);
                i2 = i3;
            } else {
                this.fieldHash = 0L;
                this.matchStat = -2;
                return false;
            }
        }
        int i5 = i;
        long j2 = -3750763034362895579L;
        while (true) {
            if (i5 >= this.len) {
                break;
            }
            char charAt = this.text.charAt(i5);
            if (charAt == c) {
                i2 += (i5 - i) + 1;
                break;
            }
            j2 = 1099511628211L * (charAt ^ j2);
            i5++;
        }
        if (j2 != j) {
            this.matchStat = -2;
            this.fieldHash = j2;
            return false;
        }
        int i6 = i2 + 1;
        int i7 = this.bp + i2;
        char charAt2 = i7 >= this.len ? EOI : this.text.charAt(i7);
        while (charAt2 != ':') {
            if (charAt2 <= ' ' && (charAt2 == ' ' || charAt2 == '\n' || charAt2 == '\r' || charAt2 == '\t' || charAt2 == '\f' || charAt2 == '\b')) {
                int i8 = i6 + 1;
                int i9 = this.bp + i6;
                charAt2 = i9 >= this.len ? EOI : this.text.charAt(i9);
                i6 = i8;
            } else {
                throw new JSONException("match feild error expect ':'");
            }
        }
        int i10 = this.bp + i6;
        char charAt3 = i10 >= this.len ? EOI : this.text.charAt(i10);
        if (charAt3 == '{') {
            int i11 = i10 + 1;
            this.bp = i11;
            this.ch = i11 >= this.len ? EOI : this.text.charAt(i11);
            this.token = 12;
        } else if (charAt3 == '[') {
            int i12 = i10 + 1;
            this.bp = i12;
            this.ch = i12 >= this.len ? EOI : this.text.charAt(i12);
            this.token = 14;
        } else {
            this.bp = i10;
            this.ch = i10 >= this.len ? EOI : this.text.charAt(i10);
            nextToken();
        }
        return true;
    }

    private int matchFieldHash(long j) {
        char c = this.ch;
        int i = 1;
        while (c != '\"' && c != '\'') {
            if (c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b') {
                int i2 = i + 1;
                int i3 = this.bp + i;
                c = i3 >= this.len ? EOI : this.text.charAt(i3);
                i = i2;
            } else {
                this.fieldHash = 0L;
                this.matchStat = -2;
                return 0;
            }
        }
        long j2 = -3750763034362895579L;
        int i4 = this.bp + i;
        while (true) {
            if (i4 >= this.len) {
                break;
            }
            char charAt = this.text.charAt(i4);
            if (charAt == c) {
                i += (i4 - this.bp) - i;
                break;
            }
            j2 = 1099511628211L * (charAt ^ j2);
            i4++;
        }
        if (j2 != j) {
            this.fieldHash = j2;
            this.matchStat = -2;
            return 0;
        }
        int i5 = i + 1;
        int i6 = this.bp + i5;
        char charAt2 = i6 >= this.len ? EOI : this.text.charAt(i6);
        while (charAt2 != ':') {
            if (charAt2 <= ' ' && (charAt2 == ' ' || charAt2 == '\n' || charAt2 == '\r' || charAt2 == '\t' || charAt2 == '\f' || charAt2 == '\b')) {
                int i7 = i5 + 1;
                int i8 = this.bp + i5;
                charAt2 = i8 >= this.len ? EOI : this.text.charAt(i8);
                i5 = i7;
            } else {
                throw new JSONException("match feild error expect ':'");
            }
        }
        return i5 + 1;
    }

    public int scanFieldInt(long j) {
        int i;
        char charAt;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return 0;
        }
        int i2 = matchFieldHash + 1;
        int i3 = this.bp + matchFieldHash;
        int i4 = this.len;
        char c = EOI;
        char charAt2 = i3 >= i4 ? EOI : this.text.charAt(i3);
        boolean z = charAt2 == '\"';
        if (z) {
            int i5 = i2 + 1;
            int i6 = this.bp + i2;
            charAt2 = i6 >= this.len ? EOI : this.text.charAt(i6);
            i2 = i5;
            z = true;
        }
        boolean z2 = charAt2 == '-';
        if (z2) {
            int i7 = i2 + 1;
            int i8 = this.bp + i2;
            charAt2 = i8 >= this.len ? EOI : this.text.charAt(i8);
            i2 = i7;
        }
        if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        int i9 = charAt2 - '0';
        while (true) {
            i = i2 + 1;
            int i10 = this.bp + i2;
            charAt = i10 >= this.len ? EOI : this.text.charAt(i10);
            if (charAt < '0' || charAt > '9') {
                break;
            }
            i9 = (i9 * 10) + (charAt - '0');
            i2 = i;
        }
        if (charAt == '.') {
            this.matchStat = -1;
            return 0;
        }
        if (charAt == '\"') {
            if (!z) {
                this.matchStat = -1;
                return 0;
            }
            int i11 = i + 1;
            int i12 = this.bp + i;
            i = i11;
            charAt = i12 >= this.len ? EOI : this.text.charAt(i12);
        }
        if (i9 < 0) {
            this.matchStat = -1;
            return 0;
        }
        while (charAt != ',') {
            if (charAt > ' ' || !(charAt == ' ' || charAt == '\n' || charAt == '\r' || charAt == '\t' || charAt == '\f' || charAt == '\b')) {
                if (charAt == '}') {
                    int i13 = i + 1;
                    char charAt3 = charAt(this.bp + i);
                    if (charAt3 == ',') {
                        this.token = 16;
                        int i14 = this.bp + (i13 - 1) + 1;
                        this.bp = i14;
                        if (i14 < this.len) {
                            c = this.text.charAt(i14);
                        }
                        this.ch = c;
                    } else if (charAt3 == ']') {
                        this.token = 15;
                        int i15 = this.bp + (i13 - 1) + 1;
                        this.bp = i15;
                        if (i15 < this.len) {
                            c = this.text.charAt(i15);
                        }
                        this.ch = c;
                    } else if (charAt3 == '}') {
                        this.token = 13;
                        int i16 = this.bp + (i13 - 1) + 1;
                        this.bp = i16;
                        if (i16 < this.len) {
                            c = this.text.charAt(i16);
                        }
                        this.ch = c;
                    } else if (charAt3 == 26) {
                        this.token = 20;
                        this.bp += i13 - 1;
                        this.ch = EOI;
                    } else {
                        this.matchStat = -1;
                        return 0;
                    }
                    this.matchStat = 4;
                    return z2 ? -i9 : i9;
                }
                this.matchStat = -1;
                return 0;
            }
            int i17 = i + 1;
            int i18 = this.bp + i;
            i = i17;
            charAt = i18 >= this.len ? EOI : this.text.charAt(i18);
        }
        int i19 = this.bp + (i - 1) + 1;
        this.bp = i19;
        if (i19 < this.len) {
            c = this.text.charAt(i19);
        }
        this.ch = c;
        this.matchStat = 3;
        this.token = 16;
        return z2 ? -i9 : i9;
    }

    public final int[] scanFieldIntArray(long j) {
        boolean z;
        int[] iArr;
        int i;
        int i2;
        char charAt;
        int i3;
        int i4;
        char charAt2;
        int[] iArr2;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        int[] iArr3 = null;
        if (matchFieldHash == 0) {
            return null;
        }
        int i5 = matchFieldHash + 1;
        int i6 = this.bp + matchFieldHash;
        if ((i6 >= this.len ? EOI : this.text.charAt(i6)) != '[') {
            this.matchStat = -1;
            return null;
        }
        int i7 = i5 + 1;
        int i8 = this.bp + i5;
        char charAt3 = i8 >= this.len ? EOI : this.text.charAt(i8);
        int[] iArr4 = new int[16];
        if (charAt3 == ']') {
            i4 = i7 + 1;
            int i9 = this.bp + i7;
            charAt2 = i9 >= this.len ? EOI : this.text.charAt(i9);
            i3 = 0;
        } else {
            int i10 = 0;
            while (true) {
                if (charAt3 == '-') {
                    int i11 = i7 + 1;
                    int i12 = this.bp + i7;
                    charAt3 = i12 >= this.len ? EOI : this.text.charAt(i12);
                    i7 = i11;
                    z = true;
                } else {
                    z = false;
                }
                if (charAt3 < '0') {
                    iArr = iArr3;
                    i = -1;
                    break;
                } else if (charAt3 > '9') {
                    i = -1;
                    iArr = null;
                    break;
                } else {
                    int i13 = charAt3 - '0';
                    while (true) {
                        i2 = i7 + 1;
                        int i14 = this.bp + i7;
                        charAt = i14 >= this.len ? EOI : this.text.charAt(i14);
                        if (charAt < '0' || charAt > '9') {
                            break;
                        }
                        i13 = (i13 * 10) + (charAt - '0');
                        i7 = i2;
                    }
                    if (i10 >= iArr4.length) {
                        int[] iArr5 = new int[(iArr4.length * 3) / 2];
                        System.arraycopy(iArr4, 0, iArr5, 0, i10);
                        iArr4 = iArr5;
                    }
                    i3 = i10 + 1;
                    if (z) {
                        i13 = -i13;
                    }
                    iArr4[i10] = i13;
                    if (charAt == ',') {
                        int i15 = i2 + 1;
                        int i16 = this.bp + i2;
                        i2 = i15;
                        iArr2 = null;
                        charAt = i16 >= this.len ? EOI : this.text.charAt(i16);
                    } else if (charAt == ']') {
                        i4 = i2 + 1;
                        int i17 = this.bp + i2;
                        charAt2 = i17 >= this.len ? EOI : this.text.charAt(i17);
                    } else {
                        iArr2 = null;
                    }
                    i10 = i3;
                    charAt3 = charAt;
                    iArr3 = iArr2;
                    i7 = i2;
                }
            }
            this.matchStat = i;
            return iArr;
        }
        if (i3 != iArr4.length) {
            int[] iArr6 = new int[i3];
            System.arraycopy(iArr4, 0, iArr6, 0, i3);
            iArr4 = iArr6;
        }
        if (charAt2 == ',') {
            this.bp += i4 - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return iArr4;
        } else if (charAt2 == '}') {
            int i18 = i4 + 1;
            char charAt4 = charAt(this.bp + i4);
            if (charAt4 == ',') {
                this.token = 16;
                this.bp += i18 - 1;
                next();
            } else if (charAt4 == ']') {
                this.token = 15;
                this.bp += i18 - 1;
                next();
            } else if (charAt4 == '}') {
                this.token = 13;
                this.bp += i18 - 1;
                next();
            } else if (charAt4 == 26) {
                this.bp += i18 - 1;
                this.token = 20;
                this.ch = EOI;
            } else {
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 4;
            return iArr4;
        } else {
            this.matchStat = -1;
            return null;
        }
    }

    public long scanFieldLong(long j) {
        int i;
        char charAt;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return 0L;
        }
        int i2 = matchFieldHash + 1;
        int i3 = this.bp + matchFieldHash;
        char charAt2 = i3 >= this.len ? EOI : this.text.charAt(i3);
        boolean z = charAt2 == '\"';
        if (z) {
            int i4 = i2 + 1;
            int i5 = this.bp + i2;
            charAt2 = i5 >= this.len ? EOI : this.text.charAt(i5);
            i2 = i4;
        }
        boolean z2 = charAt2 == '-';
        if (z2) {
            int i6 = i2 + 1;
            int i7 = this.bp + i2;
            charAt2 = i7 >= this.len ? EOI : this.text.charAt(i7);
            i2 = i6;
        }
        if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return 0L;
        }
        long j2 = charAt2 - '0';
        while (true) {
            i = i2 + 1;
            int i8 = this.bp + i2;
            charAt = i8 >= this.len ? EOI : this.text.charAt(i8);
            if (charAt < '0' || charAt > '9') {
                break;
            }
            j2 = (j2 * 10) + (charAt - '0');
            i2 = i;
        }
        if (charAt == '.') {
            this.matchStat = -1;
            return 0L;
        }
        if (charAt == '\"') {
            if (!z) {
                this.matchStat = -1;
                return 0L;
            }
            int i9 = i + 1;
            int i10 = this.bp + i;
            charAt = i10 >= this.len ? EOI : this.text.charAt(i10);
            i = i9;
        }
        if (j2 < 0) {
            this.matchStat = -1;
            return 0L;
        } else if (charAt == ',') {
            int i11 = this.bp + (i - 1) + 1;
            this.bp = i11;
            this.ch = i11 >= this.len ? EOI : this.text.charAt(i11);
            this.matchStat = 3;
            this.token = 16;
            return z2 ? -j2 : j2;
        } else if (charAt == '}') {
            int i12 = i + 1;
            char charAt3 = charAt(this.bp + i);
            if (charAt3 == ',') {
                this.token = 16;
                int i13 = this.bp + (i12 - 1) + 1;
                this.bp = i13;
                this.ch = i13 >= this.len ? EOI : this.text.charAt(i13);
            } else if (charAt3 == ']') {
                this.token = 15;
                int i14 = this.bp + (i12 - 1) + 1;
                this.bp = i14;
                this.ch = i14 >= this.len ? EOI : this.text.charAt(i14);
            } else if (charAt3 == '}') {
                this.token = 13;
                int i15 = this.bp + (i12 - 1) + 1;
                this.bp = i15;
                this.ch = i15 >= this.len ? EOI : this.text.charAt(i15);
            } else if (charAt3 == 26) {
                this.token = 20;
                this.bp += i12 - 1;
                this.ch = EOI;
            } else {
                this.matchStat = -1;
                return 0L;
            }
            this.matchStat = 4;
            return z2 ? -j2 : j2;
        } else {
            this.matchStat = -1;
            return 0L;
        }
    }

    public String scanFieldString(long j) {
        String str;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return null;
        }
        int i = matchFieldHash + 1;
        int i2 = this.bp + matchFieldHash;
        if (i2 >= this.len) {
            throw new JSONException("unclosed str, " + info());
        } else if (this.text.charAt(i2) != '\"') {
            this.matchStat = -1;
            return this.stringDefaultValue;
        } else {
            int i3 = this.bp + i;
            int indexOf = this.text.indexOf(34, i3);
            if (indexOf == -1) {
                throw new JSONException("unclosed str, " + info());
            }
            if (V6) {
                str = this.text.substring(i3, indexOf);
            } else {
                int i4 = indexOf - i3;
                str = new String(sub_chars(this.bp + i, i4), 0, i4);
            }
            if (str.indexOf(92) != -1) {
                boolean z = false;
                while (true) {
                    int i5 = indexOf - 1;
                    int i6 = 0;
                    while (i5 >= 0 && this.text.charAt(i5) == '\\') {
                        i6++;
                        i5--;
                        z = true;
                    }
                    if (i6 % 2 == 0) {
                        break;
                    }
                    indexOf = this.text.indexOf(34, indexOf + 1);
                }
                int i7 = indexOf - i3;
                char[] sub_chars = sub_chars(this.bp + i, i7);
                if (z) {
                    str = readString(sub_chars, i7);
                } else {
                    str = new String(sub_chars, 0, i7);
                    if (str.indexOf(92) != -1) {
                        str = readString(sub_chars, i7);
                    }
                }
            }
            int i8 = indexOf + 1;
            int i9 = this.len;
            char c = EOI;
            char charAt = i8 >= i9 ? EOI : this.text.charAt(i8);
            if (charAt == ',') {
                int i10 = i8 + 1;
                this.bp = i10;
                if (i10 < this.len) {
                    c = this.text.charAt(i10);
                }
                this.ch = c;
                this.matchStat = 3;
                this.token = 16;
                return str;
            } else if (charAt == '}') {
                int i11 = i8 + 1;
                char charAt2 = i11 >= this.len ? EOI : this.text.charAt(i11);
                if (charAt2 == ',') {
                    this.token = 16;
                    this.bp = i11;
                    next();
                } else if (charAt2 == ']') {
                    this.token = 15;
                    this.bp = i11;
                    next();
                } else if (charAt2 == '}') {
                    this.token = 13;
                    this.bp = i11;
                    next();
                } else if (charAt2 == 26) {
                    this.token = 20;
                    this.bp = i11;
                    this.ch = EOI;
                } else {
                    this.matchStat = -1;
                    return this.stringDefaultValue;
                }
                this.matchStat = 4;
                return str;
            } else {
                this.matchStat = -1;
                return this.stringDefaultValue;
            }
        }
    }

    public Date scanFieldDate(long j) {
        int i;
        char charAt;
        char c;
        int i2;
        Date date;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return null;
        }
        int i3 = this.bp;
        int i4 = matchFieldHash + 1;
        int i5 = matchFieldHash + i3;
        int i6 = this.len;
        char c2 = EOI;
        char charAt2 = i5 >= i6 ? EOI : this.text.charAt(i5);
        if (charAt2 == '\"') {
            int i7 = this.bp;
            int i8 = i7 + i4;
            int i9 = i4 + 1;
            int i10 = i7 + i4;
            if (i10 < this.len) {
                this.text.charAt(i10);
            }
            int indexOf = this.text.indexOf(34, this.bp + i9);
            if (indexOf == -1) {
                throw new JSONException("unclosed str");
            }
            int i11 = indexOf - i8;
            this.bp = i8;
            if (scanISO8601DateIfMatch(false, i11)) {
                date = this.calendar.getTime();
                int i12 = i9 + i11;
                i2 = i12 + 1;
                c = charAt(i12 + i3);
                this.bp = i3;
            } else {
                this.bp = i3;
                this.matchStat = -1;
                return null;
            }
        } else if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return null;
        } else {
            long j2 = charAt2 - '0';
            while (true) {
                i = i4 + 1;
                int i13 = this.bp + i4;
                charAt = i13 >= this.len ? EOI : this.text.charAt(i13);
                if (charAt < '0' || charAt > '9') {
                    break;
                }
                j2 = (j2 * 10) + (charAt - '0');
                i4 = i;
            }
            if (charAt == '.') {
                this.matchStat = -1;
                return null;
            }
            if (charAt == '\"') {
                int i14 = i + 1;
                int i15 = this.bp + i;
                c = i15 >= this.len ? EOI : this.text.charAt(i15);
                i2 = i14;
            } else {
                c = charAt;
                i2 = i;
            }
            if (j2 < 0) {
                this.matchStat = -1;
                return null;
            }
            date = new Date(j2);
        }
        if (c == ',') {
            int i16 = this.bp + (i2 - 1) + 1;
            this.bp = i16;
            if (i16 < this.len) {
                c2 = this.text.charAt(i16);
            }
            this.ch = c2;
            this.matchStat = 3;
            this.token = 16;
            return date;
        } else if (c == '}') {
            int i17 = i2 + 1;
            char charAt3 = charAt(this.bp + i2);
            if (charAt3 == ',') {
                this.token = 16;
                int i18 = this.bp + (i17 - 1) + 1;
                this.bp = i18;
                if (i18 < this.len) {
                    c2 = this.text.charAt(i18);
                }
                this.ch = c2;
            } else if (charAt3 == ']') {
                this.token = 15;
                int i19 = this.bp + (i17 - 1) + 1;
                this.bp = i19;
                if (i19 < this.len) {
                    c2 = this.text.charAt(i19);
                }
                this.ch = c2;
            } else if (charAt3 == '}') {
                this.token = 13;
                int i20 = this.bp + (i17 - 1) + 1;
                this.bp = i20;
                if (i20 < this.len) {
                    c2 = this.text.charAt(i20);
                }
                this.ch = c2;
            } else if (charAt3 == 26) {
                this.token = 20;
                this.bp += i17 - 1;
                this.ch = EOI;
            } else {
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 4;
            return date;
        } else {
            this.matchStat = -1;
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00bf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean scanFieldBoolean(long j) {
        int i;
        boolean z;
        char charAt;
        int i2;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return false;
        }
        if (!this.text.startsWith("false", this.bp + matchFieldHash)) {
            if (this.text.startsWith("true", this.bp + matchFieldHash)) {
                i = matchFieldHash + 4;
            } else if (this.text.startsWith("\"false\"", this.bp + matchFieldHash)) {
                i = matchFieldHash + 7;
            } else if (this.text.startsWith("\"true\"", this.bp + matchFieldHash)) {
                i = matchFieldHash + 6;
            } else if (this.text.charAt(this.bp + matchFieldHash) == '1') {
                i = matchFieldHash + 1;
            } else if (this.text.charAt(this.bp + matchFieldHash) == '0') {
                i = matchFieldHash + 1;
            } else if (this.text.startsWith("\"1\"", this.bp + matchFieldHash)) {
                i = matchFieldHash + 3;
            } else if (!this.text.startsWith("\"0\"", this.bp + matchFieldHash)) {
                this.matchStat = -1;
                return false;
            } else {
                i = matchFieldHash + 3;
            }
            z = true;
            int i3 = i + 1;
            int i4 = this.bp + i;
            int i5 = this.len;
            char c = EOI;
            charAt = i4 < i5 ? EOI : this.text.charAt(i4);
            while (charAt != ',') {
                if (charAt == '}' || !(charAt == ' ' || charAt == '\n' || charAt == '\r' || charAt == '\t' || charAt == '\f' || charAt == '\b')) {
                    if (charAt == '}') {
                        int i6 = i3 + 1;
                        char charAt2 = charAt(this.bp + i3);
                        if (charAt2 == ',') {
                            this.token = 16;
                            int i7 = this.bp + (i6 - 1) + 1;
                            this.bp = i7;
                            if (i7 < this.len) {
                                c = this.text.charAt(i7);
                            }
                            this.ch = c;
                        } else if (charAt2 == ']') {
                            this.token = 15;
                            int i8 = this.bp + (i6 - 1) + 1;
                            this.bp = i8;
                            if (i8 < this.len) {
                                c = this.text.charAt(i8);
                            }
                            this.ch = c;
                        } else if (charAt2 == '}') {
                            this.token = 13;
                            int i9 = this.bp + (i6 - 1) + 1;
                            this.bp = i9;
                            if (i9 < this.len) {
                                c = this.text.charAt(i9);
                            }
                            this.ch = c;
                        } else if (charAt2 == 26) {
                            this.token = 20;
                            this.bp += i6 - 1;
                            this.ch = EOI;
                        } else {
                            this.matchStat = -1;
                            return false;
                        }
                        this.matchStat = 4;
                        return z;
                    }
                    this.matchStat = -1;
                    return false;
                }
                int i10 = i3 + 1;
                int i11 = this.bp + i3;
                charAt = i11 >= this.len ? EOI : this.text.charAt(i11);
                i3 = i10;
            }
            i2 = this.bp + (i3 - 1) + 1;
            this.bp = i2;
            if (i2 < this.len) {
                c = this.text.charAt(i2);
            }
            this.ch = c;
            this.matchStat = 3;
            this.token = 16;
            return z;
        }
        i = matchFieldHash + 5;
        z = false;
        int i32 = i + 1;
        int i42 = this.bp + i;
        int i52 = this.len;
        char c2 = EOI;
        if (i42 < i52) {
        }
        while (charAt != ',') {
        }
        i2 = this.bp + (i32 - 1) + 1;
        this.bp = i2;
        if (i2 < this.len) {
        }
        this.ch = c2;
        this.matchStat = 3;
        this.token = 16;
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x00b4 A[ADDED_TO_REGION] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:50:0x00b1 -> B:51:0x00b2). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final float scanFieldFloat(long j) {
        int i;
        char charAt;
        int i2;
        float parseFloat;
        int i3;
        char charAt2;
        boolean z = false;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return 0.0f;
        }
        int i4 = matchFieldHash + 1;
        char charAt3 = charAt(this.bp + matchFieldHash);
        int i5 = this.bp;
        int i6 = (i5 + i4) - 1;
        boolean z2 = charAt3 == '-';
        if (z2) {
            i4++;
            charAt3 = charAt(i5 + i4);
        }
        if (charAt3 >= '0' && charAt3 <= '9') {
            int i7 = charAt3 - '0';
            while (true) {
                i = i4 + 1;
                charAt = charAt(this.bp + i4);
                if (charAt < '0' || charAt > '9') {
                    break;
                }
                i7 = (i7 * 10) + (charAt - '0');
                i4 = i;
            }
            if (charAt == '.') {
                int i8 = i + 1;
                char charAt4 = charAt(this.bp + i);
                if (charAt4 < '0' || charAt4 > '9') {
                    this.matchStat = -1;
                    return 0.0f;
                }
                i7 = (i7 * 10) + (charAt4 - '0');
                int i9 = 10;
                while (true) {
                    i3 = i8 + 1;
                    charAt2 = charAt(this.bp + i8);
                    if (charAt2 < '0' || charAt2 > '9') {
                        break;
                    }
                    i7 = (i7 * 10) + (charAt2 - '0');
                    i9 *= 10;
                    i8 = i3;
                }
                i = i3;
                i2 = i9;
                charAt = charAt2;
            } else {
                i2 = 1;
            }
            z = (charAt == 'e' || charAt == 'E') ? true : true;
            if (z) {
                int i10 = i + 1;
                charAt = charAt(this.bp + i);
                if (charAt == '+' || charAt == '-') {
                    int i11 = i10 + 1;
                    charAt = charAt(this.bp + i10);
                    i = i11;
                    if (charAt >= '0' && charAt <= '9') {
                        i11 = i + 1;
                        charAt = charAt(this.bp + i);
                        i = i11;
                        if (charAt >= '0') {
                            i11 = i + 1;
                            charAt = charAt(this.bp + i);
                            i = i11;
                            if (charAt >= '0') {
                            }
                        }
                    }
                } else {
                    i = i10;
                    if (charAt >= '0') {
                    }
                }
            }
            int i12 = ((this.bp + i) - i6) - 1;
            if (z || i12 >= 10) {
                parseFloat = Float.parseFloat(subString(i6, i12));
            } else {
                parseFloat = i7 / i2;
                if (z2) {
                    parseFloat = -parseFloat;
                }
            }
            if (charAt == ',') {
                this.bp += i - 1;
                next();
                this.matchStat = 3;
                this.token = 16;
                return parseFloat;
            } else if (charAt == '}') {
                int i13 = i + 1;
                char charAt5 = charAt(this.bp + i);
                if (charAt5 == ',') {
                    this.token = 16;
                    this.bp += i13 - 1;
                    next();
                } else if (charAt5 == ']') {
                    this.token = 15;
                    this.bp += i13 - 1;
                    next();
                } else if (charAt5 == '}') {
                    this.token = 13;
                    this.bp += i13 - 1;
                    next();
                } else if (charAt5 == 26) {
                    this.bp += i13 - 1;
                    this.token = 20;
                    this.ch = EOI;
                } else {
                    this.matchStat = -1;
                    return 0.0f;
                }
                this.matchStat = 4;
                return parseFloat;
            } else {
                this.matchStat = -1;
                return 0.0f;
            }
        }
        this.matchStat = -1;
        return 0.0f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:142:0x021c, code lost:
        r19.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x021e, code lost:
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00d3, code lost:
        r19.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00d5, code lost:
        return r3;
     */
    /* JADX WARN: Removed duplicated region for block: B:86:0x011b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0128  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:84:0x0116 -> B:85:0x0119). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final float[] scanFieldFloatArray(long j) {
        int i;
        char charAt;
        int i2;
        float parseFloat;
        char charAt2;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        float[] fArr = null;
        if (matchFieldHash == 0) {
            return null;
        }
        int i3 = matchFieldHash + 1;
        int i4 = this.bp + matchFieldHash;
        if ((i4 >= this.len ? EOI : this.text.charAt(i4)) != '[') {
            this.matchStat = -1;
            return null;
        }
        int i5 = i3 + 1;
        int i6 = this.bp + i3;
        char charAt3 = i6 >= this.len ? EOI : this.text.charAt(i6);
        float[] fArr2 = new float[16];
        int i7 = 0;
        while (true) {
            int i8 = this.bp;
            int i9 = (i8 + i5) - 1;
            boolean z = charAt3 == '-';
            if (z) {
                int i10 = i8 + i5;
                i5++;
                charAt3 = i10 >= this.len ? EOI : this.text.charAt(i10);
            }
            if (charAt3 < '0' || charAt3 > '9') {
                break;
            }
            int i11 = charAt3 - '0';
            while (true) {
                i = i5 + 1;
                int i12 = this.bp + i5;
                charAt = i12 >= this.len ? EOI : this.text.charAt(i12);
                if (charAt < '0' || charAt > '9') {
                    break;
                }
                i11 = (i11 * 10) + (charAt - '0');
                i5 = i;
            }
            if (charAt == '.') {
                int i13 = i + 1;
                int i14 = this.bp + i;
                char charAt4 = i14 >= this.len ? EOI : this.text.charAt(i14);
                if (charAt4 >= '0' && charAt4 <= '9') {
                    i11 = (i11 * 10) + (charAt4 - '0');
                    i2 = 10;
                    while (true) {
                        i = i13 + 1;
                        int i15 = this.bp + i13;
                        charAt = i15 >= this.len ? EOI : this.text.charAt(i15);
                        if (charAt < '0' || charAt > '9') {
                            break;
                        }
                        i11 = (i11 * 10) + (charAt - '0');
                        i2 *= 10;
                        i13 = i;
                    }
                } else {
                    break;
                }
            } else {
                i2 = 1;
            }
            boolean z2 = charAt == 'e' || charAt == 'E';
            if (z2) {
                int i16 = i + 1;
                int i17 = this.bp + i;
                char charAt5 = i17 >= this.len ? EOI : this.text.charAt(i17);
                if (charAt5 == '+' || charAt5 == '-') {
                    int i18 = i16 + 1;
                    int i19 = this.bp + i16;
                    if (i19 < this.len) {
                        charAt2 = this.text.charAt(i19);
                        i = i18;
                        charAt = charAt2;
                        if (charAt >= '0' && charAt <= '9') {
                            i18 = i + 1;
                            int i20 = this.bp + i;
                            if (i20 < this.len) {
                                charAt2 = this.text.charAt(i20);
                                i = i18;
                                charAt = charAt2;
                                if (charAt >= '0') {
                                    i18 = i + 1;
                                    int i202 = this.bp + i;
                                    if (i202 < this.len) {
                                    }
                                }
                            }
                        }
                    }
                    charAt2 = EOI;
                    i = i18;
                    charAt = charAt2;
                    if (charAt >= '0') {
                    }
                } else {
                    charAt = charAt5;
                    i = i16;
                    if (charAt >= '0') {
                    }
                }
            }
            int i21 = ((this.bp + i) - i9) - 1;
            if (z2 || i21 >= 10) {
                parseFloat = Float.parseFloat(subString(i9, i21));
            } else {
                parseFloat = i11 / i2;
                if (z) {
                    parseFloat = -parseFloat;
                }
            }
            if (i7 >= fArr2.length) {
                float[] fArr3 = new float[(fArr2.length * 3) / 2];
                System.arraycopy(fArr2, 0, fArr3, 0, i7);
                fArr2 = fArr3;
            }
            int i22 = i7 + 1;
            fArr2[i7] = parseFloat;
            if (charAt == ',') {
                int i23 = i + 1;
                int i24 = this.bp + i;
                charAt = i24 >= this.len ? EOI : this.text.charAt(i24);
                i = i23;
            } else if (charAt == ']') {
                int i25 = i + 1;
                int i26 = this.bp + i;
                char charAt6 = i26 >= this.len ? EOI : this.text.charAt(i26);
                if (i22 != fArr2.length) {
                    float[] fArr4 = new float[i22];
                    System.arraycopy(fArr2, 0, fArr4, 0, i22);
                    fArr2 = fArr4;
                }
                if (charAt6 == ',') {
                    this.bp += i25 - 1;
                    next();
                    this.matchStat = 3;
                    this.token = 16;
                    return fArr2;
                } else if (charAt6 == '}') {
                    int i27 = i25 + 1;
                    int i28 = this.bp + i25;
                    char charAt7 = i28 >= this.len ? EOI : this.text.charAt(i28);
                    if (charAt7 == ',') {
                        this.token = 16;
                        this.bp += i27 - 1;
                        next();
                    } else if (charAt7 == ']') {
                        this.token = 15;
                        this.bp += i27 - 1;
                        next();
                    } else if (charAt7 == '}') {
                        this.token = 13;
                        this.bp += i27 - 1;
                        next();
                    } else if (charAt7 == 26) {
                        this.bp += i27 - 1;
                        this.token = 20;
                        this.ch = EOI;
                    } else {
                        this.matchStat = -1;
                        return null;
                    }
                    this.matchStat = 4;
                    return fArr2;
                } else {
                    this.matchStat = -1;
                    return null;
                }
            }
            fArr = null;
            charAt3 = charAt;
            i7 = i22;
            i5 = i;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:166:0x029f, code lost:
        r12 = r3;
        r20.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x02a5, code lost:
        return r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00eb, code lost:
        r20.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00f0, code lost:
        return r3;
     */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0136 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0145  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:88:0x0132 -> B:89:0x0134). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final float[][] scanFieldFloatArray2(long j) {
        int i;
        char charAt;
        int i2;
        float parseFloat;
        int i3;
        float[][] fArr;
        float[][] fArr2;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        float[][] fArr3 = null;
        if (matchFieldHash == 0) {
            return null;
        }
        int i4 = matchFieldHash + 1;
        int i5 = this.bp + matchFieldHash;
        char charAt2 = i5 >= this.len ? EOI : this.text.charAt(i5);
        char c = '[';
        if (charAt2 != '[') {
            this.matchStat = -1;
            return null;
        }
        int i6 = i4 + 1;
        int i7 = this.bp + i4;
        char charAt3 = i7 >= this.len ? EOI : this.text.charAt(i7);
        int i8 = 16;
        float[][] fArr4 = new float[16];
        int i9 = 0;
        loop0: while (true) {
            if (charAt3 == c) {
                int i10 = i6 + 1;
                int i11 = this.bp + i6;
                char charAt4 = i11 >= this.len ? EOI : this.text.charAt(i11);
                float[] fArr5 = new float[i8];
                int i12 = 0;
                while (true) {
                    int i13 = this.bp;
                    int i14 = (i13 + i10) - 1;
                    boolean z = charAt4 == '-';
                    if (z) {
                        int i15 = i13 + i10;
                        i10++;
                        charAt4 = i15 >= this.len ? EOI : this.text.charAt(i15);
                    }
                    if (charAt4 < '0' || charAt4 > '9') {
                        break loop0;
                    }
                    int i16 = charAt4 - '0';
                    while (true) {
                        i = i10 + 1;
                        int i17 = this.bp + i10;
                        charAt = i17 >= this.len ? EOI : this.text.charAt(i17);
                        if (charAt < '0' || charAt > '9') {
                            break;
                        }
                        i16 = (i16 * 10) + (charAt - '0');
                        i10 = i;
                    }
                    if (charAt == '.') {
                        int i18 = i + 1;
                        int i19 = this.bp + i;
                        char charAt5 = i19 >= this.len ? EOI : this.text.charAt(i19);
                        if (charAt5 >= '0' && charAt5 <= '9') {
                            i16 = (i16 * 10) + (charAt5 - '0');
                            i2 = 10;
                            while (true) {
                                i = i18 + 1;
                                int i20 = this.bp + i18;
                                charAt = i20 >= this.len ? EOI : this.text.charAt(i20);
                                if (charAt < '0' || charAt > '9') {
                                    break;
                                }
                                i16 = (i16 * 10) + (charAt - '0');
                                i2 *= 10;
                                i18 = i;
                            }
                        } else {
                            break loop0;
                        }
                    } else {
                        i2 = 1;
                    }
                    boolean z2 = charAt == 'e' || charAt == 'E';
                    if (z2) {
                        int i21 = i + 1;
                        int i22 = this.bp + i;
                        char charAt6 = i22 >= this.len ? EOI : this.text.charAt(i22);
                        if (charAt6 == '+' || charAt6 == '-') {
                            int i23 = i21 + 1;
                            int i24 = this.bp + i21;
                            char charAt7 = i24 >= this.len ? EOI : this.text.charAt(i24);
                            charAt = charAt7;
                            i = i23;
                            if (charAt >= '0' && charAt <= '9') {
                                i23 = i + 1;
                                int i25 = this.bp + i;
                                if (i25 < this.len) {
                                    charAt = EOI;
                                    i = i23;
                                    if (charAt >= '0') {
                                        i23 = i + 1;
                                        int i252 = this.bp + i;
                                        if (i252 < this.len) {
                                            charAt7 = this.text.charAt(i252);
                                            charAt = charAt7;
                                            i = i23;
                                            if (charAt >= '0') {
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            charAt = charAt6;
                            i = i21;
                            if (charAt >= '0') {
                            }
                        }
                    }
                    int i26 = ((this.bp + i) - i14) - 1;
                    if (z2 || i26 >= 10) {
                        parseFloat = Float.parseFloat(subString(i14, i26));
                    } else {
                        parseFloat = i16 / i2;
                        if (z) {
                            parseFloat = -parseFloat;
                        }
                    }
                    if (i12 >= fArr5.length) {
                        float[] fArr6 = new float[(fArr5.length * 3) / 2];
                        System.arraycopy(fArr5, 0, fArr6, 0, i12);
                        fArr5 = fArr6;
                    }
                    int i27 = i12 + 1;
                    fArr5[i12] = parseFloat;
                    if (charAt == ',') {
                        int i28 = i + 1;
                        int i29 = this.bp + i;
                        charAt4 = i29 >= this.len ? EOI : this.text.charAt(i29);
                        i = i28;
                        fArr2 = null;
                    } else if (charAt == ']') {
                        int i30 = i + 1;
                        int i31 = this.bp + i;
                        char charAt8 = i31 >= this.len ? EOI : this.text.charAt(i31);
                        if (i27 != fArr5.length) {
                            float[] fArr7 = new float[i27];
                            i3 = 0;
                            System.arraycopy(fArr5, 0, fArr7, 0, i27);
                            fArr5 = fArr7;
                        } else {
                            i3 = 0;
                        }
                        if (i9 >= fArr4.length) {
                            fArr4 = new float[(fArr4.length * 3) / 2];
                            System.arraycopy(fArr5, i3, fArr4, i3, i27);
                        }
                        int i32 = i9 + 1;
                        fArr4[i9] = fArr5;
                        if (charAt8 == ',') {
                            int i33 = i30 + 1;
                            int i34 = this.bp + i30;
                            charAt3 = i34 >= this.len ? EOI : this.text.charAt(i34);
                            i6 = i33;
                            fArr = null;
                        } else if (charAt8 == ']') {
                            int i35 = i30 + 1;
                            int i36 = this.bp + i30;
                            char charAt9 = i36 >= this.len ? EOI : this.text.charAt(i36);
                            if (i32 != fArr4.length) {
                                float[][] fArr8 = new float[i32];
                                System.arraycopy(fArr4, 0, fArr8, 0, i32);
                                fArr4 = fArr8;
                            }
                            if (charAt9 == ',') {
                                this.bp += i35 - 1;
                                next();
                                this.matchStat = 3;
                                this.token = 16;
                                return fArr4;
                            } else if (charAt9 == '}') {
                                int i37 = i35 + 1;
                                char charAt10 = charAt(this.bp + i35);
                                if (charAt10 == ',') {
                                    this.token = 16;
                                    this.bp += i37 - 1;
                                    next();
                                } else if (charAt10 == ']') {
                                    this.token = 15;
                                    this.bp += i37 - 1;
                                    next();
                                } else if (charAt10 == '}') {
                                    this.token = 13;
                                    this.bp += i37 - 1;
                                    next();
                                } else if (charAt10 == 26) {
                                    this.bp += i37 - 1;
                                    this.token = 20;
                                    this.ch = EOI;
                                } else {
                                    this.matchStat = -1;
                                    return null;
                                }
                                this.matchStat = 4;
                                return fArr4;
                            } else {
                                this.matchStat = -1;
                                return null;
                            }
                        } else {
                            fArr = null;
                            charAt3 = charAt8;
                            i6 = i30;
                        }
                        i9 = i32;
                        fArr3 = fArr;
                        c = '[';
                        i8 = 16;
                    } else {
                        fArr2 = null;
                        charAt4 = charAt;
                    }
                    fArr3 = fArr2;
                    i10 = i;
                    i12 = i27;
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x00bc A[ADDED_TO_REGION] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:52:0x00b9 -> B:53:0x00ba). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final double scanFieldDouble(long j) {
        int i;
        char charAt;
        int i2;
        double parseDouble;
        int i3;
        char charAt2;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return 0.0d;
        }
        int i4 = matchFieldHash + 1;
        char charAt3 = charAt(this.bp + matchFieldHash);
        int i5 = this.bp;
        int i6 = (i5 + i4) - 1;
        boolean z = charAt3 == '-';
        if (z) {
            i4++;
            charAt3 = charAt(i5 + i4);
        }
        if (charAt3 >= '0' && charAt3 <= '9') {
            int i7 = charAt3 - '0';
            while (true) {
                i = i4 + 1;
                charAt = charAt(this.bp + i4);
                if (charAt < '0' || charAt > '9') {
                    break;
                }
                i7 = (i7 * 10) + (charAt - '0');
                i4 = i;
            }
            if (charAt == '.') {
                int i8 = i + 1;
                char charAt4 = charAt(this.bp + i);
                if (charAt4 < '0' || charAt4 > '9') {
                    this.matchStat = -1;
                    return 0.0d;
                }
                i7 = (i7 * 10) + (charAt4 - '0');
                int i9 = 10;
                while (true) {
                    i3 = i8 + 1;
                    charAt2 = charAt(this.bp + i8);
                    if (charAt2 < '0' || charAt2 > '9') {
                        break;
                    }
                    i7 = (i7 * 10) + (charAt2 - '0');
                    i9 *= 10;
                    i8 = i3;
                }
                i = i3;
                i2 = i9;
                charAt = charAt2;
            } else {
                i2 = 1;
            }
            boolean z2 = charAt == 'e' || charAt == 'E';
            if (z2) {
                int i10 = i + 1;
                charAt = charAt(this.bp + i);
                if (charAt == '+' || charAt == '-') {
                    int i11 = i10 + 1;
                    charAt = charAt(this.bp + i10);
                    i = i11;
                    if (charAt >= '0' && charAt <= '9') {
                        i11 = i + 1;
                        charAt = charAt(this.bp + i);
                        i = i11;
                        if (charAt >= '0') {
                            i11 = i + 1;
                            charAt = charAt(this.bp + i);
                            i = i11;
                            if (charAt >= '0') {
                            }
                        }
                    }
                } else {
                    i = i10;
                    if (charAt >= '0') {
                    }
                }
            }
            int i12 = ((this.bp + i) - i6) - 1;
            if (!z2 && i12 < 10) {
                double d = i7;
                double d2 = i2;
                Double.isNaN(d);
                Double.isNaN(d2);
                parseDouble = d / d2;
                if (z) {
                    parseDouble = -parseDouble;
                }
            } else {
                parseDouble = Double.parseDouble(subString(i6, i12));
            }
            if (charAt == ',') {
                this.bp += i - 1;
                next();
                this.matchStat = 3;
                this.token = 16;
                return parseDouble;
            } else if (charAt == '}') {
                int i13 = i + 1;
                char charAt5 = charAt(this.bp + i);
                if (charAt5 == ',') {
                    this.token = 16;
                    this.bp += i13 - 1;
                    next();
                } else if (charAt5 == ']') {
                    this.token = 15;
                    this.bp += i13 - 1;
                    next();
                } else if (charAt5 == '}') {
                    this.token = 13;
                    this.bp += i13 - 1;
                    next();
                } else if (charAt5 == 26) {
                    this.bp += i13 - 1;
                    this.token = 20;
                    this.ch = EOI;
                } else {
                    this.matchStat = -1;
                    return 0.0d;
                }
                this.matchStat = 4;
                return parseDouble;
            } else {
                this.matchStat = -1;
                return 0.0d;
            }
        }
        this.matchStat = -1;
        return 0.0d;
    }

    /* JADX WARN: Code restructure failed: missing block: B:60:0x00d3, code lost:
        r19.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00d5, code lost:
        return r3;
     */
    /* JADX WARN: Removed duplicated region for block: B:86:0x011b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0128  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:84:0x0116 -> B:85:0x0119). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final double[] scanFieldDoubleArray(long j) {
        int i;
        char charAt;
        int i2;
        double parseDouble;
        char charAt2;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        double[] dArr = null;
        if (matchFieldHash == 0) {
            return null;
        }
        int i3 = matchFieldHash + 1;
        int i4 = this.bp + matchFieldHash;
        if ((i4 >= this.len ? EOI : this.text.charAt(i4)) != '[') {
            this.matchStat = -1;
            return null;
        }
        int i5 = i3 + 1;
        int i6 = this.bp + i3;
        char charAt3 = i6 >= this.len ? EOI : this.text.charAt(i6);
        double[] dArr2 = new double[16];
        int i7 = 0;
        while (true) {
            int i8 = this.bp;
            int i9 = (i8 + i5) - 1;
            boolean z = charAt3 == '-';
            if (z) {
                int i10 = i8 + i5;
                i5++;
                charAt3 = i10 >= this.len ? EOI : this.text.charAt(i10);
            }
            if (charAt3 < '0' || charAt3 > '9') {
                break;
            }
            int i11 = charAt3 - '0';
            while (true) {
                i = i5 + 1;
                int i12 = this.bp + i5;
                charAt = i12 >= this.len ? EOI : this.text.charAt(i12);
                if (charAt < '0' || charAt > '9') {
                    break;
                }
                i11 = (i11 * 10) + (charAt - '0');
                i5 = i;
            }
            if (charAt == '.') {
                int i13 = i + 1;
                int i14 = this.bp + i;
                char charAt4 = i14 >= this.len ? EOI : this.text.charAt(i14);
                if (charAt4 >= '0' && charAt4 <= '9') {
                    i11 = (i11 * 10) + (charAt4 - '0');
                    i2 = 10;
                    while (true) {
                        i = i13 + 1;
                        int i15 = this.bp + i13;
                        charAt = i15 >= this.len ? EOI : this.text.charAt(i15);
                        if (charAt < '0' || charAt > '9') {
                            break;
                        }
                        i11 = (i11 * 10) + (charAt - '0');
                        i2 *= 10;
                        i13 = i;
                    }
                } else {
                    break;
                }
            } else {
                i2 = 1;
            }
            boolean z2 = charAt == 'e' || charAt == 'E';
            if (z2) {
                int i16 = i + 1;
                int i17 = this.bp + i;
                char charAt5 = i17 >= this.len ? EOI : this.text.charAt(i17);
                if (charAt5 == '+' || charAt5 == '-') {
                    int i18 = i16 + 1;
                    int i19 = this.bp + i16;
                    if (i19 < this.len) {
                        charAt2 = this.text.charAt(i19);
                        i = i18;
                        charAt = charAt2;
                        if (charAt >= '0' && charAt <= '9') {
                            i18 = i + 1;
                            int i20 = this.bp + i;
                            if (i20 < this.len) {
                                charAt2 = this.text.charAt(i20);
                                i = i18;
                                charAt = charAt2;
                                if (charAt >= '0') {
                                    i18 = i + 1;
                                    int i202 = this.bp + i;
                                    if (i202 < this.len) {
                                    }
                                }
                            }
                        }
                    }
                    charAt2 = EOI;
                    i = i18;
                    charAt = charAt2;
                    if (charAt >= '0') {
                    }
                } else {
                    charAt = charAt5;
                    i = i16;
                    if (charAt >= '0') {
                    }
                }
            }
            int i21 = ((this.bp + i) - i9) - 1;
            if (!z2 && i21 < 10) {
                double d = i11;
                double d2 = i2;
                Double.isNaN(d);
                Double.isNaN(d2);
                parseDouble = d / d2;
                if (z) {
                    parseDouble = -parseDouble;
                }
            } else {
                parseDouble = Double.parseDouble(subString(i9, i21));
            }
            if (i7 >= dArr2.length) {
                double[] dArr3 = new double[(dArr2.length * 3) / 2];
                System.arraycopy(dArr2, 0, dArr3, 0, i7);
                dArr2 = dArr3;
            }
            int i22 = i7 + 1;
            dArr2[i7] = parseDouble;
            if (charAt == ',') {
                int i23 = i + 1;
                int i24 = this.bp + i;
                charAt = i24 >= this.len ? EOI : this.text.charAt(i24);
                i = i23;
            } else if (charAt == ']') {
                int i25 = i + 1;
                int i26 = this.bp + i;
                char charAt6 = i26 >= this.len ? EOI : this.text.charAt(i26);
                if (i22 != dArr2.length) {
                    double[] dArr4 = new double[i22];
                    System.arraycopy(dArr2, 0, dArr4, 0, i22);
                    dArr2 = dArr4;
                }
                if (charAt6 == ',') {
                    this.bp += i25 - 1;
                    next();
                    this.matchStat = 3;
                    this.token = 16;
                    return dArr2;
                } else if (charAt6 == '}') {
                    int i27 = i25 + 1;
                    int i28 = this.bp + i25;
                    char charAt7 = i28 >= this.len ? EOI : this.text.charAt(i28);
                    if (charAt7 == ',') {
                        this.token = 16;
                        this.bp += i27 - 1;
                        next();
                    } else if (charAt7 == ']') {
                        this.token = 15;
                        this.bp += i27 - 1;
                        next();
                    } else if (charAt7 == '}') {
                        this.token = 13;
                        this.bp += i27 - 1;
                        next();
                    } else if (charAt7 == 26) {
                        this.bp += i27 - 1;
                        this.token = 20;
                        this.ch = EOI;
                    } else {
                        this.matchStat = -1;
                        return null;
                    }
                    this.matchStat = 4;
                    return dArr2;
                } else {
                    this.matchStat = -1;
                    return null;
                }
            }
            dArr = null;
            charAt3 = charAt;
            i7 = i22;
            i5 = i;
        }
        this.matchStat = -1;
        return dArr;
    }

    /* JADX WARN: Code restructure failed: missing block: B:166:0x02a5, code lost:
        r12 = r3;
        r20.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x02ab, code lost:
        return r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00eb, code lost:
        r20.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00f0, code lost:
        return r3;
     */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0136 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0145  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:88:0x0132 -> B:89:0x0134). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final double[][] scanFieldDoubleArray2(long j) {
        int i;
        char charAt;
        int i2;
        double parseDouble;
        int i3;
        double[][] dArr;
        double[][] dArr2;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        double[][] dArr3 = null;
        if (matchFieldHash == 0) {
            return null;
        }
        int i4 = matchFieldHash + 1;
        int i5 = this.bp + matchFieldHash;
        char charAt2 = i5 >= this.len ? EOI : this.text.charAt(i5);
        char c = '[';
        if (charAt2 != '[') {
            this.matchStat = -1;
            return null;
        }
        int i6 = i4 + 1;
        int i7 = this.bp + i4;
        char charAt3 = i7 >= this.len ? EOI : this.text.charAt(i7);
        int i8 = 16;
        double[][] dArr4 = new double[16];
        int i9 = 0;
        loop0: while (true) {
            if (charAt3 == c) {
                int i10 = i6 + 1;
                int i11 = this.bp + i6;
                char charAt4 = i11 >= this.len ? EOI : this.text.charAt(i11);
                double[] dArr5 = new double[i8];
                int i12 = 0;
                while (true) {
                    int i13 = this.bp;
                    int i14 = (i13 + i10) - 1;
                    boolean z = charAt4 == '-';
                    if (z) {
                        int i15 = i13 + i10;
                        i10++;
                        charAt4 = i15 >= this.len ? EOI : this.text.charAt(i15);
                    }
                    if (charAt4 < '0' || charAt4 > '9') {
                        break loop0;
                    }
                    int i16 = charAt4 - '0';
                    while (true) {
                        i = i10 + 1;
                        int i17 = this.bp + i10;
                        charAt = i17 >= this.len ? EOI : this.text.charAt(i17);
                        if (charAt < '0' || charAt > '9') {
                            break;
                        }
                        i16 = (i16 * 10) + (charAt - '0');
                        i10 = i;
                    }
                    if (charAt == '.') {
                        int i18 = i + 1;
                        int i19 = this.bp + i;
                        char charAt5 = i19 >= this.len ? EOI : this.text.charAt(i19);
                        if (charAt5 >= '0' && charAt5 <= '9') {
                            i16 = (i16 * 10) + (charAt5 - '0');
                            i2 = 10;
                            while (true) {
                                i = i18 + 1;
                                int i20 = this.bp + i18;
                                charAt = i20 >= this.len ? EOI : this.text.charAt(i20);
                                if (charAt < '0' || charAt > '9') {
                                    break;
                                }
                                i16 = (i16 * 10) + (charAt - '0');
                                i2 *= 10;
                                i18 = i;
                            }
                        } else {
                            break loop0;
                        }
                    } else {
                        i2 = 1;
                    }
                    boolean z2 = charAt == 'e' || charAt == 'E';
                    if (z2) {
                        int i21 = i + 1;
                        int i22 = this.bp + i;
                        char charAt6 = i22 >= this.len ? EOI : this.text.charAt(i22);
                        if (charAt6 == '+' || charAt6 == '-') {
                            int i23 = i21 + 1;
                            int i24 = this.bp + i21;
                            char charAt7 = i24 >= this.len ? EOI : this.text.charAt(i24);
                            charAt = charAt7;
                            i = i23;
                            if (charAt >= '0' && charAt <= '9') {
                                i23 = i + 1;
                                int i25 = this.bp + i;
                                if (i25 < this.len) {
                                    charAt = EOI;
                                    i = i23;
                                    if (charAt >= '0') {
                                        i23 = i + 1;
                                        int i252 = this.bp + i;
                                        if (i252 < this.len) {
                                            charAt7 = this.text.charAt(i252);
                                            charAt = charAt7;
                                            i = i23;
                                            if (charAt >= '0') {
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            charAt = charAt6;
                            i = i21;
                            if (charAt >= '0') {
                            }
                        }
                    }
                    int i26 = ((this.bp + i) - i14) - 1;
                    if (!z2 && i26 < 10) {
                        double d = i16;
                        double d2 = i2;
                        Double.isNaN(d);
                        Double.isNaN(d2);
                        parseDouble = d / d2;
                        if (z) {
                            parseDouble = -parseDouble;
                        }
                    } else {
                        parseDouble = Double.parseDouble(subString(i14, i26));
                    }
                    if (i12 >= dArr5.length) {
                        double[] dArr6 = new double[(dArr5.length * 3) / 2];
                        System.arraycopy(dArr5, 0, dArr6, 0, i12);
                        dArr5 = dArr6;
                    }
                    int i27 = i12 + 1;
                    dArr5[i12] = parseDouble;
                    if (charAt == ',') {
                        int i28 = i + 1;
                        int i29 = this.bp + i;
                        charAt4 = i29 >= this.len ? EOI : this.text.charAt(i29);
                        i = i28;
                        dArr2 = null;
                    } else if (charAt == ']') {
                        int i30 = i + 1;
                        int i31 = this.bp + i;
                        char charAt8 = i31 >= this.len ? EOI : this.text.charAt(i31);
                        if (i27 != dArr5.length) {
                            double[] dArr7 = new double[i27];
                            i3 = 0;
                            System.arraycopy(dArr5, 0, dArr7, 0, i27);
                            dArr5 = dArr7;
                        } else {
                            i3 = 0;
                        }
                        if (i9 >= dArr4.length) {
                            dArr4 = new double[(dArr4.length * 3) / 2];
                            System.arraycopy(dArr5, i3, dArr4, i3, i27);
                        }
                        int i32 = i9 + 1;
                        dArr4[i9] = dArr5;
                        if (charAt8 == ',') {
                            int i33 = i30 + 1;
                            int i34 = this.bp + i30;
                            charAt3 = i34 >= this.len ? EOI : this.text.charAt(i34);
                            i6 = i33;
                            dArr = null;
                        } else if (charAt8 == ']') {
                            int i35 = i30 + 1;
                            int i36 = this.bp + i30;
                            char charAt9 = i36 >= this.len ? EOI : this.text.charAt(i36);
                            if (i32 != dArr4.length) {
                                double[][] dArr8 = new double[i32];
                                System.arraycopy(dArr4, 0, dArr8, 0, i32);
                                dArr4 = dArr8;
                            }
                            if (charAt9 == ',') {
                                this.bp += i35 - 1;
                                next();
                                this.matchStat = 3;
                                this.token = 16;
                                return dArr4;
                            } else if (charAt9 == '}') {
                                int i37 = i35 + 1;
                                char charAt10 = charAt(this.bp + i35);
                                if (charAt10 == ',') {
                                    this.token = 16;
                                    this.bp += i37 - 1;
                                    next();
                                } else if (charAt10 == ']') {
                                    this.token = 15;
                                    this.bp += i37 - 1;
                                    next();
                                } else if (charAt10 == '}') {
                                    this.token = 13;
                                    this.bp += i37 - 1;
                                    next();
                                } else if (charAt10 == 26) {
                                    this.bp += i37 - 1;
                                    this.token = 20;
                                    this.ch = EOI;
                                } else {
                                    this.matchStat = -1;
                                    return null;
                                }
                                this.matchStat = 4;
                                return dArr4;
                            } else {
                                this.matchStat = -1;
                                return null;
                            }
                        } else {
                            dArr = null;
                            charAt3 = charAt8;
                            i6 = i30;
                        }
                        i9 = i32;
                        dArr3 = dArr;
                        c = '[';
                        i8 = 16;
                    } else {
                        dArr2 = null;
                        charAt4 = charAt;
                    }
                    dArr3 = dArr2;
                    i10 = i;
                    i12 = i27;
                }
            }
        }
    }

    public long scanFieldSymbol(long j) {
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return 0L;
        }
        int i = matchFieldHash + 1;
        int i2 = this.bp + matchFieldHash;
        int i3 = this.len;
        char c = EOI;
        if ((i2 >= i3 ? EOI : this.text.charAt(i2)) != '\"') {
            this.matchStat = -1;
            return 0L;
        }
        long j2 = -3750763034362895579L;
        while (true) {
            int i4 = i + 1;
            int i5 = this.bp + i;
            char charAt = i5 >= this.len ? EOI : this.text.charAt(i5);
            if (charAt == '\"') {
                int i6 = i4 + 1;
                int i7 = this.bp + i4;
                char charAt2 = i7 >= this.len ? EOI : this.text.charAt(i7);
                if (charAt2 == ',') {
                    int i8 = this.bp + (i6 - 1) + 1;
                    this.bp = i8;
                    if (i8 < this.len) {
                        c = this.text.charAt(i8);
                    }
                    this.ch = c;
                    this.matchStat = 3;
                    return j2;
                } else if (charAt2 == '}') {
                    int i9 = i6 + 1;
                    int i10 = this.bp + i6;
                    char charAt3 = i10 >= this.len ? EOI : this.text.charAt(i10);
                    if (charAt3 == ',') {
                        this.token = 16;
                        this.bp += i9 - 1;
                        next();
                    } else if (charAt3 == ']') {
                        this.token = 15;
                        this.bp += i9 - 1;
                        next();
                    } else if (charAt3 == '}') {
                        this.token = 13;
                        this.bp += i9 - 1;
                        next();
                    } else if (charAt3 == 26) {
                        this.token = 20;
                        this.bp += i9 - 1;
                        this.ch = EOI;
                    } else {
                        this.matchStat = -1;
                        return 0L;
                    }
                    this.matchStat = 4;
                    return j2;
                } else {
                    this.matchStat = -1;
                    return 0L;
                }
            }
            j2 = (j2 ^ charAt) * 1099511628211L;
            if (charAt == '\\') {
                this.matchStat = -1;
                return 0L;
            }
            i = i4;
        }
    }

    public boolean scanISO8601DateIfMatch(boolean z) {
        return scanISO8601DateIfMatch(z, this.len - this.bp);
    }

    /* JADX WARN: Removed duplicated region for block: B:115:0x01f9 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x01fc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean scanISO8601DateIfMatch(boolean z, int i) {
        char c;
        int i2;
        int i3;
        int i4;
        int i5;
        char c2;
        int i6;
        char c3;
        int i7;
        char charAt;
        int i8;
        char charAt2;
        int i9;
        int i10;
        char charAt3;
        char charAt4;
        char charAt5;
        if (!z && i > 13) {
            char charAt6 = charAt(this.bp);
            char charAt7 = charAt(this.bp + 1);
            char charAt8 = charAt(this.bp + 2);
            char charAt9 = charAt(this.bp + 3);
            char charAt10 = charAt(this.bp + 4);
            char charAt11 = charAt(this.bp + 5);
            char charAt12 = charAt((this.bp + i) - 1);
            char charAt13 = charAt((this.bp + i) - 2);
            if (charAt6 == '/' && charAt7 == 'D' && charAt8 == 'a' && charAt9 == 't' && charAt10 == 'e' && charAt11 == '(' && charAt12 == '/' && charAt13 == ')') {
                int i11 = -1;
                for (int i12 = 6; i12 < i; i12++) {
                    char charAt14 = charAt(this.bp + i12);
                    if (charAt14 != '+') {
                        if (charAt14 < '0' || charAt14 > '9') {
                            break;
                        }
                    } else {
                        i11 = i12;
                    }
                }
                if (i11 == -1) {
                    return false;
                }
                int i13 = this.bp + 6;
                long parseLong = Long.parseLong(subString(i13, i11 - i13));
                Calendar calendar = Calendar.getInstance(this.timeZone, this.locale);
                this.calendar = calendar;
                calendar.setTimeInMillis(parseLong);
                this.token = 5;
                return true;
            }
        }
        if (i == 8 || i == 14 || ((i == 16 && ((charAt5 = charAt(this.bp + 10)) == 'T' || charAt5 == ' ')) || (i == 17 && charAt(this.bp + 6) != '-'))) {
            if (z) {
                return false;
            }
            char charAt15 = charAt(this.bp);
            char charAt16 = charAt(this.bp + 1);
            char charAt17 = charAt(this.bp + 2);
            char charAt18 = charAt(this.bp + 3);
            char charAt19 = charAt(this.bp + 4);
            char charAt20 = charAt(this.bp + 5);
            char charAt21 = charAt(this.bp + 6);
            char charAt22 = charAt(this.bp + 7);
            char charAt23 = charAt(this.bp + 8);
            boolean z2 = charAt19 == '-' && charAt22 == '-';
            boolean z3 = z2 && i == 16;
            boolean z4 = z2 && i == 17;
            if (z4 || z3) {
                charAt22 = charAt(this.bp + 9);
                c = charAt23;
            } else {
                c = charAt21;
                charAt21 = charAt20;
                charAt20 = charAt19;
            }
            if (checkDate(charAt15, charAt16, charAt17, charAt18, charAt20, charAt21, c, charAt22)) {
                setCalendar(charAt15, charAt16, charAt17, charAt18, charAt20, charAt21, c, charAt22);
                if (i != 8) {
                    char charAt24 = charAt(this.bp + 9);
                    char charAt25 = charAt(this.bp + 10);
                    char charAt26 = charAt(this.bp + 11);
                    char charAt27 = charAt(this.bp + 12);
                    char charAt28 = charAt(this.bp + 13);
                    if ((z4 && charAt25 == 'T' && charAt28 == ':' && charAt(this.bp + 16) == 'Z') || (z3 && ((charAt25 == ' ' || charAt25 == 'T') && charAt28 == ':'))) {
                        charAt25 = charAt(this.bp + 14);
                        charAt23 = charAt26;
                        charAt28 = '0';
                        charAt26 = charAt(this.bp + 15);
                        charAt24 = charAt27;
                        charAt27 = '0';
                    }
                    if (!checkTime(charAt23, charAt24, charAt25, charAt26, charAt27, charAt28)) {
                        return false;
                    }
                    if (i != 17 || z4) {
                        c2 = '0';
                        i6 = 0;
                    } else {
                        char charAt29 = charAt(this.bp + 14);
                        char charAt30 = charAt(this.bp + 15);
                        char charAt31 = charAt(this.bp + 16);
                        if (charAt29 < '0' || charAt29 > '9' || charAt30 < '0' || charAt30 > '9' || charAt31 < '0' || charAt31 > '9') {
                            return false;
                        }
                        i6 = ((charAt29 - '0') * 100) + ((charAt30 - '0') * 10) + (charAt31 - '0');
                        c2 = '0';
                    }
                    int i14 = charAt24 - c2;
                    i3 = ((charAt25 - c2) * 10) + (charAt26 - c2);
                    i4 = ((charAt27 - c2) * 10) + (charAt28 - c2);
                    i2 = i6;
                    i5 = i14 + ((charAt23 - c2) * 10);
                } else {
                    i2 = 0;
                    i3 = 0;
                    i4 = 0;
                    i5 = 0;
                }
                this.calendar.set(11, i5);
                this.calendar.set(12, i3);
                this.calendar.set(13, i4);
                this.calendar.set(14, i2);
                this.token = 5;
                return true;
            }
            return false;
        } else if (i < 9) {
            return false;
        } else {
            char charAt32 = charAt(this.bp);
            char charAt33 = charAt(this.bp + 1);
            char charAt34 = charAt(this.bp + 2);
            char charAt35 = charAt(this.bp + 3);
            char charAt36 = charAt(this.bp + 4);
            char charAt37 = charAt(this.bp + 5);
            char charAt38 = charAt(this.bp + 6);
            char charAt39 = charAt(this.bp + 7);
            char charAt40 = charAt(this.bp + 8);
            char charAt41 = charAt(this.bp + 9);
            if ((charAt36 != '-' || charAt39 != '-') && (charAt36 != '/' || charAt39 != '/')) {
                if (charAt36 == '-' && charAt38 == '-') {
                    if (charAt40 == ' ') {
                        charAt41 = charAt35;
                        charAt38 = charAt37;
                        charAt40 = charAt39;
                        c3 = '0';
                        charAt37 = '0';
                        i7 = 8;
                    } else {
                        charAt41 = charAt35;
                        charAt38 = charAt37;
                        c3 = charAt39;
                        charAt37 = '0';
                        i7 = 9;
                    }
                } else if ((charAt34 == '.' && charAt37 == '.') || (charAt34 == '-' && charAt37 == '-')) {
                    charAt37 = charAt35;
                    charAt34 = charAt40;
                    c3 = charAt32;
                    charAt40 = charAt33;
                    charAt33 = charAt39;
                    charAt32 = charAt38;
                    i7 = 10;
                    charAt38 = charAt36;
                } else if (charAt36 != 24180 && charAt36 != 45380) {
                    return false;
                } else {
                    if (charAt39 == 26376 || charAt39 == 50900) {
                        if (charAt41 == 26085 || charAt41 == 51068) {
                            charAt41 = charAt35;
                            c3 = '0';
                        } else if (charAt(this.bp + 10) != 26085 && charAt(this.bp + 10) != 51068) {
                            return false;
                        } else {
                            i7 = 11;
                        }
                    } else if (charAt38 != 26376 && charAt38 != 50900) {
                        return false;
                    } else {
                        if (charAt40 == 26085 || charAt40 == 51068) {
                            charAt41 = charAt35;
                            charAt38 = charAt37;
                            charAt40 = charAt39;
                            c3 = '0';
                        } else if (charAt41 != 26085 && charAt41 != 51068) {
                            return false;
                        } else {
                            charAt41 = charAt35;
                            charAt38 = charAt37;
                            c3 = charAt39;
                        }
                        charAt37 = '0';
                    }
                    i7 = 10;
                }
                if (checkDate(charAt32, charAt33, charAt34, charAt41, charAt37, charAt38, c3, charAt40)) {
                    return false;
                }
                setCalendar(charAt32, charAt33, charAt34, charAt41, charAt37, charAt38, c3, charAt40);
                char charAt42 = charAt(this.bp + i7);
                if (charAt42 != 'T' && (charAt42 != ' ' || z)) {
                    if (charAt42 == '\"' || charAt42 == 26 || charAt42 == 26085 || charAt42 == 51068) {
                        this.calendar.set(11, 0);
                        this.calendar.set(12, 0);
                        this.calendar.set(13, 0);
                        this.calendar.set(14, 0);
                        int i15 = this.bp + i7;
                        this.bp = i15;
                        this.ch = charAt(i15);
                        this.token = 5;
                        return true;
                    } else if ((charAt42 == '+' || charAt42 == '-') && this.len == i7 + 6 && charAt(this.bp + i7 + 3) == ':' && charAt(this.bp + i7 + 4) == '0' && charAt(this.bp + i7 + 5) == '0') {
                        setTime('0', '0', '0', '0', '0', '0');
                        this.calendar.set(14, 0);
                        setTimeZone(charAt42, charAt(this.bp + i7 + 1), charAt(this.bp + i7 + 2));
                        return true;
                    } else {
                        return false;
                    }
                }
                int i16 = i7 + 9;
                if (i >= i16 && charAt(this.bp + i7 + 3) == ':' && charAt(this.bp + i7 + 6) == ':') {
                    char charAt43 = charAt(this.bp + i7 + 1);
                    char charAt44 = charAt(this.bp + i7 + 2);
                    char charAt45 = charAt(this.bp + i7 + 4);
                    char charAt46 = charAt(this.bp + i7 + 5);
                    char charAt47 = charAt(this.bp + i7 + 7);
                    char charAt48 = charAt(this.bp + i7 + 8);
                    if (checkTime(charAt43, charAt44, charAt45, charAt46, charAt47, charAt48)) {
                        setTime(charAt43, charAt44, charAt45, charAt46, charAt47, charAt48);
                        char charAt49 = charAt(this.bp + i7 + 9);
                        if (charAt49 != '.') {
                            this.calendar.set(14, 0);
                            int i17 = this.bp + i16;
                            this.bp = i17;
                            this.ch = charAt(i17);
                            this.token = 5;
                            if (charAt49 == 'Z' && this.calendar.getTimeZone().getRawOffset() != 0) {
                                String[] availableIDs = TimeZone.getAvailableIDs(0);
                                if (availableIDs.length > 0) {
                                    this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs[0]));
                                }
                            }
                            return true;
                        }
                        int i18 = i7 + 11;
                        if (i >= i18 && (charAt = charAt(this.bp + i7 + 10)) >= '0' && charAt <= '9') {
                            int i19 = charAt - '0';
                            if (i <= i18 || (charAt4 = charAt(this.bp + i7 + 11)) < '0' || charAt4 > '9') {
                                i8 = 1;
                            } else {
                                i19 = (i19 * 10) + (charAt4 - '0');
                                i8 = 2;
                            }
                            if (i8 == 2 && (charAt3 = charAt(this.bp + i7 + 12)) >= '0' && charAt3 <= '9') {
                                i19 = (i19 * 10) + (charAt3 - '0');
                                i8 = 3;
                            }
                            this.calendar.set(14, i19);
                            char charAt50 = charAt(this.bp + i7 + 10 + i8);
                            if (charAt50 == '+' || charAt50 == '-') {
                                char charAt51 = charAt(this.bp + i7 + 10 + i8 + 1);
                                if (charAt51 >= '0' && charAt51 <= '1' && (charAt2 = charAt(this.bp + i7 + 10 + i8 + 2)) >= '0' && charAt2 <= '9') {
                                    char charAt52 = charAt(this.bp + i7 + 10 + i8 + 3);
                                    if (charAt52 == ':') {
                                        if (charAt(this.bp + i7 + 10 + i8 + 4) != '0' || charAt(this.bp + i7 + 10 + i8 + 5) != '0') {
                                            return false;
                                        }
                                        i9 = 6;
                                    } else if (charAt52 != '0') {
                                        i9 = 3;
                                    } else if (charAt(this.bp + i7 + 10 + i8 + 4) != '0') {
                                        return false;
                                    } else {
                                        i9 = 5;
                                    }
                                    setTimeZone(charAt50, charAt51, charAt2);
                                    i10 = i9;
                                }
                            } else if (charAt50 == 'Z') {
                                if (this.calendar.getTimeZone().getRawOffset() != 0) {
                                    String[] availableIDs2 = TimeZone.getAvailableIDs(0);
                                    if (availableIDs2.length > 0) {
                                        this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs2[0]));
                                    }
                                }
                                i10 = 1;
                            } else {
                                i10 = 0;
                            }
                            int i20 = i7 + 10 + i8 + i10;
                            char charAt53 = charAt(this.bp + i20);
                            if (charAt53 == 26 || charAt53 == '\"') {
                                int i21 = this.bp + i20;
                                this.bp = i21;
                                this.ch = charAt(i21);
                                this.token = 5;
                                return true;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            i7 = 10;
            charAt40 = charAt41;
            charAt41 = charAt35;
            c3 = charAt40;
            if (checkDate(charAt32, charAt33, charAt34, charAt41, charAt37, charAt38, c3, charAt40)) {
            }
        }
    }

    protected void setTime(char c, char c2, char c3, char c4, char c5, char c6) {
        this.calendar.set(11, ((c - '0') * 10) + (c2 - '0'));
        this.calendar.set(12, ((c3 - '0') * 10) + (c4 - '0'));
        this.calendar.set(13, ((c5 - '0') * 10) + (c6 - '0'));
    }

    protected void setTimeZone(char c, char c2, char c3) {
        int i = (((c2 - '0') * 10) + (c3 - '0')) * 3600 * 1000;
        if (c == '-') {
            i = -i;
        }
        if (this.calendar.getTimeZone().getRawOffset() != i) {
            String[] availableIDs = TimeZone.getAvailableIDs(i);
            if (availableIDs.length > 0) {
                this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs[0]));
            }
        }
    }

    private void setCalendar(char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
        Calendar calendar = Calendar.getInstance(this.timeZone, this.locale);
        this.calendar = calendar;
        calendar.set(1, ((c - '0') * 1000) + ((c2 - '0') * 100) + ((c3 - '0') * 10) + (c4 - '0'));
        this.calendar.set(2, (((c5 - '0') * 10) + (c6 - '0')) - 1);
        this.calendar.set(5, ((c7 - '0') * 10) + (c8 - '0'));
    }

    public static final byte[] decodeFast(String str, int i, int i2) {
        int i3;
        int i4 = 0;
        if (i2 == 0) {
            return new byte[0];
        }
        int i5 = (i + i2) - 1;
        while (i < i5 && IA[str.charAt(i)] < 0) {
            i++;
        }
        while (i5 > 0 && IA[str.charAt(i5)] < 0) {
            i5--;
        }
        int i6 = str.charAt(i5) == '=' ? str.charAt(i5 + (-1)) == '=' ? 2 : 1 : 0;
        int i7 = (i5 - i) + 1;
        if (i2 > 76) {
            i3 = (str.charAt(76) == '\r' ? i7 / 78 : 0) << 1;
        } else {
            i3 = 0;
        }
        int i8 = (((i7 - i3) * 6) >> 3) - i6;
        byte[] bArr = new byte[i8];
        int i9 = (i8 / 3) * 3;
        int i10 = 0;
        int i11 = 0;
        while (i10 < i9) {
            int[] iArr = IA;
            int i12 = i + 1;
            int i13 = i12 + 1;
            int i14 = (iArr[str.charAt(i)] << 18) | (iArr[str.charAt(i12)] << 12);
            int i15 = i13 + 1;
            int i16 = i14 | (iArr[str.charAt(i13)] << 6);
            int i17 = i15 + 1;
            int i18 = i16 | iArr[str.charAt(i15)];
            int i19 = i10 + 1;
            bArr[i10] = (byte) (i18 >> 16);
            int i20 = i19 + 1;
            bArr[i19] = (byte) (i18 >> 8);
            int i21 = i20 + 1;
            bArr[i20] = (byte) i18;
            if (i3 <= 0 || (i11 = i11 + 1) != 19) {
                i = i17;
            } else {
                i = i17 + 2;
                i11 = 0;
            }
            i10 = i21;
        }
        if (i10 < i8) {
            int i22 = 0;
            while (i <= i5 - i6) {
                i4 |= IA[str.charAt(i)] << (18 - (i22 * 6));
                i22++;
                i++;
            }
            int i23 = 16;
            while (i10 < i8) {
                bArr[i10] = (byte) (i4 >> i23);
                i23 -= 8;
                i10++;
            }
        }
        return bArr;
    }
}
